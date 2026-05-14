package it.mediclick.model.dao;

import it.mediclick.model.bean.Studio;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudioDAO 
{
    private final Contex _contex;

    public StudioDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Optional<Studio> findById(int id) throws SQLException 
    {
        try
        {
             String sql = """
                        SELECT * 
                        FROM Studio 
                        WHERE ID = ?
                        """;
            
            return _contex.eseguiSelectSingolo(sql, studioMapper, id);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca dello studio per ID: " + id + e.getMessage(), e);
        }
    }

    public List<Studio> findAll() throws SQLException 
    {    
        try
        {
             String sql = """
                        SELECT * 
                        FROM Studio
                     """;

            return _contex.eseguiSelect(sql, studioMapper);
           
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca di tutti gli studi: " + e.getMessage(), e);
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
            _contex.eseguiUpdate(sql,s.getPlaceId(),s.getNomeSede(),s.getIndirizzoMaps(),s.getCitta(),s.getLat(),s.getLng());
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nell'inserimento dello studio: " + e.getMessage(), e);
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
            _contex.eseguiUpdate(sql,s.getPlaceId(),s.getNomeSede(),s.getIndirizzoMaps(),s.getCitta(),s.getLat(),s.getLng(),s.getId());
        }
        catch(SQLException e)
        {
             throw new SQLException("Errore nell'aggiornamento dello studio: " + e.getMessage(), e);
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
             throw new SQLException("Errore nella cancellazione dello studio: " + e.getMessage(), e);
        }
    }

    private final ResultMapper<Studio> studioMapper = row -> 
    {
        Studio s = new Studio();

        s.setId(MapRow.getInt(row, "ID"));
        s.setPlaceId(MapRow.getString(row, "Place_ID"));
        s.setNomeSede(MapRow.getString(row, "Nome_Sede"));
        s.setIndirizzoMaps(MapRow.getString(row, "Indirizzo_Maps"));
        s.setCitta(MapRow.getString(row, "Citta"));
        s.setLat(MapRow.getBigDecimal(row, "Lat"));
        s.setLng(MapRow.getBigDecimal(row, "Lng"));
    
        return s;
    };
}
