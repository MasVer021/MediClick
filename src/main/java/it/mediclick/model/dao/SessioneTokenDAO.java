package it.mediclick.model.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import it.mediclick.model.bean.SessioneToken;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class SessioneTokenDAO
{
	private final Contex _contex;

	public SessioneTokenDAO(Contex contex)
	{
		_contex = contex;
	}

	public Optional<SessioneToken> getTokenbyId(int id) throws SQLException
	{
		try
		{
			String sql = """
							  SELECT *
							  FROM sessionetoken
							  WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, tokenMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel recupero del token ", e);
		}
	}

	public Optional<SessioneToken> getTokenbyToken(String token) throws SQLException
	{
		try
		{
			String sql = """
							  SELECT *
							  FROM sessionetoken
							  WHERE Token = ?
							""";

			return _contex.eseguiSelectSingolo(sql, tokenMapper, token);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel recupero del token ", e);
		}
	}

	public String insertToken(int idUtente) throws SQLException
	{
		try
		{
			String sql = """
							  INSERT INTO sessionetoken(Utente_ID, Token,Scadenza)
								VALUES (?, ?,?)
							""";
			String token = UUID.randomUUID().toString();
			LocalDate scadenza = LocalDate.now().plusYears(1);

			_contex.eseguiUpdate(sql, idUtente, token, scadenza);

			return token;

		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'inserimento del token ", e);
		}
	}

	public void deleteToken(String token) throws SQLException
	{
		String sql = """
						   DELETE FROM sessionetoken
						   WHERE Token = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, token);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella cancellazione del token ", e);
		}
	}

	private final ResultMapper<SessioneToken> tokenMapper = row ->
	{
		SessioneToken st = new SessioneToken();

		st.setId(MapRow.getIntOrNull(row, "ID"));
		st.setScadenza(MapRow.getLocalDate(row, "Scadenza"));
		st.setToken(MapRow.getString(row, "Token"));
		st.setUtenteId(MapRow.getIntOrNull(row, "Utente_ID"));

		return st;
	};

}
