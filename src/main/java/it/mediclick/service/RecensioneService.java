package it.mediclick.service;

import java.sql.SQLException;
import java.util.List;

import it.mediclick.exception.RecensioneException;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.RecensioneDAO;
import it.mediclick.util.Contex;

public class RecensioneService
{

	private RecensioneDAO recensioneDAO;
	private PrenotazioneDAO prenotazioneDAO;
	private Contex _contex;

	public RecensioneService(Contex contex)
	{
		this._contex = contex;
		this.recensioneDAO = new RecensioneDAO(_contex);
		this.prenotazioneDAO = new PrenotazioneDAO(_contex);
	}

	public Prenotazione getPrenotazionePerRecensione(int prenotazioneId, int pazienteId) throws RecensioneException
	{
		try
		{
			Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new RecensioneException("Prenotazione non trovata.", "RECENSIONE_NON_TROVATA"));

			if (p.getPazienteId() != pazienteId)
			{
				throw new RecensioneException("Non hai i permessi per recensire questa prenotazione.", "AUTH_ACCESSO_NEGATO");
			}

			if (p.getStato() != Prenotazione.Stato.COMPLETATA && p.getStato() != Prenotazione.Stato.CONFERMATA)
			{
				throw new RecensioneException("Impossibile recensire una visita non ancora completata.", "RECENSIONE_STATO_INVALIDO");
			}

			if (p.isFutura())
			{
				throw new RecensioneException("Impossibile recensire una visita non ancora avvenuta.", "RECENSIONE_STATO_INVALIDO");
			}

			ErogazionePrestazioneDAO erogazionePrestazioneDAO = new ErogazionePrestazioneDAO(_contex);

			prenotazioneDAO.getCompleto(p);
			erogazionePrestazioneDAO.getCompleto(p.getErogazionePrestazione());

			return p;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RecensioneException("Errore del sistema nel recupero della prenotazione.", "SYS_DATABASE_ERROR");
		}
	}

	public boolean lasciaRecensione(int prenotazioneId, int voto, String commento) throws RecensioneException
	{
		try
		{
			if (voto < 1 || voto > 5)
			{
				throw new RecensioneException("Il voto inserito deve essere compreso tra 1 e 5.", "RECENSIONE_VOTO_INVALIDO");
			}

			Prenotazione p = prenotazioneDAO.findById(prenotazioneId).orElseThrow(() -> new RecensioneException("Prenotazione non trovata.", "RECENSIONE_NON_TROVATA"));

			Recensione r = new Recensione();
			r.setPrenotazioneId(prenotazioneId);
			r.setVoto(voto);
			r.setCommento(commento);
			r.setVisible(true);

			recensioneDAO.insert(r);
			return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RecensioneException("Errore del sistema durante il salvataggio della recensione.", "SYS_DATABASE_ERROR");
		}

	}

	public Recensione findByIdPrenotazione(int prenotazionId) throws RecensioneException
	{
		try
		{
			return recensioneDAO.findByPrenotazione(prenotazionId).orElse(null);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RecensioneException("Errore del sistema nel recupero della recensione.", "SYS_DATABASE_ERROR");
		}
	}

	public List<Recensione> getRecensioniMedico(int medicoId) throws RecensioneException
	{
		try
		{
			return recensioneDAO.findByMedico(medicoId);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RecensioneException("Errore del sistema nel recupero delle recensioni del medico.", "SYS_DATABASE_ERROR");
		}
	}

	public void moderaRecensione(int recensioneId, boolean visibile) throws RecensioneException
	{
		try
		{
			recensioneDAO.setVisibile(recensioneId, visibile);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new RecensioneException("Errore del sistema durante la moderazione della recensione.", "SYS_DATABASE_ERROR");
		}
	}
}
