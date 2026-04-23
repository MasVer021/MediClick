package it.mediclick.model.dao;

import it.mediclick.model.bean.Paziente;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PazienteDAO 
{
    private Contex _contex;
    private UtenteDAO utente;

    public PazienteDAO(Contex contex) 
    {
        _contex = contex;
        utente = new UtenteDAO(contex);
    }

    public Paziente findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Paziente 
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
             System.err.println("Errore nella ricerca del paziente by id: " + e.getMessage());
             throw e;
        }
    }

    public Paziente findByCodiceFiscale(String cf) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Paziente 
                        WHERE Codice_Fiscale = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, cf);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca del paziente per codice fiscale: " + e.getMessage());
             throw e;
        }
    }

    public int insert(Paziente p) throws SQLException 
    {                        
        String sqlPaziente = """
                              INSERT INTO Paziente(ID,Cognome,Nome,Codice_Fiscale,Telefono,Data_Nascita) 
                              VALUES (?,?,?,?,?,?)
                             """;
        
        Connection conn = _contex.getConnection();
        conn.setAutoCommit(false);
        
        try 
        {
        	int utenteId = utente.insert(conn,p.getUtente());

            _contex.eseguiUpdate(sqlPaziente,
            	conn,
                utenteId,
                p.getCognome(),
                p.getNome(),
                p.getCodiceFiscale(),
                p.getTelefono(),
                p.getDataNascita()
            );

            p.setId(utenteId);
            
            p.getUtente().setId(utenteId);
            
            conn.commit();
            return utenteId;
            
        } 
        catch (SQLException e) 
        {
        	conn.rollback();
            System.err.println("Errore nell'inserimento del paziente: " + e.getMessage());
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

    public void update(Paziente p) throws SQLException 
    {
        String sql = """
                        UPDATE Paziente 
                        SET Cognome = ?, Nome = ?, Codice_Fiscale = ?, Telefono = ?, Data_Nascita = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, p.getCognome(), p.getNome(), p.getCodiceFiscale(), p.getTelefono(), p.getDataNascita(), p.getId());
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento del paziente: " + e.getMessage());
             throw e;
        }
    }

    private Paziente mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Paziente p = new Paziente();
            
            int id = Integer.parseInt(String.valueOf(map.get("ID")));
            
            p.setId(id);
            p.setCognome((String) map.get("Cognome"));
            p.setNome((String) map.get("Nome"));
            p.setCodiceFiscale((String) map.get("Codice_Fiscale"));
            p.setTelefono((String) map.get("Telefono"));
            
            if (map.get("Data_Nascita") != null) 
            {
                p.setDataNascita(LocalDate.parse(String.valueOf(map.get("Data_Nascita"))));
            }
            
            p.setUtente(utente.findById(id));
            return p;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping del Paziente: " + e.getMessage(), e);
        }
    }
}
