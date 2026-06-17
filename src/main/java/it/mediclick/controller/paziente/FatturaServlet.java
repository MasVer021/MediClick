package it.mediclick.controller.paziente;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.PazienteException;
import it.mediclick.exception.PrenotazioneException;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.PazienteService;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/paziente/fattura")
public class FatturaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private PrenotazioneService prenotazioneService;
	private PazienteService pazienteService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		prenotazioneService = new PrenotazioneService(contex);
		pazienteService = new PazienteService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		int prenotazioneId = ValidationUtils.parseInt(request.getParameter("prenotazioneId"), "ID prenotazione");

		try
		{
			Paziente p = getPazienteConnesso(request, response);
			Prenotazione prenotazione = prenotazioneService.getPrenotazionePaziente(p.getId(), prenotazioneId);
			request.setAttribute("prenotazione", prenotazione);

			request.getRequestDispatcher("/WEB-INF/view/paziente/fattura.jsp").forward(request, response);
			return;
		}
		catch (PrenotazioneException | PazienteException e)
		{
			request.getSession().setAttribute("errore", new ErrorInfo(e));
			response.sendRedirect(request.getContextPath() + "/paziente/prenotazioni");
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
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
