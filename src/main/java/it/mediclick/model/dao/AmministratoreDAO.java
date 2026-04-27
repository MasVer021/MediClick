package it.mediclick.model.dao;

import it.mediclick.model.bean.Amministratore;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AmministratoreDAO 
{
    private Contex _contex;
    private UtenteDAO utente;

    public AmministratoreDAO(Contex contex) 
    {
        _contex = contex;
        utente = new UtenteDAO(contex);
    }

    public Amministratore findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Amministratore 
                        WHERE ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, id);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca dell'amministratore by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Amministratore> findAll() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Amministratore
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<Amministratore> amms = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return amms;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                amms.add(mapping(map));
            }
            return amms;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca di tutti gli amministratori: " + e.getMessage());
             throw e;
        }
    }

    public int insert(Amministratore a) throws SQLException 
    {
        
                           
        String sqlAmministratore = """
                                    INSERT INTO Amministratore(ID,Dipartimento_ID) 
                                    VALUES (?,?)
                                   """;
        Connection conn = _contex.getConnection();
        conn.setAutoCommit(false);
        try
        {
        	
	        int utenteId = utente.insert(conn,a.getUtente());
	
	        _contex.eseguiUpdate(sqlAmministratore,
	        	conn,
	            utenteId,
	            a.getDipartimentoId()
	        );
	
	        a.setId(utenteId);
	        
	        a.getUtente().setId(utenteId);
	        conn.commit();
	        return utenteId;
        } 
	    catch (SQLException e) 
	    {
	    	conn.rollback();
	        System.err.println("Errore nell'inserimento dell'amministratore: " + e.getMessage());
	        throw e;
	    }
        finally
        {
        	conn.close();
        }
    }
    
    public void updatePassword(int id, String password) throws SQLException
    {
    	utente.updatePassword(id, password);
    }

    private Amministratore mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Amministratore a = new Amministratore();
            int id = Integer.parseInt(String.valueOf(map.get("ID")));
            
            a.setId(id);
            if (map.get("Dipartimento_ID") != null) 
            {
                a.setDipartimentoId(Integer.parseInt(String.valueOf(map.get("Dipartimento_ID"))));
            }
            
            a.setUtente(utente.findById(id));
            return a;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping dell'Amministratore: " + e.getMessage(), e);
        }
    }
}
