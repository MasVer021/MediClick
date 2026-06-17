package it.mediclick.service;

import java.sql.SQLException;

import it.mediclick.exception.PazienteException;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.dao.PazienteDAO;
import it.mediclick.util.Contex;

public class PazienteService
{

	private PazienteDAO pazienteDAO;

	public PazienteService(Contex contex)
	{
		pazienteDAO = new PazienteDAO(contex);
	}

	public Paziente findById(int pazienteId) throws PazienteException
	{
		try
		{
			Paziente p = pazienteDAO.findById(pazienteId).orElseThrow(() -> new PazienteException("Profilo paziente non trovato.", "PAZIENTE_NOT_FOUND"));
			pazienteDAO.getCompleto(p);
			return p;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new PazienteException("Errore del sistema nel recupero dei dati del paziente.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new PazienteException("Errore del sistema durante la modifica del numero di telefono.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new PazienteException("Errore del sistema durante la modifica della password.", "SYS_DATABASE_ERROR");
		}
	}

}
