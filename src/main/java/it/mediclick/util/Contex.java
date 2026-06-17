package it.mediclick.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class Contex
{
	private DataSource dataSource;

	public Contex()
	{
		try
		{
			Context initCtx = new InitialContext();
			Context envCtx = (Context) initCtx.lookup("java:comp/env");
			this.dataSource = (DataSource) envCtx.lookup("jdbc/MediClickDB");
		}
		catch (NamingException e)
		{
			e.printStackTrace();
			throw new RuntimeException("Impossibile trovare il DataSource JNDI jdbc/MediClickDB", e);
		}
	}

	public int eseguiUpdate(String sql, Object... params) throws SQLException
	{
		boolean isInsert = sql.trim().toUpperCase().startsWith("INSERT");
		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = isInsert ? conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sql);)
		{
			for (int i = 0; i < params.length; i++)
			{
				pstmt.setObject(i + 1, params[i]);
			}
			int rows = pstmt.executeUpdate();

			if (isInsert)
			{
				try (ResultSet keys = pstmt.getGeneratedKeys())
				{
					if (keys.next())
					{
						return keys.getInt(1);
					}
				}
			}
			return rows;
		}
	}

	public int eseguiUpdate(String sql, Connection conn, Object... params) throws SQLException
	{
		boolean isInsert = sql.trim().toUpperCase().startsWith("INSERT");
		try (PreparedStatement pstmt = isInsert ? conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : conn.prepareStatement(sql))
		{
			for (int i = 0; i < params.length; i++)
			{
				pstmt.setObject(i + 1, params[i]);
			}
			int rows = pstmt.executeUpdate();

			if (isInsert)
			{
				try (ResultSet keys = pstmt.getGeneratedKeys())
				{
					if (keys.next())
					{
						return keys.getInt(1);
					}
				}
			}
			return rows;
		}
	}

	public Connection getConnection() throws SQLException
	{
		return dataSource.getConnection();
	}

	public List<Map<String, Object>> eseguiSelect(String sql, Object... params) throws SQLException
	{
		List<Map<String, Object>> risultati = new ArrayList<>();

		try (Connection conn = dataSource.getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);)
		{
			for (int i = 0; i < params.length; i++)
			{
				pstmt.setObject(i + 1, params[i]);
			}

			try (ResultSet rs = pstmt.executeQuery())
			{
				ResultSetMetaData metaData = rs.getMetaData();
				int colCount = metaData.getColumnCount();

				while (rs.next())
				{
					Map<String, Object> riga = new HashMap<>();
					for (int i = 1; i <= colCount; i++)
					{
						riga.put(metaData.getColumnLabel(i), rs.getObject(i));
					}
					risultati.add(riga);
				}
			}
		}

		return risultati;
	}

	public <T> List<T> eseguiSelect(String sql, ResultMapper<T> mapper, Object... params) throws SQLException
	{
		List<Map<String, Object>> rows = eseguiSelect(sql, params);
		List<T> result = new ArrayList<>();
		for (Map<String, Object> row : rows)
		{
			result.add(mapper.map(row));
		}
		return result;
	}

	public <T> Optional<T> eseguiSelectSingolo(String sql, ResultMapper<T> mapper, Object... params) throws SQLException
	{
		List<T> result = eseguiSelect(sql, mapper, params);
		return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
	}
}