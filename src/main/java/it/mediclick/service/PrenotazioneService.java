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
					.orElseThrow(() -> new PrenotazioneException("Impostazione trattenuta piattaforma non trovata", "IMPOSTAZIONE_NOT_FOUND"));
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore nel recupero della trattenuta: " + e.getMessage(), "TRATTENUTA_ERROR");
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
			throw new PrenotazioneException("Errore durante la validazione dello sconto: " + e.getMessage(), "SCONTO_VALIDATION_ERROR");
		}
	}

	public CodiceSconto findSconto(String codice) throws PrenotazioneException
	{
		try
		{
			return codiceScontoDAO.findByCodice(codice).orElseThrow(() -> new PrenotazioneException("Codice sconto non trovato", "SCONTO_NOT_FOUND"));
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore durante la ricerca del sconto", "SCONTO_SEARCH_ERROR");
		}

	}

	public boolean bloccaDisponibilita(int disponibilitaId, int erogazioneId, int pazienteId) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{

				Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));

				ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(erogazioneId).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata", "PRESTAZIONE_NOT_FOUND"));

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
						throw new PrenotazioneException("Uno o più slot consecutivi necessari non sono liberi o già prenotati.", "SLOTS_NOT_AVAILABLE");
					}

					java.time.Duration slotDur = java.time.Duration.between(d.getDataOraInizio(), d.getDataOraFine());
					minutiTotaliDisponibili += slotDur.toMinutes();
				}

				if (minutiTotaliDisponibili < durata)
				{
					throw new PrenotazioneException("Non ci sono abbastanza slot consecutivi disponibili per coprire la durata della prestazione (" + durata + " minuti)", "INSUFFICIENT_SLOTS");
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
			throw new PrenotazioneException("Errore durante il blocco degli slot: " + e.getMessage(), "BLOCCO_ERROR");
		}
	}

	public void sbloccaDisponibilita(int disponibilitaId, int erogazioneId) throws PrenotazioneException
	{
		try
		{
			Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));

			ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(erogazioneId).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata", "PRESTAZIONE_NOT_FOUND"));

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
			throw new PrenotazioneException("Errore durante lo sblocco degli slot: " + e.getMessage(), "SBLOCCO_ERROR");
		}
	}

	public RiepilogoPrenotazioneDTO getRiepilogoPrenotazione(int idStudio, int idPrestazione, int idDisponibilita) throws PrenotazioneException
	{
		try
		{
			RiepilogoPrenotazioneDTO dto = new RiepilogoPrenotazioneDTO();

			Disponibilita disponibilita = disponibilitaDAO.findById(idDisponibilita).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));
			ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idPrestazione).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata", "PRESTAZIONE_NOT_FOUND"));
			Studio studio = studioDAO.findById(idStudio).orElseThrow(() -> new PrenotazioneException("Studio non trovato", "STUDIO_NOT_FOUND"));

			CatalogoPrestazioni catalogo = catalogoPrestazioniDAO.findById(prestazione.getCatalogoPrestazioniId())
					.orElseThrow(() -> new PrenotazioneException("Catalogo prestazioni non trovato", "CATALOGO_PRESTAZIONI_NOT_FOUND"));
			Medico medico = medicoDAO.findById(prestazione.getMedicoId()).orElseThrow(() -> new PrenotazioneException("Medico non trovato", "MEDICO_NOT_FOUND"));

			dto.setCatalogoPrestazioni(catalogo);
			dto.setMedico(medico);
			dto.setDisponibilita(disponibilita);
			dto.setPrestazione(prestazione);
			dto.setStudio(studio);

			return dto;
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore durante il recupero del riepilogo prenotazione: " + e.getMessage(), "RIEPILOGO_ERROR");
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
				Disponibilita slotIniziale = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new PrenotazioneException("Disponibilità non trovata", "DISPONIBILITA_NOT_FOUND"));

				ErogazionePrestazione prestazione = erogazionePrestazioneDAO.findById(idErogazione).orElseThrow(() -> new PrenotazioneException("Prestazione non trovata", "PRESTAZIONE_NOT_FOUND"));

				int durata = prestazione.getDurata();
				LocalDateTime dataInizio = slotIniziale.getDataOraInizio();
				LocalDateTime dataFine = dataInizio.plusMinutes(durata);

				// Recuperiamo tutti gli slot che coprono la prestazione
				List<Disponibilita> slotNecessari = disponibilitaDAO.findDisponibiliFilterDate(slotIniziale.getMedicoId(), dataInizio, dataFine);

				// Verifichiamo lo stato del blocco per tutti gli slot necessari
				for (Disponibilita d : slotNecessari)
				{
					if (d.getStato() != Disponibilita.Stato.BLOCCATA)
						throw new PrenotazioneException("Uno o più slot non sono bloccati per la transazione", "DISPONIBILITA_NOT_BLOCKED");

					if (d.getPazienteId() < 0 || d.getPazienteId() != pazienteId)
						throw new PrenotazioneException("Tentativo di prenotazione su slot bloccati da un altro utente", "BLOCKED_BY_OTHER_USER");

					if (d.getTimestampBlocco() != null && d.getTimestampBlocco().plusMinutes(15).isBefore(LocalDateTime.now()))
						throw new PrenotazioneException("Il blocco temporaneo di 15 minuti di uno slot è scaduto", "BLOCCO_SCADUTO");
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

				// Inseriamo la prenotazione
				prenotazioneDAO.insert(p, conn);

				// Impostiamo tutti gli slot necessari come PRENOTATI
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
				throw new PrenotazioneException("Errore transazione prenotazione: " + e.getMessage(), "PRENOTAZIONE_TRANSACTION_ERROR");
			}
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore connessione database: " + e.getMessage(), "DB_CONNECTION_ERROR");
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
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));

				if (p.getPazienteId() != pazienteIdConnesso)
				{
					throw new PrenotazioneException("Non hai i permessi per disdire questa prenotazione", "AUTH_ERROR");
				}
				if (p.getStato() == Prenotazione.Stato.CANCELLATA)
				{
					throw new PrenotazioneException("Prenotazione già cancellata", "PRENOTAZIONE_ALREADY_CANCELLED");
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
				throw new PrenotazioneException("Errore durante la disdetta: " + e.getMessage(), "DISDETTA_ERROR");
			}
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore connessione database: " + e.getMessage(), "DB_CONNECTION_ERROR");
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
			throw new PrenotazioneException("Errore nel recupero dei dettagli del medico: " + e.getMessage(), "MEDICO_DETAILS_ERROR");
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
			throw new PrenotazioneException("Errore durante il recupero delle prenotazioni: " + e.getMessage(), "PRENOTAZIONI_NOT_FOUND");
		}

		return prenotazioni;
	}

	public boolean concludiVisita(int prenotazioneId, int medicoId) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));

				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

				if (p.getDisponibilita() == null || p.getDisponibilita().getMedicoId() != medicoId)
				{
					throw new PrenotazioneException("Operazione non autorizzata per questo medico.", "UNAUTHORIZED");
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
				throw e;
			}
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore durante la conclusione della visita: " + e.getMessage(), "VISITA_CONCLUSION_ERROR");
		}
	}

	public boolean annullaPrenotazione(int prenotazioneId, int medicoIdConnesso) throws PrenotazioneException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{
				Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new PrenotazioneException("Prenotazione non trovata", "PRENOTAZIONE_NOT_FOUND"));

				prenotazioneDAO.getCompleto(p);
				erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

				if (p.getErogazionePrestazione().getMedicoId() != medicoIdConnesso)
				{
					throw new PrenotazioneException("Non hai i permessi per disdire questa prenotazione", "AUTH_ERROR");
				}
				if (p.getStato() == Prenotazione.Stato.CANCELLATA)
				{
					throw new PrenotazioneException("Prenotazione già cancellata", "PRENOTAZIONE_ALREADY_CANCELLED");
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
				throw new PrenotazioneException("Errore durante la disdetta: " + e.getMessage(), "DISDETTA_ERROR");
			}
		}
		catch (SQLException e)
		{
			throw new PrenotazioneException("Errore connessione database: " + e.getMessage(), "DB_CONNECTION_ERROR");
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
			throw new PrenotazioneException("Errore nel calcolo delle statistiche: " + e.getMessage(), "STATS_ERROR");
		}
	}
}
