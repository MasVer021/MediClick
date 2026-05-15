package it.mediclick.model.dao;

import it.mediclick.model.bean.Permesso;
import it.mediclick.model.bean.Ruolo;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class RuoloDAO 
{
    private final Contex _contex;

    public RuoloDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Optional<Ruolo> findById(int id) throws SQLException 
    { 
        try
        {
             String sql =   """
                            SELECT * 
                            FROM Ruolo 
                            WHERE ID = ?
                            """;

           return _contex.eseguiSelectSingolo(sql, ruoloMapper, id);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca del ruolo per ID: " + id + e.getMessage(), e);
        }
    }
    
    
    public Integer findByCodice(String codice) throws SQLException 
    { 
        try
        {
             String sql =   """
                            SELECT * 
                            FROM Ruolo 
                            WHERE Codice = ?
                            """;

           return _contex.eseguiSelectSingolo(sql, ruoloMapper, codice).orElseThrow(()-> new SQLException("Codice non trovato")).getId();
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca del ruolo :"+ e.getMessage(), e);
        }
    }

    public List<Ruolo> findAll() throws SQLException 
    { 
        try
        {
            String sql = """
                        SELECT * 
                        FROM Ruolo
                         """;
           
            return _contex.eseguiSelect(sql, ruoloMapper);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca di tutti i ruoli: " + e.getMessage(), e);
        }
    }

    public Optional<Ruolo> findByUtente(int utenteId) throws SQLException 
    {   
        try
        {

             String sql = """
                        SELECT R.* 
                        FROM Ruolo R 
                        JOIN Utente U ON U.Ruolo_ID = R.ID 
                        WHERE U.ID = ?
                        """;
           return _contex.eseguiSelectSingolo(sql, ruoloMapper, utenteId);
        }
        catch(SQLException e)
        {
           throw new SQLException("Errore nella ricerca del ruolo per ID utente: " + utenteId + e.getMessage(), e);
        }
    }

    public void addPermesso(int ruoloId, int permessoId) throws SQLException 
    {
        String sql =    """
                        INSERT INTO Caratterizzato(Ruolo_ID, Permesso_ID) 
                        VALUES (?, ?)
                        """;
        try
        {
            _contex.eseguiUpdate(sql, ruoloId, permessoId);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nell'aggiunta del permesso al ruolo: " + e.getMessage(), e);
        }
    }

    public void removePermesso(int ruoloId, int permessoId) throws SQLException 
    {
        String sql =    """
                        DELETE FROM Caratterizzato 
                        WHERE Ruolo_ID = ? AND Permesso_ID = ?
                        """;
        try
        {
            _contex.eseguiUpdate(sql, ruoloId, permessoId);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella rimozione del permesso dal ruolo: " + e.getMessage(), e);
        }
    }

    public List<Permesso> findPermessiByRuolo(int ruoloId) throws SQLException 
    {
        try
        {
             String sql =   """
                            SELECT P.* 
                            FROM Permesso P 
                            JOIN Caratterizzato C ON P.ID = C.Permesso_ID 
                            WHERE C.Ruolo_ID = ?
                            """;

            return _contex.eseguiSelect(sql, ruoloPermesso, ruoloId);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca permessi per ruolo: " + e.getMessage(), e);
        }
    }

    public void getCompleto(Ruolo r) throws SQLException
	{
        int ruoloId = r.getId();
        r.setPermessi(findPermessiByRuolo(ruoloId));
    }
    
    private final ResultMapper<Permesso> ruoloPermesso = row ->
    {
        Permesso p = new Permesso();
        p.setId(MapRow.getInt(row, "ID"));
        p.setCodice(MapRow.getString(row, "Codice"));
        p.setDescrizione(MapRow.getString(row, "Descrizione"));
        
        return p;
    };

    private final ResultMapper<Ruolo> ruoloMapper = row ->
    {
        Ruolo r = new Ruolo();

        int ruoloId = MapRow.getInt(row, "ID");

        r.setId(ruoloId);
        r.setCodice(MapRow.getString(row, "Codice"));
        r.setDescrizione(MapRow.getString(row, "Descrizione"));

        //r.setPermessi(findPermessiByRuolo(ruoloId));

        return r;
    };
}
