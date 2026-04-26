package it.mediclick.model.dao;

import it.mediclick.model.bean.Recensione;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecensioneDAO 
{
    private Contex _contex;

    public RecensioneDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Recensione findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Recensione 
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
             System.err.println("Errore nella ricerca della recensione by id: " + e.getMessage());
             throw e;
        }
    }

    public Recensione findByPrenotazione(int prenotazioneId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Recensione 
                        WHERE Prenotazione_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, prenotazioneId);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca della recensione per prenotazione: " + e.getMessage());
             throw e;
        }
    }

    public List<Recensione> findByMedico(int medicoId) throws SQLException 
    {
        String sql = """
                        SELECT R.* 
                        FROM Recensione R 
                        JOIN Prenotazione P ON R.Prenotazione_ID = P.ID 
                        JOIN Disponibilita D ON P.Disponibilita_ID = D.ID 
                        WHERE D.Medico_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<Recensione> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca recensioni per medico: " + e.getMessage());
             throw e;
        }
    }

    public void insert(Recensione r) throws SQLException 
    {
        String sql = """
                        INSERT INTO Recensione(Prenotazione_ID, Voto, Commento, is_visible, Data_Pubblicazione) 
                        VALUES (?,?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                r.getPrenotazioneId(),
                r.getVoto(),
                r.getCommento(),
                r.isVisible(),
                r.getDataPubblicazione() != null ? Timestamp.valueOf(r.getDataPubblicazione()) : null
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento della recensione: " + e.getMessage());
             throw e;
        }
    }

    public void setVisibile(int id, boolean visibile) throws SQLException 
    {
        String sql = """
                        UPDATE Recensione 
                        SET is_visible = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, visibile, id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento visibilita recensione: " + e.getMessage());
             throw e;
        }
    }

    private Recensione mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Recensione r = new Recensione();
            r.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            r.setPrenotazioneId(Integer.parseInt(String.valueOf(map.get("Prenotazione_ID"))));
            r.setVoto(Integer.parseInt(String.valueOf(map.get("Voto"))));
            r.setCommento((String) map.get("Commento"));
            
            if (map.get("is_visible") != null) 
            {
                r.setVisible(Boolean.parseBoolean(String.valueOf(map.get("is_visible"))));
            }

            if (map.get("Data_Pubblicazione") != null) 
            {
                r.setDataPubblicazione(LocalDateTime.parse(String.valueOf(map.get("Data_Pubblicazione"))));
            }

            return r;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di Recensione: " + e.getMessage(), e);
        }
    }
}
