package it.mediclick.model.dao;

import it.mediclick.model.bean.Studio;
import it.mediclick.util.Contex;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudioDAO 
{
    private Contex _contex;

    public StudioDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Studio findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Studio 
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
             System.err.println("Errore nella ricerca dello studio by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Studio> findAll() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Studio
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<Studio> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca di tutti gli studi: " + e.getMessage());
             throw e;
        }
    }

    public void insert(Studio s) throws SQLException 
    {
        String sql = """
                        INSERT INTO Studio(Place_ID, Nome_Sede, Indirizzo_Maps, Citta, Lat, Lng) 
                        VALUES (?,?,?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                s.getPlaceId(),
                s.getNomeSede(),
                s.getIndirizzoMaps(),
                s.getCitta(),
                s.getLat(),
                s.getLng()
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento dello studio: " + e.getMessage());
             throw e;
        }
    }

    public void update(Studio s) throws SQLException 
    {
        String sql = """
                        UPDATE Studio 
                        SET Place_ID = ?, Nome_Sede = ?, Indirizzo_Maps = ?, Citta = ?, Lat = ?, Lng = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                s.getPlaceId(),
                s.getNomeSede(),
                s.getIndirizzoMaps(),
                s.getCitta(),
                s.getLat(),
                s.getLng(),
                s.getId()
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento dello studio: " + e.getMessage());
             throw e;
        }
    }

    public void delete(int id) throws SQLException 
    {
        String sql = """
                        DELETE FROM Studio 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella cancellazione dello studio: " + e.getMessage());
             throw e;
        }
    }

    private Studio mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Studio s = new Studio();
            s.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            s.setPlaceId((String) map.get("Place_ID"));
            s.setNomeSede((String) map.get("Nome_Sede"));
            s.setIndirizzoMaps((String) map.get("Indirizzo_Maps"));
            s.setCitta((String) map.get("Citta"));
            
            if (map.get("Lat") != null) 
            {
                s.setLat(new BigDecimal(String.valueOf(map.get("Lat"))));
            }
            if (map.get("Lng") != null) 
            {
                s.setLng(new BigDecimal(String.valueOf(map.get("Lng"))));
            }

            return s;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di Studio: " + e.getMessage(), e);
        }
    }
}
