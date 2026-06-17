package it.mediclick.controller.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.AmministratoreException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.model.bean.Prenotazione;
import it.mediclick.service.AmministrazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/admin/prenotazioni")
public class PrenotazioniAdminServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private AmministrazioneService amministrazioneService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		amministrazioneService = new AmministrazioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String codiceFiscale = ValidationUtils.parseStringOpz(request.getParameter("codiceFiscale"), "");
			String dataInizio = ValidationUtils.parseStringOpz(request.getParameter("dataInizio"), "");
			String dataFine = ValidationUtils.parseStringOpz(request.getParameter("dataFine"), "");

			List<Prenotazione> prenotazioni = amministrazioneService.findPrenotazioniPiattaforma(codiceFiscale, dataInizio, dataFine);

			request.setAttribute("prenotazioni", prenotazioni);
			request.setAttribute("codiceFiscaleFiltro", codiceFiscale);
			request.setAttribute("dataInizioFiltro", dataInizio);
			request.setAttribute("dataFineFiltro", dataFine);

			request.getRequestDispatcher("/WEB-INF/view/admin/prenotazioni.jsp").forward(request, response);
		}
		catch (AmministratoreException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/admin/prenotazioni.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
}