package it.mediclick.model.dao;

import it.mediclick.model.bean.Prenotazione;
import it.mediclick.util.Contex;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PrenotazioneDAO 
{
    private Contex _contex;

    public PrenotazioneDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Prenotazione findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Prenotazione 
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
             System.err.println("Errore nella ricerca della prenotazione by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Prenotazione> findByPaziente(int pazienteId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Prenotazione 
                        WHERE Paziente_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, pazienteId);
        List<Prenotazione> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca prenotazioni per paziente: " + e.getMessage());
             throw e;
        }
    }

    public List<Prenotazione> findByMedico(int medicoId) throws SQLException 
    {
        String sql = """
                        SELECT P.* 
                        FROM Prenotazione P 
                        JOIN Disponibilita D ON P.Disponibilita_ID = D.ID 
                        WHERE D.Medico_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<Prenotazione> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca prenotazioni per medico: " + e.getMessage());
             throw e;
        }
    }

    public List<Prenotazione> findByStato(Prenotazione.Stato stato) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Prenotazione 
                        WHERE Stato = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, stato.getLabel());
        List<Prenotazione> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca prenotazioni per stato: " + e.getMessage());
             throw e;
        }
    }

    public void insert(Prenotazione p) throws SQLException 
    {
        String sql = """
                        INSERT INTO Prenotazione(Paziente_ID, Disponibilita_ID, ErogazionePrestazione_ID, CodiceSconto_ID, Stato, Metodo_Pagamento, ID_Transazione_Esterno, Importo_Pagato, Ricavo_Netto_Medico_Euro, Trattenuta_Piattaforma_Euro, Tasse_Stimate_Euro, Data_Pagamento) 
                        VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                p.getPazienteId(),
                p.getDisponibilitaId(),
                p.getErogazionePrestazioneId(),
                p.getCodiceScontoId(),
                p.getStato() != null ? p.getStato().getLabel() : "Confermata",
                p.getMetodoPagamento(),
                p.getIdTransazioneEsterno(),
                p.getImportoPagato(),
                p.getRicavoNettoMedicoEuro(),
                p.getTrattenutaPiattaformaEuro(),
                p.getTasseStimateEuro(),
                p.getDataPagamento() != null ? Timestamp.valueOf(p.getDataPagamento()) : null
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento della prenotazione: " + e.getMessage());
             throw e;
        }
    }

    public void updateStato(int id, Prenotazione.Stato stato) throws SQLException 
    {
        String sql = """
                        UPDATE Prenotazione 
                        SET Stato = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento stato prenotazione: " + e.getMessage());
             throw e;
        }
    }

    private Prenotazione mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Prenotazione p = new Prenotazione();
            p.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            p.setPazienteId(Integer.parseInt(String.valueOf(map.get("Paziente_ID"))));
            p.setDisponibilitaId(Integer.parseInt(String.valueOf(map.get("Disponibilita_ID"))));
            
            if (map.get("ErogazionePrestazione_ID") != null) 
            {
                p.setErogazionePrestazioneId(Integer.parseInt(String.valueOf(map.get("ErogazionePrestazione_ID"))));
            }
            if (map.get("CodiceSconto_ID") != null) 
            {
                p.setCodiceScontoId(Integer.parseInt(String.valueOf(map.get("CodiceSconto_ID"))));
            }

            String statoStr = (String) map.get("Stato");
            if (statoStr != null) 
            {
                for (Prenotazione.Stato s : Prenotazione.Stato.values()) 
                {
                    if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr)) 
                    {
                        p.setStato(s);
                        break;
                    }
                }
            }

            p.setMetodoPagamento((String) map.get("Metodo_Pagamento"));
            p.setIdTransazioneEsterno((String) map.get("ID_Transazione_Esterno"));

            if (map.get("Importo_Pagato") != null) 
                p.setImportoPagato((BigDecimal) map.get("Importo_Pagato"));
                
            if (map.get("Ricavo_Netto_Medico_Euro") != null) 
                p.setRicavoNettoMedicoEuro((BigDecimal) map.get("Ricavo_Netto_Medico_Euro"));
                
            if (map.get("Trattenuta_Piattaforma_Euro") != null) 
                p.setTrattenutaPiattaformaEuro((BigDecimal) map.get("Trattenuta_Piattaforma_Euro"));
                
            if (map.get("Tasse_Stimate_Euro") != null) 
                p.setTasseStimateEuro((BigDecimal) map.get("Tasse_Stimate_Euro"));

            if (map.get("Data_Pagamento") != null) 
            {
                Timestamp ts = (Timestamp) map.get("Data_Pagamento");
                p.setDataPagamento(ts.toLocalDateTime());
            }

            return p;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di Prenotazione: " + e.getMessage(), e);
        }
    }
}
