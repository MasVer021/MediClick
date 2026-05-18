package it.mediclick.controller.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.AmministratoreException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AmministrazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/admin/approvaMedico")
public class ApprovazioneMediciServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	AmministrazioneService amministrazioneService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		amministrazioneService = new AmministrazioneService(contex);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			try
			{

				boolean approva = ValidationUtils.parseBoolean(request.getParameter("approvato"), "approvato");

				int certificatoId = ValidationUtils.parseInt(request.getParameter("certificatoId"), -1);

				if (certificatoId > 0)
				{

					Utente admin = (Utente) request.getSession(false).getAttribute("utente");
					int adminId = admin.getId();

					amministrazioneService.gestisciCertificato(certificatoId, approva, adminId);
				}
				else
				{
					int medicoId = ValidationUtils.parseInt(request.getParameter("medicoId"), "Id medico");

					amministrazioneService.approvaMedico(medicoId, approva);
				}
			}
			catch (IllegalArgumentException e)
			{
				throw new AmministratoreException(e.getMessage(), "PARAMETRI_NON_VALIDI");
			}

			response.sendRedirect(request.getContextPath() + "/admin/dashboard");
		}
		catch (AmministratoreException e)
		{
			request.getSession().setAttribute("errore", new ErrorInfo(e));
			response.sendRedirect(request.getContextPath() + "/admin/dashboard");
		}
	}
}
