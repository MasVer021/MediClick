package it.mediclick.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

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

public class PrenotazioneService {

    private PrenotazioneDAO prenotazioneDAO;
    private DisponibilitaDAO disponibilitaDAO;
    private ErogazionePrestazioneDAO erogazionePrestazioneDAO;
    private MedicoDAO medicoDAO;
    private StudioDAO studioDAO;
    private CatalogoPrestazioniDAO catalogoPrestazioniDAO;
    private CodiceScontoDAO codiceScontoDAO;
    private ImpostazioniSistemaDAO impostazioniSistemaDAO;
    private Contex _contex;

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
       
    public int getTrattenuta()
    {
    	try 
    	{
			return impostazioniSistemaDAO.getIDImpByKey("COMMISSIONE_PIATTAFORMA_PCT");
		} 
    	catch (SQLException e) 
    	{
    		e.printStackTrace();
			return -1;	
    	}
    }
    
    
    public boolean isValid(CodiceSconto sconto)
    {
    	try 
    	{
			return codiceScontoDAO.isValid(sconto);
		} 
    	catch (SQLException e) 
    	{
			
			e.printStackTrace();
			return false;
		}
    }
    
    public CodiceSconto findSconto(String codice)
    {
    	try 
    	{
			return codiceScontoDAO.findByCodice(codice);
		} 
    	catch (SQLException e) 
    	{
			System.err.println("Errore validazione codice sconto: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
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

    public RiepilogoPrenotazioneDTO getRiepilogoPrenotazione(int idStudio, int idPrestazione, int idDisponibilita) 
    {
        try {
            RiepilogoPrenotazioneDTO dto = new RiepilogoPrenotazioneDTO();
            
            Disponibilita disponibilita = disponibilitaDAO.findById(idDisponibilita);
            ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idPrestazione);
            Studio studio = studioDAO.findById(idStudio);
            
            if (prestazione != null) {
                CatalogoPrestazioni catalogo = catalogoPrestazioniDAO.findById(prestazione.getCatalogoPrestazioniId());
                dto.setCatalogoPrestazioni(catalogo);
                
                Medico medico = medicoDAO.findById(prestazione.getMedicoId());
                dto.setMedico(medico);
            }
            
            dto.setDisponibilita(disponibilita);
            dto.setPrestazione(prestazione);
            dto.setStudio(studio);
            
            return dto;
        } catch (SQLException e) {
            System.err.println("Errore durante il recupero del riepilogo prenotazione: " + e.getMessage());
            return null;
        }
    }

    
    public boolean creaPrenotazione(int pazienteId, int disponibilitaId,int idErogazione,double prezzo_pagato,double prezzo_trattenuta,double prezzo_netto,double prezzo_tasse,int idSconto,String metodoPagamento) 
    {
        System.out.println("Ciao");
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
