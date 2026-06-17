package it.mediclick.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

public class RicercaService
{

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
			return medicoDAO.findCards(categoriaId, citta, query.split("\\s+"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RicercaException("Errore del sistema durante la ricerca dei medici.", "SYS_DATABASE_ERROR");
		}
	}

	public List<ErogazionePrestazione> cercaPrestazioniByMedicoEStudio(int idMedico, int idStudio) throws RicercaException
	{
		try
		{
			List<ErogazionePrestazione> erogazioni = erogazioneDAO.findByMedicoEStudio(idMedico, idStudio);
			for (ErogazionePrestazione ep : erogazioni)
			{
				erogazioneDAO.getCompleto(ep);
			}
			return erogazioni;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero delle prestazioni del medico per lo studio.", "SYS_DATABASE_ERROR");
		}
	}

	public ProfiloMedicoPubblicoDTO dettagliProfiloMedico(int idMedico) throws RicercaException
	{

		ProfiloMedicoPubblicoDTO profiloMedico = new ProfiloMedicoPubblicoDTO();

		List<ErogazionePrestazione> prestazioni = cercaPrestazioniByMedico(idMedico);
		List<Disponibilita> disponibilita = cercaDisponibilitaByMedico(idMedico);

		List<Studio> studi = new ArrayList<Studio>();

		profiloMedico.setDisponibilita(disponibilita);
		profiloMedico.setMedico(getMedicoById(idMedico));
		profiloMedico.setPrestazioni(prestazioni);
		profiloMedico.setStudi(studi);

		try
		{
			for (ErogazionePrestazione ep : prestazioni)
			{
				Studio studioCorrente = studioDAO.findById(ep.getStudioId()).orElseThrow(() -> new RicercaException("Studio medico di riferimento non trovato.", "RICERCA_STUDIO_NON_TROVATO"));
				CatalogoPrestazioni prestazioneCorrente = catalogoDAO.findById(ep.getCatalogoPrestazioniId())
						.orElseThrow(() -> new RicercaException("Prestazione medica non trovata.", "RICERCA_PRESTAZIONE_NON_TROVATA"));

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
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero dei dettagli del profilo medico.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero delle disponibilità del medico.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Disponibilita> cercaDisponibilitaByMedicoeStudio(int IdMedico, int studioId) throws RicercaException
	{
		try
		{
			return disponibilitaDAO.findByMedicoEStudio(IdMedico, studioId);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero delle disponibilità del medico.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero delle prestazioni del medico.", "SYS_DATABASE_ERROR");
		}
	}

	public Medico getMedicoById(int medicoId) throws RicercaException
	{
		try
		{
			return medicoDAO.findById(medicoId).orElseThrow(() -> new RicercaException("Profilo medico non trovato.", "RICERCA_MEDICO_NON_TROVATO"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero del medico.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Medico> getMediciConsigliati() throws RicercaException
	{
		try
		{
			return medicoDAO.findByStato(Medico.StatoVerifica.APPROVATO).stream().limit(5).collect(Collectors.toList());
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero dei medici suggeriti.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero delle categorie.", "SYS_DATABASE_ERROR");
		}
	}

	public Map<String, Object> getMedicoCittaSuggestJson(String query, String citta) throws RicercaException
	{
		String _query = query != null ? query : "";
		String _citta = citta != null ? citta : "";

		try
		{

			List<Map<String, Object>> suggerimentiCitta = medicoDAO.findCittaSuggest(_query, _citta);
			List<Map<String, Object>> suggerimentiMedico = medicoDAO.findMedicoSuggest(_query, _citta);

			List<String> cittaSuggerite = suggerimentiCitta.stream().map(s -> s.get("Citta").toString()).toList();
			List<String> mediciSuggeriti = suggerimentiMedico.stream().map(s -> s.get("cognome").toString() + " " + s.get("nome").toString()).toList();

			Map<String, Object> responseData = new HashMap<String, Object>();

			responseData.put("citta", cittaSuggerite);
			responseData.put("medici", mediciSuggeriti);

			return responseData;

		}
		catch (Exception e)
		{

			e.printStackTrace();
			throw new RicercaException("Errore del sistema nel recupero dei suggerimenti.", "SYS_ERROR");
		}

	}

}
