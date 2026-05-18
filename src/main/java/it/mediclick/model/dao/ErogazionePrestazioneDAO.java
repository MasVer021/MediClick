package it.mediclick.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Studio;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class ErogazionePrestazioneDAO
{
	private final Contex _contex;
	private final MedicoDAO medicoDAO;
	private final StudioDAO studioDAO;
	private final CatalogoPrestazioniDAO catalogoPrestazioniDAO;

	public ErogazionePrestazioneDAO(Contex contex)
	{
		_contex = contex;
		medicoDAO = new MedicoDAO(contex);
		studioDAO = new StudioDAO(contex);
		catalogoPrestazioniDAO = new CatalogoPrestazioniDAO(contex);
	}

	public Optional<ErogazionePrestazione> findById(int id) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM ErogazionePrestazione
							   WHERE ID = ?
							""";

			return _contex.eseguiSelectSingolo(sql, erogazionePrestazioneMapper, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dell'erogazione prestazione con ID " + id, e);
		}
	}

	public List<ErogazionePrestazione> findByMedico(int medicoId) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM ErogazionePrestazione
							   WHERE Medico_ID = ?
							""";

			return _contex.eseguiSelect(sql, erogazionePrestazioneMapper, medicoId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dell'erogazione prestazione del medico con ID " + medicoId, e);
		}
	}

	public List<ErogazionePrestazione> findByMedicoEStudio(int medicoId, int studioId) throws SQLException
	{
		try
		{
			String sql = """
							   SELECT *
							   FROM ErogazionePrestazione
							   WHERE Medico_ID = ? AND Studio_ID = ?
							""";

			return _contex.eseguiSelect(sql, erogazionePrestazioneMapper, medicoId, studioId);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nella ricerca dell'erogazione prestazione del medico con ID " + medicoId + " e studio con ID " + studioId, e);
		}
	}

	public void insert(ErogazionePrestazione ep) throws SQLException
	{
		String sql = """
						INSERT INTO ErogazionePrestazione(Medico_ID, CatalogoPrestazioni_ID, Studio_ID, Prezzo_Lordo_Listino, Durata, Stato)
						VALUES (?,?,?,?,?,?)
						""";
		try
		{
			Integer medicoId = ep.getMedicoId() > 0 ? ep.getMedicoId() : null;
			Integer catalogoPrestazioniId = ep.getCatalogoPrestazioniId() > 0 ? ep.getCatalogoPrestazioniId() : null;
			Integer studioId = ep.getStudioId() > 0 ? ep.getStudioId() : null;

			Double prezzoLordoListino = ep.getPrezzoLordoListino();
			Integer durata = ep.getDurata();
			String stato = ep.getStato().getLabel();

			_contex.eseguiUpdate(sql, medicoId, catalogoPrestazioniId, studioId, prezzoLordoListino, durata, stato);
		}
		catch (NullPointerException ne)
		{
			throw new SQLException("Errore nell'inserimento dell'erogazione prestazione campi non presenti: " + ne.getMessage(), ne);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'inserimento dell'erogazione prestazione: " + e.getMessage(), e);
		}
	}

	public void updateStato(int id, ErogazionePrestazione.Stato stato) throws SQLException
	{
		String sql = """
						UPDATE ErogazionePrestazione
						SET Stato = ?
						WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, stato.getLabel(), id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento stato erogazione prestazione: " + e.getMessage(), e);
		}
	}

	public void updatePrezzo(int id, Double prezzo) throws SQLException
	{
		String sql = """
						UPDATE ErogazionePrestazione
						SET Prezzo_Lordo_Listino = ?
						WHERE ID = ?
						""";
		try
		{
			_contex.eseguiUpdate(sql, prezzo, id);
		}
		catch (SQLException e)
		{
			throw new SQLException("Errore nell'aggiornamento prezzo erogazione prestazione: " + e.getMessage(), e);
		}
	}

	public void getCompleto(ErogazionePrestazione ep) throws SQLException
	{
		int medicoId = ep.getMedicoId();
		int studioId = ep.getStudioId();
		int catalogoPrestazioniId = ep.getCatalogoPrestazioniId();

		Medico m = medicoDAO.findById(medicoId).orElseThrow(() -> new SQLException("Medico non trovato per erogazione prestazione id: " + ep.getId()));
		Studio s = studioDAO.findById(studioId).orElseThrow(() -> new SQLException("Studio non trovato per erogazione prestazione id: " + ep.getId()));
		CatalogoPrestazioni c = catalogoPrestazioniDAO.findById(catalogoPrestazioniId)
				.orElseThrow(() -> new SQLException("Catalogo prestazioni non trovato per erogazione prestazione id: " + ep.getId()));

		ep.setCatalogoPrestazioni(c);
		ep.setMedico(m);
		ep.setStudio(s);
	}

	private final ResultMapper<ErogazionePrestazione> erogazionePrestazioneMapper = row ->
	{
		ErogazionePrestazione ep = new ErogazionePrestazione();

		int prestazioneId = MapRow.getInt(row, "ID");

		Integer medicoId = MapRow.getIntOrNull(row, "Medico_ID");
		medicoId = medicoId != null ? medicoId : -1;

		Integer studioId = MapRow.getIntOrNull(row, "Studio_ID");
		studioId = studioId != null ? studioId : -1;

		Integer catalogoPrestazioniId = MapRow.getIntOrNull(row, "CatalogoPrestazioni_ID");
		catalogoPrestazioniId = catalogoPrestazioniId != null ? catalogoPrestazioniId : -1;

		Double prezzo = MapRow.getDouble(row, "Prezzo_Lordo_Listino");

		Integer durata = MapRow.getIntOrNull(row, "Durata");
		durata = (durata != null) ? durata : 30;

		ep.setId(prestazioneId);

		ep.setPrezzoLordoListino(prezzo);

		ep.setDurata(durata);
		ep.setStato(ErogazionePrestazione.Stato.fromString(MapRow.getString(row, "Stato")));

		ep.setCatalogoPrestazioniId(catalogoPrestazioniId);
		ep.setMedicoId(medicoId);
		ep.setStudioId(studioId);

		return ep;
	};
}
