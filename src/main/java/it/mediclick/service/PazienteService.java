package it.mediclick.service;

import java.sql.SQLException;

import it.mediclick.exception.PazienteException;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.dao.PazienteDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;

public class PazienteService
{

	PazienteDAO pazienteDAO;
	UtenteDAO utenteDAO;
	private Contex _contex;

	public PazienteService(Contex contex)
	{
		this._contex = contex;
		pazienteDAO = new PazienteDAO(contex);
		utenteDAO = new UtenteDAO(_contex);
	}

	public Paziente findById(int pazienteId) throws PazienteException
	{
		try
		{
			Paziente p = pazienteDAO.findById(pazienteId).orElseThrow(() -> new PazienteException("Paziente non trovato", "PAZIENTE_NOT_FOUND"));
			pazienteDAO.getCompleto(p);
			return p;
		}
		catch (SQLException e)
		{
			throw new PazienteException("Errore durante il recupero dei dati del paziente: " + e.getMessage(), "PAZIENTE_ERROR");
		}
	}

	public void modificaTelefono(int pazienteId, String numeroTelefono) throws PazienteException
	{
		try
		{
			Paziente p = findById(pazienteId);
			p.setTelefono(numeroTelefono);
			pazienteDAO.update(p);
		}
		catch (SQLException e)
		{
			throw new PazienteException("Errore durante la modifica del numero di telefono: " + e.getMessage(), "PAZIENTE_TEL_MOD_ERROR");
		}
	}

	public void modificaPassword(int pazienteId, String password) throws PazienteException
	{
		try
		{
			pazienteDAO.updatePassword(pazienteId, password);
		}
		catch (SQLException e)
		{
			throw new PazienteException("Errore durante la modifica della password: " + e.getMessage(), "PAZIENTE_PASS_MOD_ERROR");
		}
	}

}
