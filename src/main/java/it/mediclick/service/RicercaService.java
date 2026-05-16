package it.mediclick.service;

import it.mediclick.exception.RicercaException;
import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.DTO.ProfiloMedicoPubblicoDTO;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.StudioDAO;
import it.mediclick.util.Contex;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaService {

    private final MedicoDAO medicoDAO;
    private final DisponibilitaDAO disponibilitaDAO;
    private final ErogazionePrestazioneDAO erogazioneDAO;
    private final StudioDAO studioDAO;
    private final CatalogoPrestazioniDAO catalogoDAO;
    private final Contex _contex;

    public RicercaService(Contex contex) 
    {
        this._contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
        this.disponibilitaDAO = new DisponibilitaDAO(_contex);
        this.erogazioneDAO = new ErogazionePrestazioneDAO(_contex);
        this.studioDAO = new StudioDAO(_contex);
        this.catalogoDAO = new CatalogoPrestazioniDAO(_contex);
    }

    
    public List<MedicoCardDTO> cercaMediciCards(String query, Integer categoriaId, String citta) throws RicercaException 
    {
        try
        {
            return medicoDAO.findCards(query, categoriaId, citta);
        } 
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante la ricerca dei medici: " + e.getMessage(), "RICERCA_MEDICI_CARDS_ERROR");
        }
    }
    
    public ProfiloMedicoPubblicoDTO dettagliProfiloMedico(int idMedico) throws RicercaException
    {
    	   	
    	ProfiloMedicoPubblicoDTO profiloMedico = new ProfiloMedicoPubblicoDTO();
    	
    	List<ErogazionePrestazione> prestazioni  = cercaPrestazioniByMedico(idMedico);
    	List<Disponibilita> disponibilita = cercaDisponibilitaByMedico(idMedico);
    	
    	List<Studio> studi = new ArrayList<Studio>();
    	
    	profiloMedico.setDisponibilita(disponibilita);
    	profiloMedico.setMedico(getMedicoById(idMedico));
    	profiloMedico.setPrestazioni(prestazioni);
        profiloMedico.setStudi(studi);
    	
        try
        {
	    	for(ErogazionePrestazione ep : prestazioni)
	    	{
	    		Studio studioCorrente = studioDAO.findById(ep.getStudioId()).orElseThrow(() -> new RicercaException("Studio non trovato con ID: " + ep.getStudioId(), "RICERCA_DETTAGLI_PROFILO_MEDICO_STUDIO_NOT_FOUND"));
	            CatalogoPrestazioni prestazioneCorrente = catalogoDAO.findById(ep.getCatalogoPrestazioniId()).orElseThrow(() -> new RicercaException("Prestazione non trovata con ID: " + ep.getCatalogoPrestazioniId(), "RICERCA_DETTAGLI_PROFILO_MEDICO_PRESTAZIONE_NOT_FOUND"));
	    		
	    		
	    		ep.setStudio(studioCorrente);
	    	    ep.setCatalogoPrestazioni(prestazioneCorrente);
	    	    
	    	    boolean giaPresente = studi.stream().anyMatch(s -> s.getId() == studioCorrente.getId());
	    	    
	    	    if (!giaPresente) 
	    	    {
	    	        studi.add(studioCorrente);
	    	    }
	    	}
        }
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante il recupero dei dettagli del profilo medico con ID " + idMedico + ": " + e.getMessage(), "RICERCA_DETTAGLI_PROFILO_MEDICO_ERROR");
        }
    	
    	profiloMedico.setStudi(studi);
    	
    	return profiloMedico;
    }
    
    public List<Disponibilita> cercaDisponibilitaByMedico(int IdMedico) throws RicercaException 
    {
        try 
        {
            return disponibilitaDAO.findDisponibili(IdMedico);
        } 
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante la ricerca delle disponibilità per il medico con ID " + IdMedico + ": " + e.getMessage(), "RICERCA_DISPONIBILITA_BY_MEDICO_ERROR");
        }
    }
    
    public List<ErogazionePrestazione> cercaPrestazioniByMedico(int IdMedico) throws RicercaException 
    {
        try 
        {
            return erogazioneDAO.findByMedico(IdMedico);
        } 
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante la ricerca delle prestazioni per il medico con ID " + IdMedico + ": " + e.getMessage(), "RICERCA_PRESTAZIONI_BY_MEDICO_ERROR");
        }
    } 
    
 
    public Medico getMedicoById(int medicoId) throws RicercaException 
    {
        try
        {
            return medicoDAO.findById(medicoId).orElseThrow(() -> new RicercaException("Medico non trovato con ID: " + medicoId, "RICERCA_GET_MEDICO_BY_ID_NOT_FOUND"));
        }
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante il recupero del medico con ID " + medicoId + ": " + e.getMessage(), "RICERCA_GET_MEDICO_BY_ID_ERROR");
        }
    }

    public List<Medico> getMediciConsigliati() throws RicercaException 
    {
        try 
        {
            return medicoDAO.findByStato(Medico.StatoVerifica.APPROVATO).stream()
                    .limit(5)
                    .collect(Collectors.toList());
        } 
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante il recupero dei medici consigliati: " + e.getMessage(), "RICERCA_GET_MEDICI_CONSIGLIATI_ERROR");
        }
    } 
    
    public List<Categoria> getCategorie() throws RicercaException 
    {
        try 
        {
            return catalogoDAO.findAllCategorie();
        } 
        catch (SQLException e) 
        {
            throw new RicercaException("Errore durante il recupero delle categorie: " + e.getMessage(), "RICERCA_GET_CATEGORIE_ERROR");
        }
    }
}
