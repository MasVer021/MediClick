package it.mediclick.controller.medico;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.MedicoService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/medico/certificati")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
		maxFileSize = 1024 * 1024 * 2, // 2 MB max
		maxRequestSize = 1024 * 1024 * 10 // 10 MB max
)

public class CertificatiServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	MedicoService medicoService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		medicoService = new MedicoService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Medico m = getMedicoConnesso(request, response);

			List<Certificato> certificati = medicoService.findAllCertificatiByMedicoId(m.getId());

			List<TipoCertificato> tipiCertificato = medicoService.findAllTipoCertificato();

			request.setAttribute("certificatiCaricati", certificati);
			request.setAttribute("tipiCertificato", tipiCertificato);
			request.getRequestDispatcher("/WEB-INF/view/medico/certificati.jsp").forward(request, response);
		}
		catch (MedicoException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/medico/certificati.jsp").forward(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Medico m = getMedicoConnesso(request, response);

			String azione = ValidationUtils.parseStringOpz(request.getParameter("action"), "NoAction");
			int certificatoId = ValidationUtils.parseInt(request.getParameter("certificatoId"), -1);
			int tipoCertificatoId = ValidationUtils.parseInt(request.getParameter("tipoCertificatoId"), -1);

			switch (azione)
			{
				case "elimina-certificato":
					if (certificatoId > 0)
					{
						medicoService.eliminaCertificato(certificatoId, m.getId());
					}
					break;
				case "carica-certificato":
					try
					{
						Part certificatoPart = request.getPart("documento");

						LocalDate scadenzaDate = ValidationUtils.parseLocalDateOpz(request.getParameter("dataScadenza"), null);
						LocalDateTime dataScadenza = (scadenzaDate != null) ? scadenzaDate.atStartOfDay() : null;

						if (certificatoPart != null && certificatoPart.getSize() > 0)
						{
							byte[] fotoBytes = certificatoPart.getInputStream().readAllBytes();

							medicoService.caricaCertificato(m.getId(), tipoCertificatoId, certificatoPart.getSubmittedFileName(), fotoBytes, certificatoPart.getContentType(), dataScadenza);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();

						Throwable causa = e;
						while (causa.getCause() != null)
						{
							causa = causa.getCause();
						}

						throw new MedicoException("Errore durante il caricamento del certificato: " + causa.getMessage(), "CERT_ERROR");
					}
					break;
			}
			doGet(request, response);

		}
		catch (MedicoException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			doGet(request, response);
			return;
		}
	}

	private Medico getMedicoConnesso(HttpServletRequest request, HttpServletResponse response) throws MedicoException
	{
		Utente u = (Utente) request.getSession(false).getAttribute("utente");

		if (u == null)
		{
			throw new MedicoException("Errore nel recupero dell'utente", "UTENTE_ERROR");
		}

		Medico m = medicoService.findById(u.getId());
		m.setUtente(u);
		return m;
	}

}
