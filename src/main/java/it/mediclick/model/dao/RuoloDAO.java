package it.mediclick.model.dao;

import it.mediclick.model.bean.Permesso;
import it.mediclick.model.bean.Ruolo;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RuoloDAO 
{
    private Contex _contex;

    public RuoloDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Ruolo findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Ruolo 
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
             System.err.println("Errore nella ricerca del ruolo by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Ruolo> findAll() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Ruolo
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<Ruolo> list = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return list;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                list.add(mapping(map));
            }
            return list;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca di tutti i ruoli: " + e.getMessage());
             throw e;
        }
    }

    public Ruolo findByUtente(int utenteId) throws SQLException 
    {
        String sql = """
                        SELECT R.* 
                        FROM Ruolo R 
                        JOIN Utente U ON U.Ruolo_ID = R.ID 
                        WHERE U.ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, utenteId);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca ruolo per utente: " + e.getMessage());
             throw e;
        }
    }

    public void addPermesso(int ruoloId, int permessoId) throws SQLException 
    {
        String sql = """
                        INSERT INTO Caratterizzato(Ruolo_ID, Permesso_ID) 
                        VALUES (?, ?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, ruoloId, permessoId);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiunta del permesso al ruolo: " + e.getMessage());
             throw e;
        }
    }

    public void removePermesso(int ruoloId, int permessoId) throws SQLException 
    {
        String sql = """
                        DELETE FROM Caratterizzato 
                        WHERE Ruolo_ID = ? AND Permesso_ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, ruoloId, permessoId);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella rimozione del permesso dal ruolo: " + e.getMessage());
             throw e;
        }
    }

    public List<Permesso> findPermessiByRuolo(int ruoloId) throws SQLException 
    {
        String sql = """
                        SELECT P.* 
                        FROM Permesso P 
                        JOIN Caratterizzato C ON P.ID = C.Permesso_ID 
                        WHERE C.Ruolo_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, ruoloId);
        List<Permesso> list = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return list;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                list.add(mappingPermesso(map));
            }
            return list;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca permessi per ruolo: " + e.getMessage());
             throw e;
        }
    }
    
    
    
    private Permesso mappingPermesso(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
        	Permesso p = new Permesso();
            p.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            p.setCodice((String) map.get("Codice"));
            p.setDescrizione((String) map.get("Descrizione"));
            return p;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping del permesso: " + e.getMessage(), e);
        }
    }
    

    private Ruolo mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Ruolo r = new Ruolo();
            r.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            r.setCodice((String) map.get("Codice"));
            r.setDescrizione((String) map.get("Descrizione"));
            return r;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di Ruolo: " + e.getMessage(), e);
        }
    }
}
