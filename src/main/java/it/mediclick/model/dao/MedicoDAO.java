package it.mediclick.model.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.model.bean.Utente;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class MedicoDAO
{
	private final Contex _contex;
	private final UtenteDAO utenteDAO;

	public MedicoDAO(Contex contex)
	{
		_contex = contex;
		utenteDAO = new UtenteDAO(_contex);
		new CatalogoPrestazioniDAO(_contex);
	}

	public Optional<Medico> findById(int id) throws SQLException
	{
		try
		{

			String sql = """
							   SELECT *
							   FROM Medico
							   WHERE ID = ?
							""";
			return _contex.eseguiSelectSingolo(sql, medicoMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca del medico con ID " + id, e);
		}
	}

	public Optional<RegimeFiscale> findRegimeFiscaleById(int id) throws SQLException
	{
		try
		{
			String sql = """
							SELECT *
							FROM regime_fiscale
							WHERE ID = ?
							""";
			return _contex.eseguiSelectSingolo(sql, regimeFiscaleMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca del regime fiscale by id: " + e.getMessage(), e);
		}
	}

	public List<RegimeFiscale> findAllRegimeFiscale() throws SQLException
	{
		try
		{
			String sql = """
							SELECT *
							FROM regime_fiscale
							""";
			return _contex.eseguiSelect(sql, regimeFiscaleMapper);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dei regimi fiscali  " + e.getMessage(), e);
		}
	}

	/*
	 * public List<Medico> findAllAttivi(String query, Integer categoriaId, String
	 * citta) throws SQLException { StringBuilder sql = new
	 * StringBuilder("SELECT DISTINCT M.* FROM Medico M ");
	 * 
	 * if (categoriaId != null) {
	 * sql.append("JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID "); sql.
	 * append("JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID ");
	 * }
	 * 
	 * if (citta != null && !citta.isEmpty()) { if (categoriaId == null) {
	 * sql.append("JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID "); }
	 * sql.append("JOIN Studio S ON EP.Studio_ID = S.ID "); }
	 * 
	 * sql.append("WHERE M.Stato_verifica = 'Approvato' ");
	 * 
	 * List<Object> params = new ArrayList<>();
	 * 
	 * if (query != null && !query.isEmpty()) {
	 * sql.append("AND (M.Cognome LIKE ? OR M.Nome LIKE ?) "); params.add("%" +
	 * query + "%"); params.add("%" + query + "%"); }
	 * 
	 * if (categoriaId != null) { sql.append("AND CP.Categoria_ID = ? ");
	 * params.add(categoriaId); }
	 * 
	 * if (citta != null && !citta.isEmpty()) { sql.append("AND S.Citta like ? ");
	 * params.add("%" + citta + "%"); }
	 * 
	 * sql.append("ORDER BY M.Cognome, M.Nome");
	 * 
	 * try { return _contex.eseguiSelect(sql.toString(), medicoMapper,
	 * params.toArray()); } catch (SQLException e) { throw new
	 * SQLException("Errore nella ricerca filtrata dei medici: " + e.getMessage(),
	 * e); } }
	 */

	/*
	 * public List<Medico> findAllAttivi() throws SQLException { return
	 * findAllAttivi(null, null, null); }
	 */

	public List<Map<String, Object>> findMedicoSuggest(String query, String citta) throws SQLException
	{
		String sql = """
						SELECT DISTINCT M.cognome, M.nome
						FROM Studio S
						JOIN ErogazionePrestazione EP ON S.ID = EP.Studio_ID
						JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID
						JOIN medico M on M.ID = ep.Medico_ID
						where (CONCAT( M.Nome, ' ', M.Cognome) LIKE ?
						OR CONCAT( M.Cognome, ' ', M.Nome) LIKE ?)
						AND s.Citta like ?
						AND M.Stato_verifica = 'Approvato'
						group by  M.cognome, M.nome
						ORDER BY Cognome , nome
						LIMIT 5
						""";

		query = "%".concat(query).concat("%");
		citta = "%".concat(citta).concat("%");

		try
		{
			return _contex.eseguiSelect(sql.toString(), query, query, citta);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca filtrata dei medici: " + e.getMessage(), e);
		}
	}

	public List<Map<String, Object>> findCittaSuggest(String query, String citta) throws SQLException
	{
		String sql = """
						SELECT DISTINCT S.Citta
						FROM Studio S
						JOIN ErogazionePrestazione EP ON S.ID = EP.Studio_ID
						JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID
						JOIN medico M on M.ID = ep.Medico_ID
						where (CONCAT( M.Nome, ' ', M.Cognome) LIKE ?
						OR CONCAT( M.Cognome, ' ', M.Nome) LIKE ?)
						AND s.Citta like ?
						AND M.Stato_verifica = 'Approvato'
						group by S.Citta
						ORDER BY Citta
						LIMIT 5
						""";

		query = "%".concat(query).concat("%");
		citta = "%".concat(citta).concat("%");

		try
		{
			return _contex.eseguiSelect(sql.toString(), query, query, citta);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca filtrata dei medici: " + e.getMessage(), e);
		}
	}

	public List<Medico> findAll() throws SQLException
	{
		try
		{
			String sql = """
							SELECT *
							FROM Medico
							""";
			return _contex.eseguiSelect(sql, medicoMapper);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dei medici  " + e.getMessage(), e);
		}
	}

	public List<MedicoCardDTO> findCards(Integer categoriaId, String citta, String... query) throws SQLException
	{
		StringBuilder sql = new StringBuilder("""
												     SELECT
												         M.*,
												         MIN(C.ID) as Cat_ID,
												         MIN(C.Nome) as Categoria_Nome,
												 		MIN(S.Indirizzo_Maps) as Indirizzo_Studio,
												         MIN(S.Citta) as Citta_Studio,
												 		MIN(EP.Prezzo_Lordo_Listino) as Costo,
												         (
												 		    SELECT COALESCE(AVG(R.Voto), 0) FROM Recensione R
												    JOIN Prenotazione P ON R.Prenotazione_ID = P.ID
												    JOIN Disponibilita D ON P.Disponibilita_ID = D.ID
												    WHERE D.Medico_ID = M.ID
												) as Media_Recensioni,
												         (
												             SELECT COUNT(*) FROM Recensione R
												             JOIN Prenotazione P ON R.Prenotazione_ID = P.ID
												             JOIN Disponibilita D ON P.Disponibilita_ID = D.ID
												             WHERE D.Medico_ID = M.ID
												         ) as Num_Recensioni,
												         (
												             SELECT MIN(Data_Ora_Inizio)
												             FROM Disponibilita D
												             WHERE D.Medico_ID = M.ID AND D.Stato = 'Disponibile'
												             AND D.Data_Ora_Inizio > NOW()
												         ) as Prima_Disponibilita
												     FROM Medico M
												     JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID
												     JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID
												     JOIN Categoria C ON CP.Categoria_ID = C.ID
												     JOIN Studio S ON EP.Studio_ID = S.ID
												     WHERE M.Stato_verifica = 'Approvato'
												 """);

		List<Object> params = new ArrayList<>();

		if (query != null)
		{
			for (String q : query)
			{
				sql.append("AND CONCAT( M.Nome, ' ', M.Cognome) LIKE ?");
				params.add("%" + q + "%");

			}

		}

		if (categoriaId != null && categoriaId > 0)
		{
			sql.append("AND CP.Categoria_ID = ? ");
			params.add(categoriaId);
		}

		if (citta != null && !citta.isEmpty())
		{
			sql.append("AND S.Citta = ? ");
			params.add(citta);
		}

		sql.append("GROUP BY M.ID ");

		try
		{
			return _contex.eseguiSelect(sql.toString(), medicoCardMapper, params.toArray());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca delle card dei medici: " + e.getMessage(), e);
		}
	}

	public List<Medico> findByStato(Medico.StatoVerifica stato) throws SQLException
	{
		try
		{

			String sql = """
							   SELECT *
							   FROM Medico
							   WHERE Stato_verifica = ?
							""";

			return _contex.eseguiSelect(sql, medicoMapper, stato.getLabel());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dei medici per stato verifica " + stato.getLabel() + ": " + e.getMessage(), e);
		}
	}

	public int insert(Medico m) throws SQLException
	{
		String sqlMedico = """
							 INSERT INTO Medico(ID,Cognome,Nome,Fotoprofilo,Bio,P_Iva,Stato_verifica,Regime_fiscale)
							 VALUES (?,?,?,?,?,?,?,?)
							""";

		Connection conn = _contex.getConnection();
		conn.setAutoCommit(false);
		try
		{
			Utente u = m.getUtente();

			int utenteId = utenteDAO.insert(conn, u);

			Integer regimeFiscaleId = m.getRegimeFiscaleId() > 0 ? m.getRegimeFiscaleId() : null;

			_contex.eseguiUpdate(sqlMedico, conn, utenteId, m.getCognome(), m.getNome(), m.getFotoprofilo(), m.getBio(), m.getpIva(),
					m.getStatoVerifica() != null ? m.getStatoVerifica().getLabel() : "In attesa", regimeFiscaleId);

			m.setId(utenteId);
			m.getUtente().setId(utenteId);

			conn.commit();
			return utenteId;
		}
		catch (SQLException e)
		{
			conn.rollback();
			throw new SQLException("Errore nell'inserimento del medico: " + e.getMessage(), e);
		}
		finally
		{
			conn.close();
		}
	}

	public void update(Medico m) throws SQLException
	{
		String sql = """
						          UPDATE Medico
						          SET Cognome = ?, Nome = ?, Fotoprofilo = ?, Bio = ?,P_Iva = ?
						          WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, m.getCognome(), m.getNome(), m.getFotoprofilo(), m.getBio(), m.getpIva(), m.getId());
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento del medico: " + e.getMessage(), e);
		}
	}

	public void updatePassword(int id, String password) throws SQLException
	{
		utenteDAO.updatePassword(id, password);
	}

	public void updateStatoVerifica(int id, Medico.StatoVerifica stato) throws SQLException
	{
		String sql = """
						   UPDATE Medico
						   SET Stato_verifica = ?
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, stato.getLabel(), id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento stato verifica medico: " + e.getMessage(), e);
		}
	}

	public void updateRegimeFiscale(int id, int regimeFiscaleId) throws SQLException
	{
		String sql = """
						   UPDATE Medico
						   SET Regime_fiscale = ?
						   WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, regimeFiscaleId, id);
		}
		catch (SQLException e)
		{
			System.err.println("Errore nell'aggiornamento regime fiscale medico: " + e.getMessage());
			throw e;
		}
	}

	public void getCompleto(Medico m) throws SQLException
	{

		int regimeFiscaleId = m.getRegimeFiscaleId();
		int utenteId = m.getId();

		RegimeFiscale r = findRegimeFiscaleById(regimeFiscaleId).orElseThrow(() -> new SQLException("Regime fiscale con ID " + regimeFiscaleId + " non trovato"));
		Utente u = utenteDAO.findById(utenteId).orElseThrow(() -> new SQLException("Utente non trovato per ID: " + utenteId));

		m.setRegimeFiscale(r);
		m.setUtente(u);
	}

	public int countAll() throws SQLException
	{
		String sql = """
						SELECT COUNT(*) as totale
						FROM Medico
						""";
		try
		{
			List<Map<String, Object>> res = _contex.eseguiSelect(sql);
			return res.isEmpty() ? 0 : ((Number) res.get(0).get("totale")).intValue();
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel conteggio di tutti i medici: " + e.getMessage(), e);
		}
	}

	public int countByStatoVerifica(Medico.StatoVerifica stato) throws SQLException
	{
		String sql = """
						SELECT COUNT(*) as totale
						FROM Medico
						WHERE Stato_verifica = ?
						""";
		try
		{
			List<Map<String, Object>> res = _contex.eseguiSelect(sql, stato.getLabel());
			return res.isEmpty() ? 0 : ((Number) res.get(0).get("totale")).intValue();
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nel conteggio dei medici per stato " + stato.getLabel() + ": " + e.getMessage(), e);
		}
	}

	private final ResultMapper<RegimeFiscale> regimeFiscaleMapper = row ->
	{
		RegimeFiscale r = new RegimeFiscale();

		r.setId(MapRow.getInt(row, "ID"));
		r.setNome(MapRow.getString(row, "Nome"));
		r.setDescrizione(MapRow.getString(row, "Descrizione"));
		r.setAliquotaDefault(MapRow.getDouble(row, "Aliquota_Default"));

		return r;
	};

	private final ResultMapper<Medico> medicoMapper = row ->
	{
		Medico m = new Medico();

		Integer regimeFiscaleId = MapRow.getIntOrNull(row, "Regime_fiscale") != null ? MapRow.getInt(row, "Regime_fiscale") : -1;

		m.setId(MapRow.getInt(row, "ID"));
		m.setCognome(MapRow.getString(row, "Cognome"));
		m.setNome(MapRow.getString(row, "Nome"));

		Object fotoprofiloStr = row.get("Fotoprofilo");

		if (fotoprofiloStr instanceof byte[])
		{
			m.setFotoprofilo((byte[]) fotoprofiloStr);
		}
		else
		{
			m.setFotoprofilo(null);
		}

		m.setBio(MapRow.getString(row, "Bio"));
		m.setpIva(MapRow.getString(row, "P_Iva"));
		m.setStatoVerifica(Medico.StatoVerifica.fromString(MapRow.getString(row, "Stato_verifica")));

		m.setRegimeFiscaleId(regimeFiscaleId);

		return m;

	};

	private final ResultMapper<MedicoCardDTO> medicoCardMapper = row ->
	{
		MedicoCardDTO dto = new MedicoCardDTO();

		Medico m = medicoMapper.map(row);

		int idCategoria = MapRow.getIntOrNull(row, "Cat_ID");

		Categoria c = new Categoria();
		c.setId(idCategoria);
		c.setNome(MapRow.getString(row, "Categoria_Nome"));

		dto.setCategoria(c);
		dto.setMedico(m);

		dto.setCosto(MapRow.getDouble(row, "Costo"));
		dto.setIndirizzo(MapRow.getString(row, "Indirizzo_Studio"));
		dto.setNumeroRecensioni(MapRow.getInt(row, "Num_Recensioni"));
		dto.setPrimaDisponibilita(MapRow.getLocalDateTime(row, "Prima_Disponibilita"));
		dto.setValoreRecensioni(MapRow.getDouble(row, "Media_Recensioni"));

		return dto;

	};
}
