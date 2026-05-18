package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import it.mediclick.model.bean.Amministratore;
import it.mediclick.model.bean.ImpostazioniSistema;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class ImpostazioniSistemaDAO
{
	private final Contex _contex;
	private final AmministratoreDAO amministratoreDAO;

	public ImpostazioniSistemaDAO(Contex contex)
	{
		_contex = contex;
		amministratoreDAO = new AmministratoreDAO(_contex);
	}

	public Optional<ImpostazioniSistema> findByID(int id) throws SQLException
	{

		try
		{
			String sql = """
							         SELECT *
							         FROM ImpostazioniSistema
							         WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, impostazioniSistemaMapper, id);

		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca delle impostazioni per id " + id + ": " + e.getMessage(), e);
		}
	}

	public List<ImpostazioniSistema> findAll() throws SQLException
	{
		try
		{
			String sql = """
							          SELECT *
							         FROM ImpostazioniSistema
							         WHERE Data_Fine is NULL
							""";

			return _contex.eseguiSelect(sql, impostazioniSistemaMapper);

		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca delle impostazioni " + e.getMessage(), e);
		}
	}

	public Optional<Integer> findIDByKey(String key) throws SQLException
	{
		try
		{
			String sql = """
							    SELECT *
							    FROM ImpostazioniSistema
							    WHERE Chiave = ?
							    AND Data_Fine is NULL
							""";

			ImpostazioniSistema imp = _contex.eseguiSelectSingolo(sql, impostazioniSistemaMapper, key).orElse(null);
			if (imp == null)
			{
				return Optional.empty();
			}
			return Optional.of(imp.getId());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca delle impostazioni per chiave " + key + ": " + e.getMessage(), e);
		}
	}

	public Optional<Integer> findValueByKey(String key) throws SQLException
	{
		try
		{
			String sql = """
							    SELECT *
							    FROM ImpostazioniSistema
							    WHERE Chiave = ?
							    AND Data_Fine is NULL
							""";

			ImpostazioniSistema imp = _contex.eseguiSelectSingolo(sql, impostazioniSistemaMapper, key).orElse(null);
			if (imp == null)
			{
				return Optional.empty();
			}
			return Optional.of(imp.getValore() != null ? Integer.parseInt(imp.getValore()) : null);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca delle impostazioni per chiave " + key + ": " + e.getMessage(), e);
		}
	}

	public int insert(String chiave, String valore, int idAmministatore) throws SQLException
	{
		Connection conn = _contex.getConnection();
		conn.setAutoCommit(false);
		try
		{
			Optional<Integer> idOld = findIDByKey(chiave);
			if (idOld.isPresent())
			{
				String sqlUpdate = """
									     UPDATE ImpostazioniSistema
									     SET Valore = ?, Updated_by = ?, Data_Inizio = ?
									     WHERE ID = ?
									""";
				_contex.eseguiUpdate(sqlUpdate, conn, valore, idAmministatore, LocalDateTime.now(), idOld.get());
				conn.commit();
				return idOld.get();
			}
			else
			{
				String sqlInsert = """
									     INSERT INTO ImpostazioniSistema (Chiave, Valore, Data_Inizio, Data_Fine, Updated_by)
									     VALUES(?, ?, ?, NULL, ?)
									""";
				int idnew = _contex.eseguiUpdate(sqlInsert, conn, chiave, valore, LocalDateTime.now(), idAmministatore);
				conn.commit();
				return idnew;
			}
		}
		catch (SQLException e)
		{
			conn.rollback();
			throw new SQLException("Errore nell'inserimento o aggiornamento delle impostazioni: " + e.getMessage(), e);
		}
		finally
		{
			conn.close();
		}
	}

	public void getCompleto(ImpostazioniSistema i) throws SQLException
	{
		int idAmministratore = i.getAmministratoreId();

		if (idAmministratore > 0)
		{
			Amministratore a = amministratoreDAO.findById(idAmministratore).orElseThrow(() -> new SQLException("Amministratore con ID " + idAmministratore + " non trovato"));
			i.setAmministratore(a);
		}

	}

	private final ResultMapper<ImpostazioniSistema> impostazioniSistemaMapper = row ->
	{
		ImpostazioniSistema i = new ImpostazioniSistema();

		Integer idAmministratore = MapRow.getIntOrNull(row, "Updated_by");
		idAmministratore = idAmministratore != null ? idAmministratore : -1;

		i.setId(MapRow.getInt(row, "ID"));
		i.setChiave(MapRow.getString(row, "Chiave"));
		i.setValore(MapRow.getString(row, "Valore"));
		i.setDataInizio(MapRow.getLocalDateTime(row, "Data_Inizio"));
		i.setDataFine(MapRow.getLocalDateTime(row, "Data_Fine"));
		i.setAmministratoreId(idAmministratore);

		return i;
	};

}
