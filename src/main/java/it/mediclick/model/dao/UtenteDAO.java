package it.mediclick.model.dao;

import it.mediclick.model.bean.Utente;
import it.mediclick.util.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UtenteDAO 
{
	private Contex _contex;
	
	public UtenteDAO(Contex contex)
	{
		_contex = contex;
	}
	
	public Utente findById(int id) throws SQLException
	{
		String sql = "SELECT * FROM Utente WHERE ID = ?";
		
		List<Map<String, Object>> result = _contex.eseguiSelect(sql, id);
		
		if (result == null || result.isEmpty()) 
	        return null;
	    try
	    {
	    	return mapping(result.get(0));
	    }
	    catch(SQLException e)
	    {
	    	 System.err.println("Errore nella ricerca dell'utente by id: " + e.getMessage());
		     throw e;
	    }
		
	}
	
	public Utente findByEmail(String email) throws SQLException
	{
		String sql = "SELECT * FROM Utente WHERE Email = ?";
		
		List<Map<String, Object>> result = _contex.eseguiSelect(sql, email);
		
		if (result == null || result.isEmpty()) 
	        return null;
	    
		 try
		    {
		    	return mapping(result.get(0));
		    }
		    catch(SQLException e)
		    {
		    	 System.err.println("Errore nella ricerca dell'utente by email: " + e.getMessage());
			     throw e;
		    }
	}
	
	public int insert(Connection conn,String email, String password, LocalDate dataIscrizione, boolean accountAttivo, Integer ruoloId) throws SQLException
	{
		return insert(conn,new Utente(-1,email,password,dataIscrizione,accountAttivo,ruoloId));
	}
	
	public int insert(Connection conn,Utente u) throws SQLException
	{
		String sql = """
					INSERT INTO Utente(Email,Password,Data_Iscrizione,Account_attivo,Ruolo_ID)
					VALUES (?,?,?,?,?);
					""";
		
		int ID = -1;
		
		try
		{
			ID = _contex.eseguiUpdate(sql,conn,u.getEmail(),PasswordUtils.hashPassword(u.getPassword()),u.getDataIscrizione(),u.isAccountAttivo(),u.getRuoloId());
		}
		catch(SQLException e)
		{
			 System.err.println("Errore nell'inserimento dell'utente: " + e.getMessage());
		     throw e;  
		}
		
		return ID;
	}
	
	public void updatePassword(int id,String password) throws SQLException
	{
		String sql = """
						UPDATE Utente
						SET Password = ?
						WHERE ID = ?; 
				""";
		try
		{
			_contex.eseguiUpdate(sql,PasswordUtils.hashPassword(password),id);
		}
		catch(SQLException e)
		{
			 System.err.println("Errore nell'aggiornamento della passowrd utente: " + e.getMessage());
		     throw e;
		}
	}
	
	public void setAccountAttivo(int id,boolean attivo) throws SQLException
	{
		String sql = """
						UPDATE Utente
						SET Account_attivo = ?
						WHERE ID = ?; 
				""";
		try
		{
			_contex.eseguiUpdate(sql,attivo,id);
		}
		catch(SQLException e)
		{
			 System.err.println("Errore nell'aggiornamento dello stato account: " + e.getMessage());
		     throw e;
		}
	}
	
	private Utente mapping (Map<String,Object> u) throws SQLException
	{
		if (u == null) 
			return null;

	    try 
	    {
	        return new Utente(
	            Integer.parseInt(String.valueOf(u.get("ID"))),
	            (String) u.get("Email"),
	            (String) u.get("Password"),
	            LocalDate.parse(String.valueOf(u.get("Data_Iscrizione"))),
	            Boolean.parseBoolean(String.valueOf(u.get("Account_attivo"))),
	            Integer.parseInt(String.valueOf(u.get("Ruolo_ID")))
	        );
	    } 
	    catch (Exception e) 
	    {
	        throw new SQLException("Errore durante il mapping dei dati Utente: " + e.getMessage(), e);
	    }

	}
}
