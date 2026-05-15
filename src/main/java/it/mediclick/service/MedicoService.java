package it.mediclick.service;

import it.mediclick.exception.MedicoException;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MedicoService 
{

    private DisponibilitaDAO disponibilitaDAO;
    private ErogazionePrestazioneDAO erogazioneDAO;
    private Contex _contex;

    public MedicoService(Contex contex) 
    {
        this._contex = contex;
        this.disponibilitaDAO = new DisponibilitaDAO(_contex);
        this.erogazioneDAO = new ErogazionePrestazioneDAO(_contex);
    }
      
    public void configuraOrario(int medicoId, LocalDateTime dataIn, LocalDateTime dataOut, Studio s) throws MedicoException
    {
    	if (dataOut.isBefore(dataIn))
        {
            throw new MedicoException("La data di fine deve essere successiva alla data di inizio.", "MEDICO_CONFIGURA_ORARIO_INVALID_DATES");
        }
    		
    	LocalDateTime tempo = dataIn;
    	List<Disponibilita> slot = new ArrayList<Disponibilita>();
    	
    	while(tempo.isBefore(dataOut))
    	{
    		Disponibilita d = new Disponibilita();
            d.setMedicoId(medicoId);
            d.setStudioId(s.getId());
            d.setDataOraInizio(tempo);
            d.setDataOraFine(tempo.plusMinutes(30));
            d.setStato(Disponibilita.Stato.DISPONIBILE);
            slot.add(d);
    		tempo = tempo.plusMinutes(30);
    	}
    	
    	configuraOrario(medicoId, slot);
    }
    
    public void configuraOrario(int medicoId, List<Disponibilita> slot) throws MedicoException 
    {
        try 
        {
            slot.stream().forEach(d->d.setMedicoId(medicoId));   
            disponibilitaDAO.insertMultiDisponibilita(slot);  
        } 
        catch (SQLException e) 
        {
            throw new MedicoException("Errore durante la configurazione dell'orario per il medico con ID " + medicoId + ": " + e.getMessage(), "MEDICO_CONFIGURA_ORARIO_ERROR");
        }
    }

    
    public void associaPrestazione(int medicoId, int catalogoId, double prezzo) throws MedicoException 
    {
        ErogazionePrestazione ep = new ErogazionePrestazione();
        ep.setMedicoId(medicoId);
        ep.setCatalogoPrestazioniId(catalogoId);
        ep.setPrezzoLordoListino(prezzo);
        ep.setStato(ErogazionePrestazione.Stato.ATTIVA);
        
        try 
        {
            erogazioneDAO.insert(ep);
        } 
        catch (SQLException e) 
        {
            throw new MedicoException("Errore durante l'associazione della prestazione con ID " + catalogoId + " al medico con ID " + medicoId + ": " + e.getMessage(), "MEDICO_ASSOCIA_PRESTAZIONE_ERROR");
            
        }
      
    }

  
    public void rimuoviPrestazione(int erogazioneId) throws MedicoException 
    {
        try 
        {
            erogazioneDAO.updateStato(erogazioneId, ErogazionePrestazione.Stato.SOSPESA);
        } 
        catch (SQLException e) 
        {
            throw new MedicoException("Errore durante la rimozione dell'associazione della prestazione con ID " + erogazioneId + ": " + e.getMessage(), "MEDICO_RIMUOVI_PRESTAZIONE_ERROR");
        }
        
    }

    public List<ErogazionePrestazione> getMiePrestazioni(int medicoId) throws MedicoException 
    {
        try 
        {
            return erogazioneDAO.findByMedico(medicoId);
        } 
        catch (SQLException e) 
        {
            throw new MedicoException("Errore durante il recupero delle prestazioni del medico con ID " + medicoId + ": " + e.getMessage(), "MEDICO_GET_MIE_PRESTAZIONI_ERROR");
        }
    }

    public List<Disponibilita> getAgenda(int medicoId, LocalDateTime start, LocalDateTime end) throws MedicoException 
    {
        try
        {
            List<Disponibilita> dis = disponibilitaDAO.findDisponibili(medicoId);
            if(end.isBefore(start))
                return dis;
            
            return dis.stream().filter(d-> d.getDataOraInizio().isAfter(start) && d.getDataOraFine().isBefore(end)).toList();
        }
        catch (SQLException e) 
        {
            throw new MedicoException("Errore durante il recupero dell'agenda del medico con ID " + medicoId + ": " + e.getMessage(), "MEDICO_GET_AGENDA_ERROR");
        }
    	
    }
}
