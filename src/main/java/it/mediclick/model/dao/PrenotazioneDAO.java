package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class PrenotazioneDAO
{
	private final Contex _contex;
	private final PazienteDAO pazienteDAO;
	private final DisponibilitaDAO disponibilitaDAO;
	private final ErogazionePrestazioneDAO erogazionePrestazioneDAO;
	private final CodiceScontoDAO codiceScontoDAO;

	public PrenotazioneDAO(Contex contex)
	{
		_contex = contex;
		pazienteDAO = new PazienteDAO(_contex);
		disponibilitaDAO = new DisponibilitaDAO(_contex);
		erogazionePrestazioneDAO = new ErogazionePrestazioneDAO(_contex);
		codiceScontoDAO = new CodiceScontoDAO(_contex);
	}

	public Optional<Prenotazione> findById(int id) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Prenotazione
							   WHERE ID = ?
							""";
			return _contex.eseguiSelectSingolo(sql, prenotazioneMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della prenotazione per ID: " + id + e.getMessage(), e);
		}
	}

	public List<Prenotazione> findByPaziente(int pazienteId) throws SQLException
	{
		try
		{
			String sql = """
							SELECT *
							FROM Prenotazione
							WHERE Paziente_ID = ?
							ORDER BY Data_Pagamento DESC
							""";

			return _contex.eseguiSelect(sql, prenotazioneMapper, pazienteId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca prenotazioni per paziente: " + e.getMessage(), e);
		}
	}

	public List<Prenotazione> findByMedico(int medicoId) throws SQLException
	{
		try
		{
			String sql = """
							SELECT P.*
							FROM Prenotazione P
							JOIN Disponibilita D ON P.Disponibilita_ID = D.ID
							WHERE D.Medico_ID = ?
							""";

			return _contex.eseguiSelect(sql, prenotazioneMapper, medicoId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca prenotazioni per medico: " + e.getMessage(), e);
		}
	}

	public List<Prenotazione> findByMedico(int medicoId, LocalDateTime dataInizio, LocalDateTime dataFine) throws SQLException
	{
		try
		{
			String sql = """
							SELECT P.*
							FROM Prenotazione P
							JOIN Disponibilita D ON P.Disponibilita_ID = D.ID
							WHERE D.Medico_ID = ?
							AND D.Data_Ora_Inizio >= ?
							AND D.Data_Ora_Fine <= ?
							""";

			return _contex.eseguiSelect(sql, prenotazioneMapper, medicoId, dataInizio, dataFine);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca prenotazioni per medico: " + e.getMessage(), e);
		}
	}

	public List<Prenotazione> findByStato(Prenotazione.Stato stato) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Prenotazione
							   WHERE Stato = ?
							""";

			return _contex.eseguiSelect(sql, prenotazioneMapper, stato.getLabel());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca prenotazioni per stato: " + e.getMessage(), e);
		}
	}

	public void insert(Prenotazione p) throws SQLException
	{
		try (Connection conn = _contex.getConnection())
		{
			insert(p, conn);
		}

	}

	public void insert(Prenotazione p, Connection conn) throws SQLException
	{
		String sql = """
						   INSERT INTO Prenotazione(Paziente_ID, Disponibilita_ID, ErogazionePrestazione_ID, CodiceSconto_ID, Stato, Metodo_Pagamento, ID_Transazione_Esterno, Importo_Pagato, Ricavo_Netto_Medico_Euro, Trattenuta_Piattaforma_Euro, Tasse_Stimate_Euro, Data_Pagamento)
						   VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
						""";
		try
		{
			Integer idpaziente = p.getPazienteId() > 0 ? (Integer) p.getPazienteId() : null;
			Integer iddisponibilita = p.getDisponibilitaId() > 0 ? (Integer) p.getDisponibilitaId() : null;
			Integer iderogazionePrestazione = p.getErogazionePrestazioneId() > 0 ? (Integer) p.getErogazionePrestazioneId() : null;
			Integer idcodiceSconto = p.getCodiceScontoId() > 0 ? (Integer) p.getCodiceScontoId() : null;

			_contex.eseguiUpdate(sql, conn, idpaziente, iddisponibilita, iderogazionePrestazione, idcodiceSconto, p.getStato() != null ? p.getStato().getLabel() : "Confermata", p.getMetodoPagamento(),
					p.getIdTransazioneEsterno(), p.getImportoPagato(), p.getRicavoNettoMedicoEuro(), p.getTrattenutaPiattaformaEuro(), p.getTasseStimateEuro(),
					p.getDataPagamento() != null ? Timestamp.valueOf(p.getDataPagamento()) : null);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'inserimento della prenotazione: " + e.getMessage(), e);
		}
	}

	public void updateStato(int id, Prenotazione.Stato stato) throws SQLException
	{

		try (Connection conn = _contex.getConnection())
		{
			updateStato(id, stato, conn);
		}

	}

	public void updateStato(int id, Prenotazione.Stato stato, Connection conn) throws SQLException
	{
		String sql = """
						   UPDATE Prenotazione
						   SET Stato = ?
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, conn, stato.getLabel(), id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento stato prenotazione: " + e.getMessage(), e);
		}
	}

	public double getGuadagniTotaliPiattaforma() throws SQLException
	{
		String sql = """
						SELECT SUM(Trattenuta_Piattaforma_Euro) as totale
						FROM Prenotazione
						WHERE Stato IN ('Confermata', 'Completata')
						""";
		try
		{
			List<Map<String, Object>> res = _contex.eseguiSelect(sql);
			if (!res.isEmpty() && res.get(0).get("totale") != null)
			{
				return ((Number) res.get(0).get("totale")).doubleValue();
			}
			return 0.0;
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel calcolo dei guadagni totali della piattaforma: " + e.getMessage(), e);
		}
	}

	public void getCompleto(Prenotazione p) throws SQLException
	{
		int pazienteId = p.getPazienteId();
		int disponibilitaId = p.getDisponibilitaId();
		int erogazionePrestazioneId = p.getErogazionePrestazioneId();
		int codiceScontoId = p.getCodiceScontoId();

		Paziente pa = pazienteDAO.findById(pazienteId).orElseThrow(() -> new SQLException("Paziente non trovato per ID: " + pazienteId));
		Disponibilita d = disponibilitaDAO.findById(disponibilitaId).orElseThrow(() -> new SQLException("Disponibilità non trovata per ID: " + disponibilitaId));
		ErogazionePrestazione ep = erogazionePrestazioneDAO.findById(erogazionePrestazioneId)
				.orElseThrow(() -> new SQLException("Erogazione di prestazione non trovata per ID: " + erogazionePrestazioneId));
		if (codiceScontoId > 0)
		{
			CodiceSconto cs = codiceScontoDAO.findById(codiceScontoId).orElse(null);
			p.setCodiceSconto(cs);
		}

		p.setPaziente(pa);
		p.setDisponibilita(d);
		p.setErogazionePrestazione(ep);
	}

	private final ResultMapper<Prenotazione> prenotazioneMapper = row ->
	{
		Prenotazione p = new Prenotazione();

		Integer pazienteId = MapRow.getIntOrNull(row, "Paziente_ID");
		pazienteId = pazienteId != null ? pazienteId : -1;

		Integer disponibilitaId = MapRow.getIntOrNull(row, "Disponibilita_ID");
		disponibilitaId = disponibilitaId != null ? disponibilitaId : -1;

		Integer erogazionePrestazioneId = MapRow.getIntOrNull(row, "ErogazionePrestazione_ID");
		erogazionePrestazioneId = erogazionePrestazioneId != null ? erogazionePrestazioneId : -1;

		Integer codiceScontoId = MapRow.getIntOrNull(row, "CodiceSconto_ID");
		codiceScontoId = codiceScontoId != null ? codiceScontoId : -1;

		p.setId(MapRow.getInt(row, "ID"));

		p.setStato(Prenotazione.Stato.fromString(MapRow.getString(row, "Stato")));
		p.setMetodoPagamento(MapRow.getString(row, "Metodo_Pagamento"));
		p.setIdTransazioneEsterno(MapRow.getString(row, "ID_Transazione_Esterno"));
		p.setImportoPagato(MapRow.getDouble(row, "Importo_Pagato"));
		p.setRicavoNettoMedicoEuro(MapRow.getDouble(row, "Ricavo_Netto_Medico_Euro"));
		p.setTrattenutaPiattaformaEuro(MapRow.getDouble(row, "Trattenuta_Piattaforma_Euro"));
		p.setTasseStimateEuro(MapRow.getDouble(row, "Tasse_Stimate_Euro"));
		p.setDataPagamento(MapRow.getLocalDateTime(row, "Data_Pagamento"));

		p.setPazienteId(pazienteId);
		p.setDisponibilitaId(disponibilitaId);
		p.setErogazionePrestazioneId(erogazionePrestazioneId);
		p.setCodiceScontoId(codiceScontoId);

		return p;
	};
}
