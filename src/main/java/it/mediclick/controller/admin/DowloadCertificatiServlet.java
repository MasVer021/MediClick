package it.mediclick.controller.admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.model.bean.Certificato;
import it.mediclick.service.AmministrazioneService;
import it.mediclick.service.MedicoService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/admin/downloadCertificato")
public class DowloadCertificatiServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	AmministrazioneService amministrazioneService;
	MedicoService medicoService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		amministrazioneService = new AmministrazioneService(contex);
		medicoService = new MedicoService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			int certificatoId = ValidationUtils.parseInt(request.getParameter("id"), -1);
			if (certificatoId <= 0)
			{
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID certificato non valido");
				return;
			}

			Certificato c = medicoService.findCertificatoById(certificatoId);

			if (c == null || c.getDatiDocumento() == null)
			{
				response.sendError(HttpServletResponse.SC_NOT_FOUND, "Certificato non trovato o privo di contenuto");
				return;
			}

			response.setContentType(c.getMimeType() != null ? c.getMimeType() : "application/octet-stream");
			response.setContentLength(c.getDatiDocumento().length);
			response.setHeader("Content-Disposition", "inline; filename=\"" + c.getNomeFile() + "\"");

			response.getOutputStream().write(c.getDatiDocumento());
			response.getOutputStream().flush();
		}
		catch (MedicoException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		doGet(request, response);
	}

}
