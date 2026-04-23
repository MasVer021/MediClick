package it.mediclick.model.dao;

import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Utente;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MedicoDAO 
{
    private Contex _contex;
    private UtenteDAO utente;

    public MedicoDAO(Contex contex) 
    {
        _contex = contex;
        utente = new UtenteDAO(contex);
    }

    public Medico findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Medico 
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
             System.err.println("Errore nella ricerca del medico by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Medico> findAll() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Medico
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<Medico> medici = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return medici;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                medici.add(mapping(map));
            }
            return medici;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca di tutti i medici: " + e.getMessage());
             throw e;
        }
    }

    public List<Medico> findByStato(Medico.StatoVerifica stato) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Medico 
                        WHERE Stato_verifica = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, stato.getLabel());
        List<Medico> medici = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return medici;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                medici.add(mapping(map));
            }
            return medici;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca medici per stato: " + e.getMessage());
             throw e;
        }
    }

    public int insert(Medico m) throws SQLException 
    {                          
        String sqlMedico = """
                            INSERT INTO Medico(ID,Cognome,Nome,Bio,P_Iva,Stato_verifica,Regime_fiscale) 
                            VALUES (?,?,?,?,?,?,?)
                           """;
        
        Connection conn = _contex.getConnection();
        conn.setAutoCommit(false);
        try
        {
        	int utenteId = utente.insert(conn,m.getUtente());
        	
            _contex.eseguiUpdate(sqlMedico,
            	conn,
                utenteId,
                m.getCognome(),
                m.getNome(),
                m.getBio(),
                m.getpIva(),
                m.getStatoVerifica() != null ? m.getStatoVerifica().getLabel() : "In attesa",
                m.getRegimeFiscaleId()
            );

            m.setId(utenteId);
           
            m.getUtente().setId(utenteId);
            conn.commit();
            return utenteId;
        } 
        catch (SQLException e) 
        {
        	conn.rollback();
            System.err.println("Errore nell'inserimento del medico: " + e.getMessage());
            throw e;
        }
        finally
        {
        	conn.close();
        }
    }
    
    public void update(Medico m) throws SQLException
    {
    	String sql = """
		                UPDATE Medico 
		                SET Cognome = ?, Nome = ?, Bio = ?,P_Iva = ?  
		                WHERE ID = ?
    				""";
    	try 
    	{
			_contex.eseguiUpdate(sql, m.getCognome(),m.getNome(),m.getBio(),m.getpIva());
		} 
    	catch (SQLException e) 
    	{
			 System.err.println("Errore nell'aggiornamento del medico: " + e.getMessage());
             throw e;
		}
    }
    
    public void updatePassword(int id, String password) throws SQLException
    {
    	utente.updatePassword(id, password);
    }
    
    public void updateStatoVerifica(int id, Medico.StatoVerifica stato) throws SQLException 
    {
        String sql = """
                        UPDATE Medico 
                        SET Stato_verifica = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento stato verifica medico: " + e.getMessage());
             throw e;
        }
    }

    public void updateRegimeFiscale(int id, int regimeFiscaleId) throws SQLException 
    {
        String sql = """
                        UPDATE Medico 
                        SET Regime_fiscale = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, regimeFiscaleId, id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento regime fiscale medico: " + e.getMessage());
             throw e;
        }
    }

    private Medico mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Medico m = new Medico();
            int id = Integer.parseInt(String.valueOf(map.get("ID")));
            
            
            m.setId(id);
            m.setCognome((String) map.get("Cognome"));
            m.setNome((String) map.get("Nome"));
            m.setBio((String) map.get("Bio"));
            m.setpIva((String) map.get("P_Iva"));

            String statoStr = (String) map.get("Stato_verifica");
            
            m.setStatoVerifica(Medico.StatoVerifica.fromString(statoStr));
            
            
            if (map.get("Regime_fiscale") != null) 
            {
                m.setRegimeFiscaleId(Integer.parseInt(String.valueOf(map.get("Regime_fiscale"))));
            }
            
            m.setUtente(utente.findById(id));
            
            return m;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping del Medico: " + e.getMessage(), e);
        }
    }
}
