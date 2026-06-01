package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Studio;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class DisponibilitaDAO
{
	private final Contex _contex;
	private final MedicoDAO medicoDAO;
	private final PazienteDAO pazienteDAO;
	private final StudioDAO studioDAO;

	public DisponibilitaDAO(Contex contex)
	{
		_contex = contex;
		medicoDAO = new MedicoDAO(_contex);
		pazienteDAO = new PazienteDAO(_contex);
		studioDAO = new StudioDAO(_contex);
	}

	public Optional<Disponibilita> findById(int id) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Disponibilita
							   WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, disponibilitaMapper, id);

		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita con ID " + id, e);
		}
	}

	public List<Disponibilita> findByMedico(int medicoId) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Disponibilita
							   WHERE Medico_ID = ?
							""";

			return _contex.eseguiSelect(sql, disponibilitaMapper, medicoId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita con ID medico " + medicoId, e);
		}
	}

	public List<Disponibilita> findByMedicoEData(int medicoId, LocalDate data) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Disponibilita
							   WHERE Medico_ID = ? AND DATE(Data_Ora_Inizio) = ?
							""";

			return _contex.eseguiSelect(sql, disponibilitaMapper, medicoId, java.sql.Date.valueOf(data));
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita con ID medico " + medicoId + " e data " + data, e);
		}
	}

	public List<Disponibilita> findByMedicoEStudio(int medicoId, int studioId) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Disponibilita
							   WHERE Medico_ID = ?
							   AND Studio_ID = ?
							   AND (Stato = 'Disponibile' OR (Stato = 'Bloccata' AND Timestamp_Blocco < DATE_SUB(NOW(), INTERVAL 15 MINUTE)))
							   AND Data_Ora_Inizio > NOW()
							""";

			return _contex.eseguiSelect(sql, disponibilitaMapper, medicoId, studioId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita con ID medico " + medicoId + " e studio " + studioId, e);
		}
	}

	public List<Disponibilita> findDisponibili(int medicoId) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Disponibilita
							   WHERE Medico_ID = ?
							   AND (Stato = 'Disponibile' OR (Stato = 'Bloccata' AND Timestamp_Blocco < DATE_SUB(NOW(), INTERVAL 15 MINUTE)))
							   AND Data_Ora_Inizio > NOW()
							""";

			return _contex.eseguiSelect(sql, disponibilitaMapper, medicoId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita libere con ID medico " + medicoId, e);
		}
	}

	public List<Disponibilita> findDisponibiliFilterDate(int medicoId, LocalDateTime dataInizio, LocalDateTime dataFine) throws SQLException
	{
		try
		{
			String sql = """
							    SELECT *
							    FROM Disponibilita
							    WHERE Medico_ID = ?
							 AND Data_Ora_Inizio < ?
							 AND Data_Ora_Fine > ?
							 AND Stato != 'Cancellata'
							""";

			return _contex.eseguiSelect(sql, disponibilitaMapper, medicoId, dataFine, dataInizio);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca della disponibilita libere con ID medico " + medicoId, e);
		}
	}

	public void insertMultiDisponibilita(List<Disponibilita> disponibilitaList) throws SQLException
	{
		try (Connection conn = _contex.getConnection())
		{
			conn.setAutoCommit(false);
			try
			{

				for (Disponibilita d : disponibilitaList)
				{
					insert(d, conn);
				}

				conn.commit();
			}
			catch (SQLException e)
			{
				conn.rollback();
				throw new SQLException("Errore nell'inserimento delle disponibilita: " + e.getMessage(), e);
			}
		}
		catch (Exception e)
		{
			throw new SQLException("Errore nella gestione della transazione per l'inserimento delle disponibilita: " + e.getMessage(), e);
		}

	}

	public void insert(Disponibilita d, Connection conn) throws SQLException
	{

		String sql = """
						   INSERT INTO Disponibilita(Medico_ID, Studio_ID, Data_Ora_Inizio, Data_Ora_Fine, Stato, Timestamp_Blocco, Paziente_ID_Blocco)
						   VALUES (?,?,?,?,?,?,?)
						""";
		try
		{

			Integer medicoId = d.getMedicoId() > 0 ? d.getMedicoId() : null;
			Integer studioId = d.getStudioId() > 0 ? d.getStudioId() : null;

			Integer pazienteIdBlocco = d.getPazienteId() > 0 ? d.getPazienteId() : null;

			LocalDateTime dataOraInizio = d.getDataOraInizio() != null ? d.getDataOraInizio() : null;
			LocalDateTime dataOraFine = d.getDataOraFine() != null ? d.getDataOraFine() : null;
			LocalDateTime dataOraBlocco = d.getTimestampBlocco() != null ? d.getTimestampBlocco() : null;
			String stato = d.getStato() != null ? d.getStato().getLabel() : null;

			_contex.eseguiUpdate(sql, conn, medicoId, studioId, dataOraInizio, dataOraFine, stato, dataOraBlocco, pazienteIdBlocco);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'inserimento della disponibilita: " + e.getMessage(), e);
		}
	}

	public void updateStato(int id, Disponibilita.Stato stato) throws SQLException
	{
		try (Connection conn = _contex.getConnection())
		{
			updateStato(id, stato, conn);
		}

	}

	public void updateStato(int id, Disponibilita.Stato stato, Connection conn) throws SQLException
	{
		String sql = """
						   UPDATE Disponibilita
						   SET Stato = ?
						   WHERE ID = ?
						""";
		try
		{
			if (!stato.equals(Disponibilita.Stato.BLOCCATA))
				_contex.eseguiUpdate(sql, conn, stato.getLabel(), id);
			else
				throw new SQLException("Per bloccare una disponibilita, utilizzare il metodo setBlocco con ID della disponibilita, ID del paziente e blocca=true");
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento dello stato della disponibilita con ID " + id + ": " + e.getMessage(), e);
		}
	}

	public void setBlocco(int id, Integer pazienteId, boolean blocca) throws SQLException
	{
		String sql = """
						   UPDATE Disponibilita
						   SET Stato = ?, Timestamp_Blocco = ?, Paziente_ID_Blocco = ?
						   WHERE ID = ?
						""";
		String stato = blocca ? Disponibilita.Stato.BLOCCATA.getLabel() : Disponibilita.Stato.DISPONIBILE.getLabel();
		LocalDateTime ts = blocca ? LocalDateTime.now() : null;
		Integer pId = blocca ? pazienteId : null;

		try
		{
			_contex.eseguiUpdate(sql, stato, ts, pId, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel settare il blocco della disponibilita con ID " + id + ": " + e.getMessage(), e);
		}
	}

	public void deleteLogic(int id) throws SQLException
	{
		String sql = """
						   UPDATE Disponibilita
						   SET Stato = ?
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, Disponibilita.Stato.CANCELLATA.getLabel(), id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella cancellazione logica della disponibilita con ID " + id + ": " + e.getMessage(), e);
		}
	}

	public void delete(int id) throws SQLException
	{
		String sql = """
						   DELETE FROM Disponibilita
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella cancellazione della disponibilita con ID " + id + ": " + e.getMessage(), e);
		}
	}

	public void getCompleto(Disponibilita d) throws SQLException
	{
		int medicoId = d.getMedicoId();
		int studioId = d.getStudioId();
		int pazineteIdBlocco = d.getPazienteId();

		Medico m = medicoDAO.findById(medicoId).orElseThrow(() -> new SQLException("Medico non trovato per disponibilita id: " + medicoId));
		Studio s = studioDAO.findById(studioId).orElseThrow(() -> new SQLException("Studio non trovato per disponibilita id: " + studioId));

		if (pazineteIdBlocco > 0)
		{
			Paziente p = pazienteDAO.findById(pazineteIdBlocco).orElse(null);
			d.setPaziente(p);
		}

		d.setMedico(m);
		d.setStudio(s);

	}

	private final ResultMapper<Disponibilita> disponibilitaMapper = row ->
	{
		Disponibilita d = new Disponibilita();
		int id = MapRow.getInt(row, "ID");

		Integer medicoId = MapRow.getIntOrNull(row, "Medico_ID");
		medicoId = medicoId != null ? medicoId : -1;

		Integer studioId = MapRow.getIntOrNull(row, "Studio_ID");
		studioId = studioId != null ? studioId : -1;

		Integer pazineteIdBlocco = MapRow.getIntOrNull(row, "Paziente_ID_Blocco");
		pazineteIdBlocco = pazineteIdBlocco != null ? pazineteIdBlocco : -1;

		d.setId(id);

		d.setStudioId(studioId);
		d.setMedicoId(medicoId);
		d.setPazienteId(pazineteIdBlocco);

		d.setDataOraFine(MapRow.getLocalDateTime(row, "Data_Ora_Fine"));
		d.setDataOraInizio(MapRow.getLocalDateTime(row, "Data_Ora_Inizio"));
		d.setTimestampBlocco(MapRow.getLocalDateTime(row, "Timestamp_Blocco"));
		d.setStato(Disponibilita.Stato.fromString((MapRow.getString(row, "Stato"))));

		return d;
	};
}
