package it.mediclick.controller.admin;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	private AmministrazioneService amministrazioneService;
	private MedicoService medicoService;

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

			String mimeType = c.getMimeType() != null ? c.getMimeType() : "application/octet-stream";
			response.setContentType(mimeType);
			response.setContentLength(c.getDatiDocumento().length);

			String nomeFileCodificato = java.net.URLEncoder.encode(c.getNomeFile(), "UTF-8").replaceAll("\\+", "%20");

			String disposizione = "inline";

			if (!mimeType.equals("application/pdf") && !mimeType.startsWith("image/"))
			{
				disposizione = "attachment";
			}

			response.setHeader("Content-Disposition", disposizione + "; filename*=UTF-8''" + nomeFileCodificato);

			response.getOutputStream().write(c.getDatiDocumento());
			response.getOutputStream().flush();
		}
		catch (MedicoException e)
		{
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Impossibile recuperare il certificato");
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		doGet(request, response);
	}

}
