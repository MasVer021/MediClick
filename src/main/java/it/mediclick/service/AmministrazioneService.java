package it.mediclick.service;

import java.sql.SQLException;
import java.util.List;

import it.mediclick.exception.AmministratoreException;
import it.mediclick.model.DTO.StatistichePiattaformaDTO;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.ImpostazioniSistema;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.model.bean.Utente;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.CertificatoDAO;
import it.mediclick.model.dao.ImpostazioniSistemaDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;

public class AmministrazioneService
{

	private final MedicoDAO medicoDAO;
	private final CatalogoPrestazioniDAO catalogoDAO;
	private final UtenteDAO utenteDAO;
	private final PrenotazioneDAO prenotazioneDAO;
	private final CertificatoDAO certificatoDAO;
	private final ImpostazioniSistemaDAO impostazioniSistemaDAO;
	private final Contex _contex;

	public AmministrazioneService(Contex contex)
	{
		_contex = contex;
		this.medicoDAO = new MedicoDAO(_contex);
		this.catalogoDAO = new CatalogoPrestazioniDAO(_contex);
		this.utenteDAO = new UtenteDAO(_contex);
		this.prenotazioneDAO = new PrenotazioneDAO(_contex);
		this.certificatoDAO = new CertificatoDAO(_contex);
		this.impostazioniSistemaDAO = new ImpostazioniSistemaDAO(_contex);
	}

	public void approvaMedico(int medicoId, boolean isApprovato) throws AmministratoreException
	{
		try
		{
			Medico medico = medicoDAO.findById(medicoId).orElseThrow(() -> new AmministratoreException("Impossibile approvare il medico: account non trovato.", "ADM_MEDICO_NON_TROVATO"));

			if (medico.getStatoVerifica() != Medico.StatoVerifica.IN_ATTESA)
			{
				throw new AmministratoreException("Impossibile approvare il medico: lo stato corrente dell'account non consente l'approvazione.", "ADM_STATO_INVALIDO");
			}
			if (isApprovato)
			{
				List<Certificato> certificatiCaricati = certificatoDAO.findByMedico(medico.getId());

				List<TipoCertificato> certificatiObbligatori = certificatoDAO.tipoCertificatofindAll().stream().filter(c -> c.isObbligatorio()).toList();

				for (TipoCertificato tc : certificatiObbligatori)
				{
					boolean certificatoApprovato = certificatiCaricati.stream().anyMatch(c -> c.getTipoCertificatoId() == tc.getId() && c.getStato() == Certificato.Stato.APPROVATO);

					if (!certificatoApprovato)
					{
						throw new AmministratoreException(String.format("Impossibile approvare il medico: il certificato obbligatorio %s non è ancora stato approvato.", tc.getNome()),
								"ADM_STATO_INVALIDO");
					}
				}

				medicoDAO.updateStatoVerifica(medicoId, Medico.StatoVerifica.APPROVATO);
			}
			else
			{
				medicoDAO.updateStatoVerifica(medicoId, Medico.StatoVerifica.RIFIUTATO);
			}
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore del sistema durante l'aggiornamento dello stato del medico.", "SYS_DATABASE_ERROR");
		}

	}

	public void aggiungiAlCatalogo(CatalogoPrestazioni cp) throws AmministratoreException
	{
		try
		{
			catalogoDAO.insert(cp);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore di comunicazione con il database del catalogo.", "SYS_DATABASE_ERROR");
		}
	}

	public void aggiornaStatoPrestazione(int catalogoId, CatalogoPrestazioni.Stato stato) throws AmministratoreException
	{
		try
		{
			catalogoDAO.updateStato(catalogoId, stato);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore durante la modifica dello stato della prestazione.", "SYS_DATABASE_ERROR");
		}
	}

	public void bloccaUtente(int utenteId, boolean bloccato) throws AmministratoreException
	{
		try
		{
			utenteDAO.setAccountAttivo(utenteId, !bloccato);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore durante il blocco dell'utente con ID " + utenteId + ": " + e.getMessage(), "AMMINISTRATORE_BLOCCA_UTENTE_ERROR");
		}
	}

	public List<Medico> getMediciInAttesa() throws AmministratoreException
	{
		try
		{
			return medicoDAO.findByStato(Medico.StatoVerifica.IN_ATTESA);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore durante il recupero dei medici in attesa: " + e.getMessage(), "AMMINISTRATORE_GET_MEDICI_IN_ATTESA_ERROR");
		}
	}

	public StatistichePiattaformaDTO getStatistichePiattaforma() throws AmministratoreException
	{
		try
		{
			int totaleMedici = medicoDAO.countAll();
			int mediciDaApprovare = medicoDAO.countByStatoVerifica(Medico.StatoVerifica.IN_ATTESA);
			double guadagniPiattaforma = prenotazioneDAO.getGuadagniTotaliPiattaforma();

			return new StatistichePiattaformaDTO(totaleMedici, mediciDaApprovare, guadagniPiattaforma);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nel recupero delle statistiche complessive: " + e.getMessage(), "STATS_PIATTAFORMA_ERROR");
		}
	}

	public void gestisciCertificato(int certificatoId, boolean approvato, int approvatoreId) throws AmministratoreException
	{
		try
		{
			Certificato.Stato nuovoStato = approvato ? Certificato.Stato.APPROVATO : Certificato.Stato.RIFIUTATO;

			certificatoDAO.updateStato(certificatoId, nuovoStato, approvatoreId);

		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nella gestione dello stato del certificato: " + e.getMessage(), "AMMINISTRATORE_GESTISCI_CERT_ERROR");
		}
	}

	public void aggiungiCategoria(Categoria c) throws AmministratoreException
	{
		try
		{
			catalogoDAO.insertCategoria(c);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore durante l'aggiunta della categoria: " + e.getMessage(), "AMMINISTRATORE_AGGIUNGI_CATEGORIA_ERROR");
		}
	}

	public List<Categoria> findAllCategorie() throws AmministratoreException
	{
		try
		{
			return catalogoDAO.findAllCategorie();
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nel recupero delle categorie: " + e.getMessage(), "AMMINISTRATORE_GET_CATEGORIE_ERROR");
		}
	}

	public List<CatalogoPrestazioni> findAllPrestazioni() throws AmministratoreException
	{
		try
		{
			List<CatalogoPrestazioni> prestazioni = catalogoDAO.findAll();
			for (CatalogoPrestazioni cp : prestazioni)
			{
				catalogoDAO.getCompleto(cp);
			}
			return prestazioni;
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nel recupero delle prestazioni: " + e.getMessage(), "AMMINISTRATORE_GET_PRESTAZIONI_ERROR");
		}
	}

	public List<Medico> getTuttiIMedici() throws AmministratoreException
	{
		try
		{
			List<Medico> medici = medicoDAO.findAll();
			for (Medico m : medici)
			{
				Utente u = utenteDAO.findById(m.getId()).orElse(null);
				m.setUtente(u);
			}
			return medici;
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nel recupero dei medici: " + e.getMessage(), "AMMINISTRATORE_GET_MEDICI_ERROR");
		}
	}

	public List<ImpostazioniSistema> getTutteLeImpostazioni() throws AmministratoreException
	{
		try
		{
			List<ImpostazioniSistema> settings = impostazioniSistemaDAO.findAll();
			for (ImpostazioniSistema s : settings)
			{
				impostazioniSistemaDAO.getCompleto(s);
			}
			return settings;
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nel recupero delle impostazioni: " + e.getMessage(), "AMMINISTRATORE_GET_IMPOSTAZIONI_ERROR");
		}
	}

	public void aggiornaImpostazione(String chiave, String valore, int adminId) throws AmministratoreException
	{
		try
		{
			impostazioniSistemaDAO.insert(chiave, valore, adminId);
		}
		catch (SQLException e)
		{
			throw new AmministratoreException("Errore nell'aggiornamento dell'impostazione: " + e.getMessage(), "AMMINISTRATORE_UPDATE_IMPOSTAZIONE_ERROR");
		}
	}

}
