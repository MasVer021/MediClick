package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;

import it.mediclick.model.bean.Ruolo;
import it.mediclick.model.bean.Utente;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.PasswordUtils;
import it.mediclick.util.ResultMapper;

public class UtenteDAO
{
	private final Contex _contex;
	private RuoloDAO ruoloDAO;

	public UtenteDAO(Contex contex)
	{
		_contex = contex;
		ruoloDAO = new RuoloDAO(contex);
	}

	public Optional<Utente> findById(int id) throws SQLException
	{
		try
		{
			String sql = """
							SELECT *
							FROM Utente
							WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, utenteMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dell'utente per ID: " + id + e.getMessage(), e);
		}

	}

	public Optional<Utente> findByEmail(String email) throws SQLException
	{

		try
		{
			String sql = """
							SELECT *
							FROM Utente
							WHERE Email = ?
							""";

			return _contex.eseguiSelectSingolo(sql, utenteMapper, email);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dell'utente per email: " + email + e.getMessage(), e);
		}
	}

	public int insert(Connection conn, Utente u) throws SQLException
	{
		String sql = """
						INSERT INTO Utente(Email,Password,Data_Iscrizione,Account_attivo,Ruolo_ID)
						VALUES (?,?,?,?,?);
						""";

		int ID = -1;

		try
		{
			int RuoloId = u.getRuoloId();
			String passwordhash = PasswordUtils.hashPassword(u.getPassword());
			LocalDateTime dataIscrizione = LocalDateTime.now();

			ID = _contex.eseguiUpdate(sql, conn, u.getEmail(), passwordhash, dataIscrizione, u.isAccountAttivo(), RuoloId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'inserimento dell'utente: " + e.getMessage(), e);
		}

		return ID;
	}

	public void updatePassword(int id, String password) throws SQLException
	{
		String sql = """
								UPDATE Utente
								SET Password = ?
								WHERE ID = ?;
						""";
		try
		{
			String passwordHash = PasswordUtils.hashPassword(password);
			_contex.eseguiUpdate(sql, passwordHash, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento della password utente: " + e.getMessage(), e);
		}
	}

	public void setAccountAttivo(int id, boolean attivo) throws SQLException
	{
		String sql = """
								UPDATE Utente
								SET Account_attivo = ?
								WHERE ID = ?;
						""";
		try
		{
			_contex.eseguiUpdate(sql, attivo, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento dello stato dell'account utente: " + e.getMessage(), e);
		}
	}

	public void getCompleto(Utente u) throws SQLException
	{
		int ruoloId = u.getRuoloId();
		if (ruoloId > 0)
		{
			Ruolo r = ruoloDAO.findById(ruoloId).orElseThrow(() -> new SQLException("Ruolo non trovato per ID: " + ruoloId));
			u.setRuolo(r);
		}
	}

	private final ResultMapper<Utente> utenteMapper = row ->
	{
		Utente u = new Utente();

		int ruoloId = MapRow.getInt(row, "Ruolo_ID");

		u.setId(MapRow.getInt(row, "ID"));
		u.setEmail(MapRow.getString(row, "Email"));
		u.setPassword(MapRow.getString(row, "Password"));
		u.setDataIscrizione(MapRow.getLocalDate(row, "Data_Iscrizione"));
		u.setAccountAttivo(MapRow.getBoolean(row, "Account_attivo"));
		u.setRuoloId(ruoloId);

		return u;
	};
}
