package it.mediclick.model.dao;

import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class RecensioneDAO 
{
    private final Contex _contex;
    private final PrenotazioneDAO prenotazioneDAO;

    public RecensioneDAO(Contex contex) 
    {
        _contex = contex;
        prenotazioneDAO = new PrenotazioneDAO(_contex); 
    }

    public Optional<Recensione> findById(int id) throws SQLException 
    {
          
        try
        {
            String sql = """
                        SELECT * 
                        FROM Recensione 
                        WHERE ID = ?
                        """;

            return _contex.eseguiSelectSingolo(sql, recensioneMapper, id);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca della recensione per ID: " + id + e.getMessage(), e);
        }
    }

    public Optional<Recensione> findByPrenotazione(int prenotazioneId) throws SQLException 
    {             
        try
        {
            String sql = """
                        SELECT * 
                        FROM Recensione 
                        WHERE Prenotazione_ID = ?
                     """;

            return _contex.eseguiSelectSingolo(sql, recensioneMapper, prenotazioneId);
        }
        catch(SQLException e)
        {
            throw new SQLException("Errore nella ricerca della recensione per ID prenotazione: " + prenotazioneId + e.getMessage(), e);
        }
    }

    public List<Recensione> findByMedico(int medicoId) throws SQLException 
    {     
        try
        {

             String sql = """
                        SELECT R.* 
                        FROM Recensione R 
                        JOIN Prenotazione P ON R.Prenotazione_ID = P.ID 
                        JOIN Disponibilita D ON P.Disponibilita_ID = D.ID 
                        WHERE D.Medico_ID = ?
                     """;

           return _contex.eseguiSelect(sql, recensioneMapper, medicoId);
        }
        catch(SQLException e)
        {
             throw new SQLException("Errore nella ricerca delle recensioni per ID medico: " + medicoId + e.getMessage(), e);
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
            Integer prenotazioneId = r.getPrenotazioneId() > 0 ? r.getPrenotazioneId() : null;

            LocalDateTime dataPubblicazione = r.getDataPubblicazione() != null ? r.getDataPubblicazione() : null;
              
            _contex.eseguiUpdate(sql,prenotazioneId,r.getVoto(),r.getCommento(),r.isVisible(),dataPubblicazione);
        }
        catch(SQLException e)
        {
           throw new SQLException("Errore nell'inserimento della recensione: " + e.getMessage(), e);
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
           throw new SQLException("Errore nell'aggiornamento della visibilità della recensione con ID " + id + ": " + e.getMessage(), e);
        }
    }

    public void getCompleto(Recensione r) throws SQLException
	{
        int prenotazioneId = r.getPrenotazioneId();

        Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new SQLException("Prenotazione non trovata per ID: " + prenotazioneId));
        r.setPrenotazione(p);
    }

    private final ResultMapper<Recensione> recensioneMapper = row -> 
    {
        Recensione r = new Recensione();

        Integer prenotazioneId = MapRow.getIntOrNull(row, "Prenotazione_ID");
        prenotazioneId = prenotazioneId != null ? prenotazioneId : -1;

        r.setId(MapRow.getInt(row, "ID"));
        r.setVoto(MapRow.getInt(row, "Voto"));
        r.setCommento(MapRow.getString(row, "Commento"));
        r.setVisible(MapRow.getBoolean(row, "is_visible"));
        r.setDataPubblicazione(MapRow.getLocalDateTime(row, "Data_Pubblicazione"));

        r.setPrenotazioneId(prenotazioneId);

        return r;
    };
}
