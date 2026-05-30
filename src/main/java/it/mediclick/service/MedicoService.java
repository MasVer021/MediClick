package it.mediclick.service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.mediclick.exception.MedicoException;
import it.mediclick.model.DTO.SlotAgendaDTO;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.CertificatoDAO;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.StudioDAO;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

public class MedicoService
{
	private DisponibilitaDAO disponibilitaDAO;
	private ErogazionePrestazioneDAO erogazioneDAO;
	private PrenotazioneDAO prenotazioneDAO;
	private CatalogoPrestazioniDAO catalogoPrestazioniDAO;
	private MedicoDAO medicoDAO;
	private StudioDAO studioDAO;
	private CertificatoDAO certificatoDAO;
	private Contex _contex;

	public MedicoService(Contex contex)
	{
		this._contex = contex;
		this.disponibilitaDAO = new DisponibilitaDAO(_contex);
		this.erogazioneDAO = new ErogazionePrestazioneDAO(_contex);
		this.prenotazioneDAO = new PrenotazioneDAO(_contex);
		this.medicoDAO = new MedicoDAO(_contex);
		this.studioDAO = new StudioDAO(_contex);
		this.catalogoPrestazioniDAO = new CatalogoPrestazioniDAO(_contex);
		this.certificatoDAO = new CertificatoDAO(_contex);
	}

	public void configuraOrario(int medicoId, LocalDateTime dataIn, LocalDateTime dataOut, Studio s) throws MedicoException
	{
		if (dataOut.isBefore(dataIn))
		{
			throw new MedicoException("La data di fine deve essere successiva alla data di inizio.", "MEDICO_CONFIGURA_ORARIO_INVALID_DATES");
		}

		try
		{
			List<Disponibilita> slotEsistenti = disponibilitaDAO.findDisponibiliFilterDate(medicoId, dataIn, dataOut);
			if (!slotEsistenti.isEmpty())
			{
				throw new MedicoException("Attenzione: hai già inserito delle disponibilità in questa fascia oraria. Controlla l'agenda.", "MEDICO_CONFIGURA_ORARIO_ALREADY_EXISTS");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel controllo delle disponibilità.", "SYS_DATABASE_ERROR");
		}

		LocalDateTime tempo = dataIn;
		List<Disponibilita> slot = new ArrayList<Disponibilita>();

		while (tempo.isBefore(dataOut))
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
			slot.stream().forEach(d -> d.setMedicoId(medicoId));
			disponibilitaDAO.insertMultiDisponibilita(slot);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante la configurazione dell'orario.", "SYS_DATABASE_ERROR");
		}
	}

	public void associaPrestazione(int medicoId, int catalogoId, int studioId, double prezzo, int durata) throws MedicoException
	{
		ErogazionePrestazione ep = new ErogazionePrestazione();
		ep.setMedicoId(medicoId);
		ep.setCatalogoPrestazioniId(catalogoId);
		ep.setStudioId(studioId);
		ep.setPrezzoLordoListino(prezzo);
		ep.setStato(ErogazionePrestazione.Stato.ATTIVA);
		ep.setDurata(durata);

		try
		{
			erogazioneDAO.insert(ep);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante l'associazione della prestazione.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante la rimozione della prestazione.", "SYS_DATABASE_ERROR");
		}

	}

	public void AttivaPrestazione(int erogazioneId) throws MedicoException
	{
		try
		{
			erogazioneDAO.updateStato(erogazioneId, ErogazionePrestazione.Stato.ATTIVA);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante la rimozione della prestazione.", "SYS_DATABASE_ERROR");
		}

	}

	public List<ErogazionePrestazione> getMiePrestazioni(int medicoId) throws MedicoException
	{
		try
		{
			List<ErogazionePrestazione> lista = erogazioneDAO.findByMedico(medicoId);

			for (ErogazionePrestazione ep : lista)
			{
				erogazioneDAO.getCompleto(ep);
			}

			return lista;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero delle tue prestazioni.", "SYS_DATABASE_ERROR");
		}
	}

	public List<CatalogoPrestazioni> findAllPrestazioni() throws MedicoException
	{
		try
		{
			return catalogoPrestazioniDAO.findAll();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero del catalogo delle prestazioni.", "SYS_DATABASE_ERROR");
		}
	}

	public void aggiornaProfilo(int medicoId, String cognome, String nome, String bio, String pIva, int regimeFiscaleId, byte[] fotoBytes) throws MedicoException
	{
		try
		{

			Medico m = medicoDAO.findById(medicoId).orElseThrow(() -> new MedicoException("Profilo medico non trovato.", "MEDICO_NON_TROVATO"));

			if (cognome != null)
			{
				cognome = ValidationUtils.parseString(cognome, "cognome");
				m.setCognome(cognome);
			}

			if (nome != null)
			{
				nome = ValidationUtils.parseString(nome, "nome");
				m.setNome(nome);
			}

			if (bio != null)
			{
				bio = ValidationUtils.parseString(bio, "nome");
				m.setBio(bio);
			}

			if (pIva != null && !pIva.trim().isEmpty())
			{
				pIva = ValidationUtils.parsePIva(pIva);
				m.setpIva(pIva);
			}

			if (fotoBytes != null)
			{
				fotoBytes = ValidationUtils.parseByteArray(fotoBytes, "Foto Profilo");
				m.setFotoprofilo(fotoBytes);
			}

			medicoDAO.update(m);

			if (regimeFiscaleId > 0)
			{
				medicoDAO.updateRegimeFiscale(medicoId, regimeFiscaleId);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante l'aggiornamento del profilo.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Disponibilita> getAgenda(int medicoId, LocalDateTime start, LocalDateTime end) throws MedicoException
	{
		try
		{
			List<Disponibilita> dis = disponibilitaDAO.findDisponibili(medicoId);
			if (end.isBefore(start))
				return dis;

			return dis.stream().filter(d -> d.getDataOraInizio().isAfter(start) && d.getDataOraFine().isBefore(end)).toList();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dell'agenda.", "SYS_DATABASE_ERROR");
		}

	}

	public List<SlotAgendaDTO> getAgendaGiornaliera(int medicoId, LocalDate data) throws MedicoException
	{
		try
		{
			LocalDateTime dataInizio = data.atStartOfDay();
			LocalDateTime dataFine = data.plusDays(1).atStartOfDay();

			List<Disponibilita> slotDelGiorno = disponibilitaDAO.findDisponibiliFilterDate(medicoId, dataInizio, dataFine);

			List<Prenotazione> prenotazioni = prenotazioneDAO.findByMedico(medicoId, dataInizio, dataFine);

			Map<Integer, Prenotazione> mappaPrenotazioni = prenotazioni.stream().filter(p -> p.getDisponibilitaId() > 0)
					.collect(Collectors.toMap(Prenotazione::getDisponibilitaId, p -> p, (p1, p2) -> p1));

			List<SlotAgendaDTO> agenda = new ArrayList<>();

			for (Disponibilita d : slotDelGiorno)
			{
				SlotAgendaDTO dto = new SlotAgendaDTO();
				dto.setDisponibilitaId(d.getId());
				dto.setDataOraInizio(d.getDataOraInizio());
				dto.setDataOraFine(d.getDataOraFine());
				dto.setStatoSlot(d.getStato() != null ? d.getStato().getLabel() : null);

				Prenotazione p = mappaPrenotazioni.get(d.getId());

				if (p != null && (d.getStato() == Disponibilita.Stato.PRENOTATA || d.getStato() == Disponibilita.Stato.COMPLETATA))
				{

					prenotazioneDAO.getCompleto(p);
					erogazioneDAO.getCompleto(p.getErogazionePrestazione());

					dto.setPrenotazioneId(p.getId());
					dto.setStatoPrenotazione(p.getStato() != null ? p.getStato().getLabel() : null);

					if (p.getPaziente() != null)
					{

						dto.setNomePaziente(p.getPaziente().getNome());
						dto.setCognomePaziente(p.getPaziente().getCognome());
						dto.setTelefonoPaziente(p.getPaziente().getTelefono());
					}

					dto.setNomePrestazione(p.getErogazionePrestazione().getCatalogoPrestazioni().getNome());
				}

				agenda.add(dto);
			}

			agenda.sort(Comparator.comparing(SlotAgendaDTO::getDataOraInizio));

			return agenda;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nella generazione dell'agenda giornaliera.", "SYS_DATABASE_ERROR");
		}
	}

	public Medico findById(int medicoId) throws MedicoException
	{
		try
		{
			return medicoDAO.findById(medicoId).orElseThrow(() -> new MedicoException("Profilo medico non trovato.", "MEDICO_NON_TROVATO"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero del profilo medico.", "SYS_DATABASE_ERROR");
		}
	}

	public Studio findStudioById(int studioId) throws MedicoException
	{
		try
		{
			return studioDAO.findById(studioId).orElseThrow(() -> new MedicoException("Studio medico non trovato.", "STUDIO_NON_TROVATO"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dello studio.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Studio> findAllStudio() throws MedicoException
	{
		try
		{
			return studioDAO.findAll();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dell'elenco degli studi.", "SYS_DATABASE_ERROR");
		}
	}

	public void rimuoviDisponibilita(int disponibilitId, int medicoId) throws MedicoException
	{
		try
		{
			Disponibilita d = disponibilitaDAO.findById(disponibilitId).orElseThrow(() -> new MedicoException("Slot di disponibilità non trovato.", "DISPONIBILITA_NON_TROVATA"));

			if (d.getMedicoId() != medicoId)
			{
				throw new MedicoException("Non hai i permessi per rimuovere questo slot di disponibilità.", "AUTH_ACCESSO_NEGATO");
			}
			disponibilitaDAO.deleteLogic(disponibilitId);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante la rimozione dello slot di disponibilità.", "SYS_DATABASE_ERROR");
		}
	}

	public List<RegimeFiscale> findAllRegimiFiscali() throws MedicoException
	{
		try
		{
			return medicoDAO.findAllRegimeFiscale();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dei regimi fiscali.", "SYS_DATABASE_ERROR");
		}
	}

	public List<TipoCertificato> findAllTipoCertificato() throws MedicoException
	{
		try
		{
			return certificatoDAO.tipoCertificatofindAll();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dei tipi di certificato.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Certificato> findAllCertificatiByMedicoId(int medicoId) throws MedicoException
	{
		try
		{
			List<Certificato> list = certificatoDAO.findByMedico(medicoId);
			for (Certificato c : list)
			{
				certificatoDAO.getCompleto(c);
			}
			return list;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero dei certificati caricati.", "SYS_DATABASE_ERROR");
		}
	}

	public Certificato findCertificatoById(int id) throws MedicoException
	{
		try
		{
			Certificato c = certificatoDAO.findById(id).orElseThrow(() -> new MedicoException("Certificato non trovato.", "CERTIFICATO_NON_TROVATO"));
			certificatoDAO.getCompleto(c);
			return c;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema nel recupero del certificato.", "SYS_DATABASE_ERROR");
		}
	}

	public void caricaCertificato(int medicoId, int tipoCertificatoId, String nomeFile, byte[] dati, String mimeType, LocalDateTime dataScadenza) throws MedicoException
	{
		try
		{

			Certificato c = new Certificato();
			c.setMedicoId(medicoId);
			c.setTipoCertificatoId(tipoCertificatoId);
			c.setNomeFile(nomeFile);
			c.setDatiDocumento(dati);
			c.setMimeType(mimeType);
			c.setDataCaricamento(LocalDateTime.now());
			c.setDataScadenza(dataScadenza);
			c.setStato(Certificato.Stato.IN_REVISIONE);

			Optional<Certificato> certificatoPassato = certificatoDAO.findByMedicoETipo(medicoId, tipoCertificatoId);
			if (certificatoPassato.isPresent())
			{
				eliminaCertificato(certificatoPassato.get().getId(), medicoId);
			}

			certificatoDAO.insert(c);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante il salvataggio del certificato.", "SYS_DATABASE_ERROR");
		}
	}

	public void eliminaCertificato(int certificatoId, int medicoId) throws MedicoException
	{
		try
		{
			Optional<Certificato> certOpt = certificatoDAO.findById(certificatoId);
			if (certOpt.isPresent())
			{
				Certificato c = certOpt.get();
				if (c.getMedicoId() != medicoId)
				{
					throw new MedicoException("Non hai i permessi per eliminare questo certificato.", "AUTH_ACCESSO_NEGATO");
				}

				certificatoDAO.updateStato(certificatoId, Certificato.Stato.ELIMINATO);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new MedicoException("Errore di sistema durante l'eliminazione del certificato.", "SYS_DATABASE_ERROR");
		}
	}

}
