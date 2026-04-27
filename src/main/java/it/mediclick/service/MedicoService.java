package it.mediclick.service;

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

public class MedicoService {

    private DisponibilitaDAO disponibilitaDAO;
    private ErogazionePrestazioneDAO erogazioneDAO;
    private Contex _contex;

    public MedicoService(Contex contex) 
    {
        this._contex = contex;
        this.disponibilitaDAO = new DisponibilitaDAO(_contex);
        this.erogazioneDAO = new ErogazionePrestazioneDAO(_contex);
    }
      
    public void configuraOrario(int medicoId, LocalDateTime dataIn, LocalDateTime dataOut, Studio s) throws SQLException 
    {
    	if (dataOut.isBefore(dataIn))
    		return ;
    	
    	LocalDateTime tempo = dataIn;
    	List<Disponibilita> slot = new ArrayList<Disponibilita>();
    	
    	while(tempo.isBefore(dataOut))
    	{
    		slot.add(new Disponibilita(-1,medicoId,s.getId(),tempo,tempo.plusMinutes(30),Disponibilita.Stato.DISPONIBILE,null,null));
    		tempo = tempo.plusMinutes(30);
    	}
    	
    	configuraOrario(medicoId, slot);
    	
    }
    
    public void configuraOrario(int medicoId, List<Disponibilita> slot) throws SQLException 
    {
        for (Disponibilita d : slot) 
        {
            d.setMedicoId(medicoId);
            disponibilitaDAO.insert(d);
        }
    }

    
    public void associaPrestazione(int medicoId, int catalogoId, double prezzo) throws SQLException 
    {
        ErogazionePrestazione ep = new ErogazionePrestazione();
        ep.setMedicoId(medicoId);
        ep.setCatalogoPrestazioniId(catalogoId);
        ep.setPrezzoLordoListino(prezzo);
        ep.setStato(ErogazionePrestazione.Stato.ATTIVA);
        
        erogazioneDAO.insert(ep);
    }

  
    public void rimuoviPrestazione(int erogazioneId) throws SQLException 
    {
        erogazioneDAO.updateStato(erogazioneId, ErogazionePrestazione.Stato.SOSPESA);
    }

    public List<ErogazionePrestazione> getMiePrestazioni(int medicoId) throws SQLException 
    {
        return erogazioneDAO.findByMedico(medicoId);
    }

    public List<Disponibilita> getAgenda(int medicoId, LocalDateTime start, LocalDateTime end) throws SQLException 
    {
    	List<Disponibilita> dis = disponibilitaDAO.findByMedico(medicoId);
    	if(end.isBefore(start))
    		return dis;
    	
    	return dis.stream().filter(d-> d.getDataOraInizio().isAfter(start) && d.getDataOraFine().isBefore(end)).toList();
    }
}
