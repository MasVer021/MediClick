package it.mediclick.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Contex 
{
    private String url;
    private String userName;
    private String password;

    public Contex(String dbUrl, String userName, String password) 
    {
        this.url = dbUrl;
        this.userName = userName;
        this.password = password;

        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } 
        catch (ClassNotFoundException e) 
        {
            e.printStackTrace();
        }
    }
    
    public int eseguiUpdate(String sql, Object... params) throws SQLException
    {
        boolean isInsert = sql.trim().toUpperCase().startsWith("INSERT");
        try
        (
            Connection conn = DriverManager.getConnection(this.url, this.userName, this.password);
            PreparedStatement pstmt = isInsert ? 
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : 
                conn.prepareStatement(sql);
        )
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
        try (PreparedStatement pstmt = isInsert ? 
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : 
                conn.prepareStatement(sql))
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
        return DriverManager.getConnection(this.url, this.userName, this.password);
    }

    public List<Map<String, Object>> eseguiSelect(String sql, Object... params) throws SQLException 
    {
        List<Map<String, Object>> risultati = new ArrayList<>();

        try 
        (
            Connection conn = DriverManager.getConnection(this.url, this.userName, this.password);
            PreparedStatement pstmt = conn.prepareStatement(sql);
        ) 
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
}