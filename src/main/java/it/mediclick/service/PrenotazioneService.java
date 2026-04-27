package it.mediclick.service;

import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PrenotazioneService {

    private PrenotazioneDAO prenotazioneDAO;
    private DisponibilitaDAO disponibilitaDAO;
    private Contex _contex;

    public PrenotazioneService(Contex contex) {
        this._contex = contex;
        this.prenotazioneDAO = new PrenotazioneDAO(contex);
        this.disponibilitaDAO = new DisponibilitaDAO(contex);
    }

    public boolean bloccaDisponibilita(int disponibilitaId, int pazienteId) {
        try {
            Disponibilita d = disponibilitaDAO.findById(disponibilitaId);
            if (d == null) return false;

            boolean isDisponibile = d.getStato() == Disponibilita.Stato.DISPONIBILE;
            boolean isBloccoScaduto = d.getStato() == Disponibilita.Stato.BLOCCATA && 
                                      d.getTimestampBlocco() != null && 
                                      d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now());

            if (isDisponibile || isBloccoScaduto) {
                disponibilitaDAO.setBlocco(disponibilitaId, pazienteId, true);
                return true;
            }
            
            return false;
        } 
        catch (SQLException e) 
        {
            System.err.println("Errore durante il blocco della disponibilità: " + e.getMessage());
            return false;
        }
    }

    
    public boolean creaPrenotazione(int pazienteId, int disponibilitaId) {
        try (Connection conn = _contex.getConnection()) 
        {
            conn.setAutoCommit(false);
            try 
            {
                Disponibilita d = disponibilitaDAO.findById(disponibilitaId);
                
                if (d == null || d.getStato() != Disponibilita.Stato.BLOCCATA) 
                {
                    return false;
                }

                if (d.getPazienteIdBlocco() == null || d.getPazienteIdBlocco() != pazienteId) 
                {
                    System.err.println("Tentativo di prenotazione su slot bloccato da un altro utente.");
                    return false;
                }
                
                if (d.getTimestampBlocco() != null && 
                    d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now())) 
                {
                    System.err.println("Tentativo di prenotazione su slot con blocco scaduto.");
                    return false;
                }

                
                Prenotazione p = new Prenotazione();
                p.setPazienteId(pazienteId);
                p.setDisponibilitaId(disponibilitaId);
                p.setDataPagamento(LocalDateTime.now());
                p.setStato(Prenotazione.Stato.CONFERMATA);
                
                prenotazioneDAO.insert(p,conn);

                disponibilitaDAO.updateStato(disponibilitaId, Disponibilita.Stato.PRENOTATA, conn);

                conn.commit();
                return true;
            } catch (SQLException e) 
            {
                if (conn != null) conn.rollback();
                System.err.println("Errore transazione prenotazione: " + e.getMessage());
                return false;
            }
        } 
        catch (SQLException e) 
        {
            System.err.println("Errore connessione prenotazione: " + e.getMessage());
            return false;
        }
    }

    
    public boolean disdiciPrenotazione(int prenotazioneId)
    {
        try (Connection conn = _contex.getConnection()) 
        {
            conn.setAutoCommit(false);
            try {
                Prenotazione p = prenotazioneDAO.findById(prenotazioneId);
                if (p == null || p.getStato() == Prenotazione.Stato.CANCELLATA) 
                {
                    return false;
                }
                
                prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.CANCELLATA, conn);
                disponibilitaDAO.updateStato(p.getDisponibilitaId(), Disponibilita.Stato.DISPONIBILE, conn);

                conn.commit();
                return true;
            } 
            catch (SQLException e) 
            {
                conn.rollback();
                return false;
            }
        } 
        catch (SQLException e) 
        {
            return false;
        }
    }

    public List<Prenotazione> getPrenotazioniPaziente(int pazienteId, boolean future) throws SQLException 
    {   
        return prenotazioneDAO.findByPaziente(pazienteId);
    }

    public boolean concludiVisita(int prenotazioneId, String note) 
    {
        try 
        {
            prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.COMPLETATA);
            return true;
        } 
        catch (SQLException e) 
        {
            return false;
        }
    }
}
