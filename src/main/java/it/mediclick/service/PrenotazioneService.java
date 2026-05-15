package it.mediclick.service;

import it.mediclick.exception.*;
import it.mediclick.model.DTO.RiepilogoPrenotazioneDTO;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.CodiceScontoDAO;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.ImpostazioniSistemaDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.StudioDAO;
import it.mediclick.util.Contex;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class PrenotazioneService {

    private final PrenotazioneDAO prenotazioneDAO;
    private final DisponibilitaDAO disponibilitaDAO;
    private final ErogazionePrestazioneDAO erogazionePrestazioneDAO;
    private final MedicoDAO medicoDAO;
    private final StudioDAO studioDAO;
    private final CatalogoPrestazioniDAO catalogoPrestazioniDAO;
    private final CodiceScontoDAO codiceScontoDAO;
    private final ImpostazioniSistemaDAO impostazioniSistemaDAO;
    private final Contex _contex;

    public PrenotazioneService(Contex contex) 
    {
        this._contex = contex;
        this.prenotazioneDAO = new PrenotazioneDAO(contex);
        this.disponibilitaDAO = new DisponibilitaDAO(contex);
        this.erogazionePrestazioneDAO = new ErogazionePrestazioneDAO(contex);
        this.medicoDAO = new MedicoDAO(contex);
        this.studioDAO = new StudioDAO(contex);
        this.catalogoPrestazioniDAO = new CatalogoPrestazioniDAO(contex);
        this.codiceScontoDAO = new CodiceScontoDAO(contex);
        this.impostazioniSistemaDAO = new ImpostazioniSistemaDAO(contex);
    }
       
    public int getTrattenuta() throws PrenotazioneException
    {
    	try 
    	{
			return impostazioniSistemaDAO.findValueByKey("COMMISSIONE_PIATTAFORMA_PCT").orElseThrow(()-> new PrenotazioneException("Impostazione trattenuta piattaforma non trovata", "IMPOSTAZIONE_NOT_FOUND"));
		} 
    	catch (SQLException e) 
    	{
    		throw new PrenotazioneException("Errore nel recupero della trattenuta: " + e.getMessage(), "TRATTENUTA_ERROR");
    	}
    }
    
    
    public boolean isValid(CodiceSconto sconto) throws PrenotazioneException
    {
    	try 
    	{
			return codiceScontoDAO.isValid(sconto);
		} 
    	catch (SQLException e) 
    	{
			throw new PrenotazioneException("Errore durante la validazione dello sconto: " + e.getMessage(), "SCONTO_VALIDATION_ERROR");
		}
    }
    
    public CodiceSconto findSconto(String codice) throws PrenotazioneException
    {
        try 
        {
            return codiceScontoDAO.findByCodice(codice).orElseThrow(()-> new PrenotazioneException("Codice sconto non trovato", "SCONTO_NOT_FOUND"));	
    
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore durante la ricerca del sconto", "SCONTO_SEARCH_ERROR");
        }
    	
    }

    public boolean bloccaDisponibilita(int disponibilitaId, int pazienteId) throws PrenotazioneException 
    {
        try {
            Disponibilita d = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));

            boolean isDisponibile = d.getStato() == Disponibilita.Stato.DISPONIBILE;
            boolean isBloccoScaduto = d.getStato() == Disponibilita.Stato.BLOCCATA && 
                                      d.getTimestampBlocco() != null && 
                                      d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now());

            if (isDisponibile || isBloccoScaduto) 
            {
                disponibilitaDAO.setBlocco(disponibilitaId, pazienteId, true);
                return true;
            }
            
            throw new PrenotazioneException("Slot non disponibile o già bloccato", "SLOT_NOT_AVAILABLE");
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore durante il blocco della disponibilità: " + e.getMessage(), "BLOCCO_DISPONIBILITA_ERROR");
        }
    }

    public RiepilogoPrenotazioneDTO getRiepilogoPrenotazione(int idStudio, int idPrestazione, int idDisponibilita) throws PrenotazioneException
    {
        try {
            RiepilogoPrenotazioneDTO dto = new RiepilogoPrenotazioneDTO();
            
            Disponibilita disponibilita = disponibilitaDAO.findById(idDisponibilita).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));
            ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idPrestazione).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata", "PRESTAZIONE_NOT_FOUND"));
            Studio studio = studioDAO.findById(idStudio).orElseThrow(() -> new PrenotazioneException("Studio non trovato", "STUDIO_NOT_FOUND"));
            
            CatalogoPrestazioni catalogo = catalogoPrestazioniDAO.findById(prestazione.getCatalogoPrestazioniId()).orElseThrow(() -> new PrenotazioneException("Catalogo prestazioni non trovato", "CATALOGO_PRESTAZIONI_NOT_FOUND"));
            Medico medico = medicoDAO.findById(prestazione.getMedicoId()).orElseThrow(() -> new PrenotazioneException("Medico non trovato", "MEDICO_NOT_FOUND"));
            
            dto.setCatalogoPrestazioni(catalogo);
            dto.setMedico(medico);
            dto.setDisponibilita(disponibilita);
            dto.setPrestazione(prestazione);
            dto.setStudio(studio);
            
            return dto;
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore durante il recupero del riepilogo prenotazione: " + e.getMessage(), "RIEPILOGO_ERROR");
        }
    }

    
    public boolean creaPrenotazione(int pazienteId, int disponibilitaId,int idErogazione,double prezzo_pagato,double prezzo_trattenuta,double prezzo_netto,double prezzo_tasse,int idSconto,String metodoPagamento) throws PrenotazioneException
    {
        try (Connection conn = _contex.getConnection()) 
        {
            conn.setAutoCommit(false);
            try 
            {
                Disponibilita d = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));
                                
                if (d.getStato() != Disponibilita.Stato.BLOCCATA)
                    throw new PrenotazioneException("Disponibilità non è bloccata", "DISPONIBILITA_NOT_BLOCKED");

                if (d.getPazienteId() < 0 || d.getPazienteId() != pazienteId) 
                    throw new PrenotazioneException("Tentativo di prenotazione su slot bloccato da un altro utente", "BLOCKED_BY_OTHER_USER");
                
                if (d.getTimestampBlocco() != null && 
                    d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now())) 
                    throw new PrenotazioneException("Tentativo di prenotazione su slot con blocco scaduto", "BLOCCO_SCADUTO");

                Prenotazione p = new Prenotazione();
                p.setPazienteId(pazienteId);
                p.setDisponibilitaId(disponibilitaId);
                p.setErogazionePrestazioneId(idErogazione);
                p.setDataPagamento(LocalDateTime.now());
                p.setStato(Prenotazione.Stato.CONFERMATA);
                
                if(idSconto>0)
                    p.setCodiceScontoId(idSconto);
                
                p.setImportoPagato(prezzo_pagato);
                p.setMetodoPagamento(metodoPagamento);
                p.setRicavoNettoMedicoEuro(prezzo_netto);
                p.setTasseStimateEuro(prezzo_tasse);
                p.setTrattenutaPiattaformaEuro(prezzo_trattenuta);
                
                prenotazioneDAO.insert(p,conn);
                disponibilitaDAO.updateStato(disponibilitaId, Disponibilita.Stato.PRENOTATA, conn);
                conn.commit();
                return true;
            } 
            catch (PrenotazioneException pe) 
            {
                conn.rollback();
                throw pe;
            }
            catch (SQLException e) 
            {
                conn.rollback();
                throw new PrenotazioneException("Errore transazione prenotazione: " + e.getMessage(), "PRENOTAZIONE_TRANSACTION_ERROR");
            }
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore connessione database: " + e.getMessage(), "DB_CONNECTION_ERROR");
        }
    }

    
    public boolean disdiciPrenotazione(int prenotazioneId) throws PrenotazioneException
    {
        try (Connection conn = _contex.getConnection()) 
        {
            conn.setAutoCommit(false);
            try {
                Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));
                if (p.getStato() == Prenotazione.Stato.CANCELLATA)
                    throw new PrenotazioneException("Prenotazione già cancellata", "PRENOTAZIONE_ALREADY_CANCELLED");
                
                prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.CANCELLATA, conn);
                disponibilitaDAO.updateStato(p.getDisponibilitaId(), Disponibilita.Stato.DISPONIBILE, conn);

                conn.commit();
                return true;
            } 
            catch (PrenotazioneException pe) 
            {
                conn.rollback();
                throw pe;
            } catch (SQLException e) 
            {
                conn.rollback();
                throw new PrenotazioneException("Errore durante la disdetta: " + e.getMessage(), "DISDETTA_ERROR");
            }
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore connessione database: " + e.getMessage(), "DB_CONNECTION_ERROR");
        }
    }

    public List<Prenotazione> getPrenotazioniPaziente(int pazienteId, boolean future) throws SQLException 
    {   
        return prenotazioneDAO.findByPaziente(pazienteId);
    }

    public boolean concludiVisita(int prenotazioneId, String note) throws PrenotazioneException
    {
        try 
        {
            prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.COMPLETATA);
            return true;
        } 
        catch (SQLException e) 
        {
            throw new PrenotazioneException("Errore durante la conclusione della visita: " + e.getMessage(), "VISITA_CONCLUSION_ERROR");
        }
    }
}
