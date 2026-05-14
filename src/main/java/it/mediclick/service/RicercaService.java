package it.mediclick.service;

import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.DTO.ProfiloMedicoPubblicoDTO;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.RecensioneDAO;
import it.mediclick.model.dao.StudioDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

public class RicercaService {

    private MedicoDAO medicoDAO;
    private DisponibilitaDAO disponibilitaDAO;
    private ErogazionePrestazioneDAO erogazioneDAO;
    private StudioDAO studioDAO;
    private CatalogoPrestazioniDAO catalogoDAO;
    private Contex _contex;

    public RicercaService(Contex contex) {
        this._contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
        this.disponibilitaDAO = new DisponibilitaDAO(_contex);
        this.erogazioneDAO = new ErogazionePrestazioneDAO(_contex);
        this.studioDAO = new StudioDAO(_contex);
        this.catalogoDAO = new CatalogoPrestazioniDAO(_contex);
    }

    
    public List<MedicoCardDTO> cercaMediciCards(String query, Integer categoriaId, String citta) throws SQLException 
    {
        return medicoDAO.findCards(query, categoriaId, citta);
    }
    
    public ProfiloMedicoPubblicoDTO dettagliProfiloMedico(int idMedico) throws SQLException
    {
    	   	
    	ProfiloMedicoPubblicoDTO profiloMedico = new ProfiloMedicoPubblicoDTO();
    	
    	List<ErogazionePrestazione> prestazioni  = cercaPrestazioniByMedico(idMedico);
    	
    	for(ErogazionePrestazione ep : prestazioni)
    	{
    		
    	}
    	
    	List<Disponibilita> disponibilita = cercaDisponibilitaByMedico(idMedico);
    	
    	for(Disponibilita d : disponibilita)
    	{
    		
    	}
    	
    	
    
    	
    	
    	
    	List<Studio> studi = new ArrayList<Studio>();
    	
    	profiloMedico.setDisponibilita(cercaDisponibilitaByMedico(idMedico));
    	profiloMedico.setMedico(getMedicoById(idMedico));
    	profiloMedico.setPrestazioni(prestazioni);
    	
    	for(ErogazionePrestazione ep : prestazioni)
    	{
    		Studio studioCorrente = studioDAO.findById(ep.getStudioId());
    		
    		
    		
    		
    		
    		ep.setStudio(studioCorrente);
    	    ep.setCatalogoPrestazioni(catalogoDAO.findById(ep.getCatalogoPrestazioniId()));
    	    
    	  
    	    
    	    boolean giaPresente = studi.stream().anyMatch(s -> s.getId() == studioCorrente.getId());
    	    
    	    if (!giaPresente) 
    	    {
    	        studi.add(studioCorrente);
    	    }
    	}
    	
    	profiloMedico.setStudi(studi);
    	
    	return profiloMedico;
    }
    
    public List<Disponibilita> cercaDisponibilitaByMedico(int IdMedico) throws SQLException 
    {
        return disponibilitaDAO.findByMedico(IdMedico);
    }
    
    public List<ErogazionePrestazione> cercaPrestazioniByMedico(int IdMedico) throws SQLException 
    {
        return erogazioneDAO.findByMedico(IdMedico);
    } 
    
 
    public Medico getMedicoById(int medicoId) throws SQLException 
    {
        return medicoDAO.findById(medicoId);
    }

    public List<Medico> getMediciConsigliati() throws SQLException 
    {
        return medicoDAO.findByStato(Medico.StatoVerifica.APPROVATO).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Categoria> getCategorie() throws SQLException 
    {
        CatalogoPrestazioniDAO catalogoDAO = new CatalogoPrestazioniDAO(_contex);
        return catalogoDAO.findAllCategorie();
    }
}
