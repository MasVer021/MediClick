package it.mediclick.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.mediclick.exception.PrenotazioneException;
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

public class PrenotazioneService
{

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
			return impostazioniSistemaDAO.findValueByKey("COMMISSIONE_PIATTAFORMA_PCT")
					.orElseThrow(() -> new PrenotazioneException("Impostazione commissione piattaforma non trovata.", "IMPOSTAZIONE_NON_TROVATA"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nel recupero della commissione.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema durante la validazione dello sconto.", "SYS_DATABASE_ERROR");
		}
	}

	public CodiceSconto findSconto(String codice) throws PrenotazioneException
	{
		try
		{
			return codiceScontoDAO.findByCodice(codice).orElseThrow(() -> new PrenotazioneException("Codice sconto non trovato o non valido.", "SCONTO_NON_TROVATO"));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nella ricerca dello sconto.", "SYS_DATABASE_ERROR");
		}

	}

	public boolean bloccaDisponibilita(int disponibilitaId, int erogazioneId, int pazienteId) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{

				Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Slot di disponibilità non trovato.", "DISPONIBILITA_NON_TROVATA"));

				ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(erogazioneId).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata.", "PRESTAZIONE_NON_TROVATA"));

				int durata = prestazione.getDurata();

				LocalDateTime dataInizio = slotIniziale.getDataOraInizio();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(slotIniziale.getMedicoId(), dataInizio, dataFine);

				long minutiTotaliDisponibili = 0;

				for (Disponibilita d : slotNecessari)
				{
					boolean isDisponibile = d.getStato() == Disponibilita.Stato.DISPONIBILE;
					boolean isBloccoScaduto = d.getStato() == Disponibilita.Stato.BLOCCATA && d.getTimestampBlocco() != null && d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now());

					if (!isDisponibile && !isBloccoScaduto)
					{
						throw new PrenotazioneException("Uno o più slot necessari non sono liberi o sono già occupati.", "SLOT_NON_DISPONIBILE");
					}

					java.time.Duration slotDur = java.time.Duration.between(d.getDataOraInizio(), d.getDataOraFine());
					minutiTotaliDisponibili += slotDur.toMinutes();
				}

				if (minutiTotaliDisponibili < durata)
				{
					throw new PrenotazioneException("Non ci sono abbastanza slot consecutivi disponibili per coprire la durata della visita.", "SLOT_INSUFFICIENTI");
				}

				for (Disponibilita d : slotNecessari)
				{
					disponibilitaDAO.setBlocco(d.getId(), pazienteId, true);
				}

				conn.commit();
				return true;
			}
			catch (Exception e)
			{
				conn.rollback();
				throw e;
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema durante il blocco degli slot temporanei.", "SYS_DATABASE_ERROR");
		}
	}

	public void sbloccaDisponibilita(int disponibilitaId, int erogazioneId) throws PrenotazioneException
	{
		try
		{
			Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Slot di disponibilità non trovato.", "DISPONIBILITA_NON_TROVATA"));

			ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(erogazioneId).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata.", "PRESTAZIONE_NON_TROVATA"));

			int durata = prestazione.getDurata();
			LocalDateTime dataInizio = slotIniziale.getDataOraInizio();
			LocalDateTime dataFine = dataInizio.plusMinutes(durata);

			List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(slotIniziale.getMedicoId(), dataInizio, dataFine);

			for (Disponibilita d : slotNecessari)
			{
				disponibilitaDAO.setBlocco(d.getId(), null, false);
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema durante lo sblocco degli slot.", "SYS_DATABASE_ERROR");
		}
	}

	public RiepilogoPrenotazioneDTO getRiepilogoPrenotazione(int idStudio, int idPrestazione, int idDisponibilita) throws PrenotazioneException
	{
		try
		{
			RiepilogoPrenotazioneDTO dto = new RiepilogoPrenotazioneDTO();

			Disponibilita disponibilita = disponibilitaDAO.findById(idDisponibilita).orElseThrow(() -> new PrenotazioneException("Slot di disponibilità non trovato.", "DISPONIBILITA_NON_TROVATA"));
			ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idPrestazione).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata.", "PRESTAZIONE_NON_TROVATA"));
			Studio studio = studioDAO.findById(idStudio).orElseThrow(() -> new PrenotazioneException("Studio medico non trovato.", "STUDIO_NON_TROVATO"));

			CatalogoPrestazioni catalogo = catalogoPrestazioniDAO.findById(prestazione.getCatalogoPrestazioniId())
					.orElseThrow(() -> new PrenotazioneException("Prestazione a catalogo non trovata.", "PRESTAZIONE_NON_TROVATA"));
			Medico medico = medicoDAO.findById(prestazione.getMedicoId()).orElseThrow(() -> new PrenotazioneException("Profilo medico non trovato.", "MEDICO_NON_TROVATO"));

			dto.setCatalogoPrestazioni(catalogo);
			dto.setMedico(medico);
			dto.setDisponibilita(disponibilita);
			dto.setPrestazione(prestazione);
			dto.setStudio(studio);

			return dto;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nel recupero del riepilogo della prenotazione.", "SYS_DATABASE_ERROR");
		}
	}

	public boolean creaPrenotazione(int pazienteId, int disponibilitaId, String idTransazioneEsterno, int idErogazione, double prezzo_pagato, double prezzo_trattenuta, double prezzo_netto,
			double prezzo_tasse, int idSconto, String metodoPagamento) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Slot di disponibilità non trovato.", "DISPONIBILITA_NON_TROVATA"));

				ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idErogazione).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata.", "PRESTAZIONE_NON_TROVATA"));

				int durata = prestazione.getDurata();
				LocalDateTime dataInizio = slotIniziale.getDataOraInizio();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(slotIniziale.getMedicoId(), dataInizio, dataFine);

				for (Disponibilita d : slotNecessari)
				{
					if (d.getStato() != Disponibilita.Stato.BLOCCATA)
						throw new PrenotazioneException("Uno o più slot non sono bloccati per la prenotazione.", "PRENOTAZIONE_SLOT_NON_BLOCCATO");

					if (d.getPazienteId() < 0 || d.getPazienteId() != pazienteId)
						throw new PrenotazioneException("Tentativo di prenotazione su slot bloccati da un altro utente.", "AUTH_ACCESSO_NEGATO");

					if (d.getTimestampBlocco() != null && d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now()))
						throw new PrenotazioneException("Il blocco temporaneo dello slot è scaduto. Riprova.", "PRENOTAZIONE_BLOCCO_SCADUTO");
				}

				Prenotazione p = new Prenotazione();
				p.setPazienteId(pazienteId);
				p.setDisponibilitaId(disponibilitaId);
				p.setErogazionePrestazioneId(idErogazione);
				p.setDataPagamento(LocalDateTime.now());
				p.setStato(Prenotazione.Stato.CONFERMATA);
				p.setIdTransazioneEsterno(idTransazioneEsterno);

				if (idSconto > 0)
					p.setCodiceScontoId(idSconto);

				p.setImportoPagato(prezzo_pagato);
				p.setMetodoPagamento(metodoPagamento);
				p.setRicavoNettoMedicoEuro(prezzo_netto);
				p.setTasseStimateEuro(prezzo_tasse);
				p.setTrattenutaPiattaformaEuro(prezzo_trattenuta);

				prenotazioneDAO.insert(p, conn);

				for (Disponibilita d : slotNecessari)
				{
					disponibilitaDAO.updateStato(d.getId(), Disponibilita.Stato.PRENOTATA, conn);
				}

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
				e.printStackTrace();
				throw new PrenotazioneException("Errore del sistema durante la persistenza della prenotazione.", "SYS_DATABASE_ERROR");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nella connessione al database.", "SYS_DATABASE_ERROR");
		}
	}

	public double getPrezzoPagato(double prezzoListino, double percentualeSconto)
	{
		return prezzoListino * (1 - (percentualeSconto / 100.0));
	}

	public double getPrezzoTrattenuta(double prezzoListino, double percentualeSconto) throws PrenotazioneException
	{
		double prezzoPagato = getPrezzoPagato(prezzoListino, percentualeSconto);

		return (getTrattenuta() / 100.0) * prezzoPagato;
	}

	public double getTasse(double prezzoListino, double percentualeSconto, double aliquota) throws PrenotazioneException
	{
		double prezzoPagatoNotrattenuta = getPrezzoPagato(prezzoListino, percentualeSconto) - getPrezzoTrattenuta(prezzoListino, percentualeSconto);
		return prezzoPagatoNotrattenuta * (aliquota / 100);
	}

	public double getPrezzoNetto(double prezzoListino, double percentualeSconto, double aliquota) throws PrenotazioneException
	{
		double prezzo_pagato = getPrezzoPagato(prezzoListino, percentualeSconto);
		double prezzo_trattenuta = getPrezzoTrattenuta(prezzoListino, percentualeSconto);
		double prezzo_tasse = getTasse(prezzoListino, percentualeSconto, aliquota);

		return prezzo_pagato - prezzo_trattenuta - prezzo_tasse;
	}

	public boolean disdiciPrenotazione(int prenotazioneId, int pazienteIdConnesso) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata.", "PRENOTAZIONE_NON_TROVATA"));

				if (p.getPazienteId() != pazienteIdConnesso)
				{
					throw new PrenotazioneException("Non hai i permessi per disdire questa prenotazione.", "AUTH_ACCESSO_NEGATO");
				}
				if (p.getStato() == Prenotazione.Stato.CANCELLATA)
				{
					throw new PrenotazioneException("Prenotazione già cancellata.", "PRENOTAZIONE_GIA_CANCELLATA");
				}

				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

				LocalDateTime dataInizio = p.getDisponibilita().getDataOraInizio();
				int durata = p.getErogazionePrestazione().getDurata();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(p.getDisponibilita().getMedicoId(), dataInizio, dataFine);

				prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.CANCELLATA, conn);

				for (Disponibilita d : slotNecessari)
				{
					disponibilitaDAO.updateStato(d.getId(), Disponibilita.Stato.DISPONIBILE, conn);
				}

				conn.commit();
				return true;
			}
			catch (PrenotazioneException pe)
			{
				conn.rollback();
				throw pe;
			}
			catch (Exception e)
			{
				conn.rollback();
				e.printStackTrace();
				throw new PrenotazioneException("Errore del sistema durante la disdetta della prenotazione.", "SYS_DATABASE_ERROR");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nella connessione al database.", "SYS_DATABASE_ERROR");
		}
	}

	public void getCompleto(Medico m) throws PrenotazioneException
	{
		try
		{
			medicoDAO.getCompleto(m);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nel recupero dei dettagli del medico.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Prenotazione> getPrenotazioniPaziente(int pazienteId, boolean future) throws PrenotazioneException
	{

		ErogazionePrestazioneDAO erogazioneDAO = new ErogazionePrestazioneDAO(_contex);

		List<Prenotazione> prenotazioni;
		try
		{
			prenotazioni = prenotazioneDAO.findByPaziente(pazienteId);

			for (Prenotazione p : prenotazioni)
			{
				prenotazioneDAO.getCompleto(p);
				erogazioneDAO.getCompleto(p.getErogazionePrestazione());

			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nel recupero delle prenotazioni.", "SYS_DATABASE_ERROR");
		}

		return prenotazioni;
	}

	public Prenotazione getPrenotazionePaziente(int pazienteId, int prenotazioneId) throws PrenotazioneException
	{

		try
		{
			Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));

			if (p.getPazienteId() == pazienteId)
			{
				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());
				medicoDAO.getCompleto(p.getErogazionePrestazione().getMedico());
				return p;
			}
			else
			{
				throw new PrenotazioneException("Prenotazione non disponibile alla consultazione", "AUTH_ERROR");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore del sistema durante il recupero della prenotazione.", "SYS_DATABASE_ERROR");
		}

	}

	public boolean concludiVisita(int prenotazioneId, int medicoId) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata.", "PRENOTAZIONE_NON_TROVATA"));

				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

				if (p.getDisponibilita() == null || p.getDisponibilita().getMedicoId() != medicoId)
				{
					throw new PrenotazioneException("Non hai i permessi per concludere questa visita.", "AUTH_ACCESSO_NEGATO");
				}

				LocalDateTime dataInizio = p.getDisponibilita().getDataOraInizio();
				int durata = p.getErogazionePrestazione().getDurata();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(p.getDisponibilita().getMedicoId(), dataInizio, dataFine);

				prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.COMPLETATA, conn);

				for (Disponibilita d : slotNecessari)
				{
					disponibilitaDAO.updateStato(d.getId(), Disponibilita.Stato.COMPLETATA, conn);
				}

				conn.commit();
				return true;
			}
			catch (Exception e)
			{
				conn.rollback();
				e.printStackTrace();
				if (e instanceof PrenotazioneException)
				{
					throw (PrenotazioneException) e;
				}
				throw new PrenotazioneException("Errore del sistema durante la conclusione della visita.", "SYS_DATABASE_ERROR");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nella connessione al database.", "SYS_DATABASE_ERROR");
		}
	}

	public boolean annullaPrenotazione(int prenotazioneId, int medicoIdConnesso) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata.", "PRENOTAZIONE_NON_TROVATA"));

				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

				if (p.getErogazionePrestazione().getMedicoId() != medicoIdConnesso)
				{
					throw new PrenotazioneException("Non hai i permessi per annullare questa prenotazione.", "AUTH_ACCESSO_NEGATO");
				}
				if (p.getStato() == Prenotazione.Stato.CANCELLATA)
				{
					throw new PrenotazioneException("Prenotazione già cancellata.", "PRENOTAZIONE_GIA_CANCELLATA");
				}

				LocalDateTime dataInizio = p.getDisponibilita().getDataOraInizio();
				int durata = p.getErogazionePrestazione().getDurata();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(p.getDisponibilita().getMedicoId(), dataInizio, dataFine);

				prenotazioneDAO.updateStato(prenotazioneId, Prenotazione.Stato.CANCELLATA, conn);

				for (Disponibilita d : slotNecessari)
				{
					disponibilitaDAO.updateStato(d.getId(), Disponibilita.Stato.DISPONIBILE, conn);
				}

				conn.commit();
				return true;
			}
			catch (PrenotazioneException pe)
			{
				conn.rollback();
				throw pe;
			}
			catch (Exception e)
			{
				conn.rollback();
				e.printStackTrace();
				throw new PrenotazioneException("Errore del sistema durante l'annullamento della prenotazione.", "SYS_DATABASE_ERROR");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nella connessione al database.", "SYS_DATABASE_ERROR");
		}
	}

	public Map<String, Object> getStatistiche(int medicoId, LocalDate inizio, LocalDate fine) throws PrenotazioneException
	{
		try
		{

			List<Prenotazione> prenotazioni = prenotazioneDAO.findByMedico(medicoId, inizio.atStartOfDay(), fine.atStartOfDay());

			int conteggio = 0;
			double guadagnoNetto = 0.0;
			double tasseStimate = 0.0;

			for (Prenotazione p : prenotazioni)
			{
				if (p.getStato() == Prenotazione.Stato.COMPLETATA)
				{
					conteggio++;
					guadagnoNetto += p.getRicavoNettoMedicoEuro();
					tasseStimate += p.getTasseStimateEuro();
				}
			}

			Map<String, Object> stats = new HashMap<>();

			stats.put("conteggio", conteggio);
			stats.put("guadagnoNetto", guadagnoNetto);
			stats.put("tasseStimate", tasseStimate);

			return stats;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PrenotazioneException("Errore di sistema nel recupero delle statistiche.", "SYS_DATABASE_ERROR");
		}
	}
}
