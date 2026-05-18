package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Utente;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class PazienteDAO
{
	private final Contex _contex;
	private final UtenteDAO utenteDAO;

	public PazienteDAO(Contex contex)
	{
		_contex = contex;
		utenteDAO = new UtenteDAO(contex);
	}

	public Optional<Paziente> findById(int id) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Paziente
							   WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, pazienteMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca del paziente per ID: " + id + e.getMessage(), e);
		}
	}

	public Optional<Paziente> findByCodiceFiscale(String cf) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM Paziente
							   WHERE Codice_Fiscale = ?
							""";

			return _contex.eseguiSelectSingolo(sql, pazienteMapper, cf);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca del paziente per Codice Fiscale: " + cf + e.getMessage(), e);
		}
	}

	public int insert(Paziente p) throws SQLException
	{
		String sqlPaziente = """
								 INSERT INTO Paziente(ID,Cognome,Nome,Codice_Fiscale,Telefono,Data_Nascita)
								 VALUES (?,?,?,?,?,?)
								""";

		Connection conn = _contex.getConnection();
		conn.setAutoCommit(false);

		try
		{
			int utenteId = utenteDAO.insert(conn, p.getUtente());

			_contex.eseguiUpdate(sqlPaziente, conn, utenteId, p.getCognome(), p.getNome(), p.getCodiceFiscale(), p.getTelefono(), p.getDataNascita());

			p.setId(utenteId);

			p.getUtente().setId(utenteId);

			conn.commit();
			return utenteId;
		}
		catch (SQLException e)
		{
			conn.rollback();
			throw new SQLException("Errore durante l'inserimento del paziente: " + e.getMessage(), e);
		}
		finally
		{
			conn.close();
		}
	}

	public void updatePassword(int id, String password) throws SQLException
	{
		utenteDAO.updatePassword(id, password);
	}

	public void update(Paziente p) throws SQLException
	{
		String sql = """
						   UPDATE Paziente
						   SET Cognome = ?, Nome = ?, Codice_Fiscale = ?, Telefono = ?, Data_Nascita = ?
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, p.getCognome(), p.getNome(), p.getCodiceFiscale(), p.getTelefono(), p.getDataNascita(), p.getId());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore durante l'aggiornamento del paziente con ID " + p.getId() + ": " + e.getMessage(), e);
		}
	}

	public void getCompleto(Paziente p) throws SQLException
	{
		int utenteId = p.getId();

		Utente u = utenteDAO.findById(utenteId).orElseThrow(() -> new SQLException("Utente non trovato per ID: " + utenteId));

		p.setUtente(u);
	}

	private final ResultMapper<Paziente> pazienteMapper = row ->
	{
		Paziente p = new Paziente();

		p.setId(MapRow.getInt(row, "ID"));
		p.setCognome(MapRow.getString(row, "Cognome"));
		p.setNome(MapRow.getString(row, "Nome"));
		p.setCodiceFiscale(MapRow.getString(row, "Codice_Fiscale"));
		p.setTelefono(MapRow.getString(row, "Telefono"));
		p.setDataNascita(MapRow.getLocalDate(row, "Data_Nascita"));

		return p;

	};
}
