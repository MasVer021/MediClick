package it.mediclick.controller.paziente;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.PazienteException;
import it.mediclick.exception.RecensioneException;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.PazienteService;
import it.mediclick.service.RecensioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/paziente/recensione")
public class RecensioneServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private PazienteService pazienteService;
	private RecensioneService recensioneService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		pazienteService = new PazienteService(contex);
		recensioneService = new RecensioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			try
			{
				Paziente p = getPazienteConnesso(request, response);
				int prenotazioneId = ValidationUtils.parseInt(request.getParameter("prenotazioneId"), "id prenotazione");

				Prenotazione prenotazione = recensioneService.getPrenotazionePerRecensione(prenotazioneId, p.getId());

				Recensione recensione = recensioneService.findByIdPrenotazione(prenotazioneId);

				request.setAttribute("recensione", recensione);
				request.setAttribute("prenotazione", prenotazione);
				request.getRequestDispatcher("/WEB-INF/view/paziente/recensione.jsp").forward(request, response);
			}
			catch (IllegalArgumentException | PazienteException e)
			{
				throw new RecensioneException(e.getMessage(), "REC_IDPREN_ERROR");
			}
		}
		catch (RecensioneException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/paziente/prenotazioni.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			try
			{
				int prenotazioneId = ValidationUtils.parseInt(request.getParameter("prenotazioneId"), "id prenotazione");

				if (recensioneService.findByIdPrenotazione(prenotazioneId) != null)
				{
					throw new RecensioneException("Hai già recensito questa visita", "REC_DUPLICATA");
				}

				Paziente p = getPazienteConnesso(request, response);

				Prenotazione prenotazione = recensioneService.getPrenotazionePerRecensione(prenotazioneId, p.getId());

				int voto = ValidationUtils.parseInt(request.getParameter("voto"), "voto");

				String commento = ValidationUtils.parseStringOpz(request.getParameter("commento"), "Nessun commento");

				if (voto < 1 || voto > 5)
				{
					throw new RecensioneException("Il voto può essere compreso tra 1 e 5", "ILLEGAL_VOTO_FORMAT");
				}

				recensioneService.lasciaRecensione(prenotazioneId, voto, commento);

				response.sendRedirect(request.getContextPath() + "/paziente/recensione?prenotazioneId=" + prenotazioneId);
			}
			catch (IllegalArgumentException | PazienteException e)
			{
				throw new RecensioneException(e.getMessage(), "REC_IDPREN_ERROR");
			}
		}
		catch (RecensioneException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			doGet(request, response);
		}
	}

	private Paziente getPazienteConnesso(HttpServletRequest request, HttpServletResponse response) throws PazienteException
	{
		Utente u = (Utente) request.getSession(false).getAttribute("utente");

		if (u == null)
		{
			throw new PazienteException("Errore nel recupero delL'utente", "UTENTE_ERROR");
		}

		Paziente p = pazienteService.findById(u.getId());
		p.setUtente(u);
		return p;
	}

}
