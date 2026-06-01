package it.mediclick.controller.paziente;

import java.io.IOException;
import java.util.List;

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

@WebServlet("/paziente/prenotazioni")
public class PrenotazioniEffettuateServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	PazienteService pazienteService;
	PrenotazioneService prenotazioniService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		pazienteService = new PazienteService(contex);
		prenotazioniService = new PrenotazioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Paziente p = getPazienteConnesso(request, response);
			try
			{
				List<Prenotazione> prenotazioni = prenotazioniService.getPrenotazioniPaziente(p.getId(), false);

				int numeroPrenotazioni = (int) prenotazioni.stream().filter(pre -> !pre.getStato().equals(Prenotazione.Stato.CANCELLATA)).count();

				double spesaTotale = (double) prenotazioni.stream().filter(pre -> !pre.getStato().equals(Prenotazione.Stato.CANCELLATA)).mapToDouble(Prenotazione::getImportoPagato).sum();

				int visiteDaEffettuare = (int) prenotazioni.stream().filter(pre -> !pre.getStato().equals(Prenotazione.Stato.CANCELLATA) && pre.isFutura()).count();

				request.setAttribute("numeroPrenotazioni", numeroPrenotazioni);
				request.setAttribute("spesaTotale", spesaTotale);
				request.setAttribute("visiteDaEffettuare", visiteDaEffettuare);
				request.setAttribute("prenotazioni", prenotazioni);
			}
			catch (Exception e)
			{
				throw new PazienteException(e.getMessage(), "PRENOTAZIONE_ERROR");
			}

		}
		catch (PazienteException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
		}

		request.getRequestDispatcher("/WEB-INF/view/paziente/prenotazioni.jsp").forward(request, response);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Paziente p = getPazienteConnesso(request, response);

			String action = request.getParameter("action");
			if ("disdici".equals(action))
			{
				try
				{
					int prenotazioneId = ValidationUtils.parseInt(request.getParameter("prenotazioneId"), "ID Prenotazione");

					prenotazioniService.disdiciPrenotazione(prenotazioneId, p.getUtente().getId());

					request.getSession().setAttribute("successo", "Prenotazione disdetta con successo!");
					response.sendRedirect(request.getContextPath() + "/paziente/prenotazioni");
					return;
				}
				catch (PrenotazioneException | IllegalArgumentException e)
				{
					throw new PazienteException(e.getMessage(), "DISDETTA_ERROR");
				}
			}

			doGet(request, response);
		}
		catch (PazienteException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
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
