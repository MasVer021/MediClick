package it.mediclick.service;

import it.mediclick.exception.PazienteException;
import it.mediclick.exception.PrenotazioneException;
import it.mediclick.exception.RecensioneException;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.RecensioneDAO;
import it.mediclick.util.Contex;
import java.sql.SQLException;
import java.util.List;

public class RecensioneService 
{

    private RecensioneDAO recensioneDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private Contex _contex;

    public RecensioneService(Contex contex) 
    {
        this._contex = contex;
        this.recensioneDAO = new RecensioneDAO(_contex);
        this.prenotazioneDAO = new PrenotazioneDAO(_contex);
    }
    
    
    public Prenotazione getPrenotazionePerRecensione(int prenotazioneId, int pazienteId) throws RecensioneException 
    {
    	try
    	{
			Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(()-> new RecensioneException("Prenotazione con id: "+prenotazioneId+ " non trovata", "PRENOTAZIONE_NOT_FOUND"));
			
			if (p.getPazienteId() != pazienteId) 
	        {
	            throw new RecensioneException("Non hai i permessi per recensire.", "AUTH_ERROR");
	        }
			
	        if (p.getStato() != Prenotazione.Stato.COMPLETATA && p.getStato() != Prenotazione.Stato.CONFERMATA) 
	        {
	            throw new RecensioneException("Stato non valido.", "STATO_ERROR");
	        }
	        
	        if (p.isFutura())
	        {
	            throw new RecensioneException("Visita non ancora avvenuta.", "DATA_ERROR");
	        }
	        
	        ErogazionePrestazioneDAO erogazionePrestazioneDAO = new ErogazionePrestazioneDAO(_contex);
	        
	        
	        
	        prenotazioneDAO.getCompleto(p);
	        erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());
	        
			return p;  
		} 
    	catch (SQLException e)
    	{
    		throw new RecensioneException("Errore durante il recupero della prenotazione: " + e.getMessage(), "PRENOTAZIONE_ERROR");
		}
    }

    public boolean lasciaRecensione(int prenotazioneId, int voto, String commento) throws RecensioneException 
    {
        try
        {
            if (voto < 1 || voto > 5) 
            {
                throw new RecensioneException("Voto deve essere tra 1 e 5", "RECENSIONE_VOTO_INVALIDO");
            }


            Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new RecensioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));
        
            Recensione r = new Recensione();
            r.setPrenotazioneId(prenotazioneId);
            r.setVoto(voto);
            r.setCommento(commento);
            r.setVisible(true);
            
            recensioneDAO.insert(r);
            return true;
        } 
        catch (SQLException e) 
        {
            throw new RecensioneException("Errore durante l'inserimento della recensione: " + e.getMessage(), "RECENSIONE_INSERT_ERROR");
        }
        
    }
    
    
    public Recensione findByIdPrenotazione(int prenotazionId) throws RecensioneException 
    {
    	 try 
         {
             return recensioneDAO.findByPrenotazione(prenotazionId).orElse(null);
         } 
         catch (SQLException e) 
         {
             throw new RecensioneException("Errore durante il recupero della recensione per la prenotazione con ID " + prenotazionId + ": " + e.getMessage(), "RECENSIONE_GET_BY_MEDICO_ERROR");
         }
    }
    
    public List<Recensione> getRecensioniMedico(int medicoId) throws RecensioneException 
    {
        try 
        {
            return recensioneDAO.findByMedico(medicoId);
        } 
        catch (SQLException e) 
        {
            throw new RecensioneException("Errore durante il recupero delle recensioni per il medico con ID " + medicoId + ": " + e.getMessage(), "RECENSIONE_GET_BY_MEDICO_ERROR");
        }
    }

    public void moderaRecensione(int recensioneId, boolean visibile) throws RecensioneException 
    {
        try 
        {
            recensioneDAO.setVisibile(recensioneId, visibile);
        } 
        catch (SQLException e) 
        {
            throw new RecensioneException("Errore durante la moderazione della recensione con ID " + recensioneId + ": " + e.getMessage(), "RECENSIONE_MODERA_ERROR");
        }
    }
}
