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
import it.mediclick.model.bean.ImpostazioniSistema;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AmministrazioneService;
import it.mediclick.util.Contex;

@WebServlet("/admin/impostazioni")
public class ImpostazioniServlet extends HttpServlet
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
			List<ImpostazioniSistema> impostazioni = amministrazioneService.getTutteLeImpostazioni();
			request.setAttribute("impostazioni", impostazioni);

			if (request.getSession().getAttribute("successo") != null)
			{
				request.setAttribute("successo", request.getSession().getAttribute("successo"));
				request.getSession().removeAttribute("successo");
			}
			if (request.getSession().getAttribute("errore") != null)
			{
				request.setAttribute("errore", request.getSession().getAttribute("errore"));
				request.getSession().removeAttribute("errore");
			}

			request.getRequestDispatcher("/WEB-INF/view/admin/impostazioni.jsp").forward(request, response);
		}
		catch (AmministratoreException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/admin/impostazioni.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			String chiave = request.getParameter("chiave");
			String valore = request.getParameter("valore");

			if (chiave == null || chiave.isBlank() || valore == null || valore.isBlank())
			{
				throw new AmministratoreException("I campi Chiave e Valore sono obbligatori.", "PARAM_ERROR");
			}

			Utente admin = (Utente) request.getSession(false).getAttribute("utente");
			int adminId = admin.getId();

			amministrazioneService.aggiornaImpostazione(chiave.trim(), valore.trim(), adminId);

			request.getSession().setAttribute("successo", "Impostazione '" + chiave + "' aggiornata con successo!");
			response.sendRedirect(request.getContextPath() + "/admin/impostazioni");
		}
		catch (AmministratoreException e)
		{
			request.getSession().setAttribute("errore", new ErrorInfo(e));
			response.sendRedirect(request.getContextPath() + "/admin/impostazioni");
		}
	}
}
