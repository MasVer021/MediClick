package it.mediclick.controller.admin;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.mediclick.exception.AmministratoreException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.model.DTO.StatistichePiattaformaDTO;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.service.AmministrazioneService;
import it.mediclick.service.MedicoService;
import it.mediclick.util.Contex;

@WebServlet("/admin/dashboard")
public class DashboardAdminServlet extends HttpServlet
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
			List<Medico> mediciInAttesa = amministrazioneService.getMediciInAttesa();

			Map<Integer, List<Certificato>> certificatiMedico = new HashMap<>();

			for (Medico m : mediciInAttesa)
			{
				List<Certificato> certificati = medicoService.findAllCertificatiByMedicoId(m.getId());

				certificatiMedico.put(m.getId(), certificati);
			}

			StatistichePiattaformaDTO stats = amministrazioneService.getStatistichePiattaforma();
			List<TipoCertificato> tipiCertificato = medicoService.findAllTipoCertificato();

			request.setAttribute("stats", stats);
			request.setAttribute("tipiCertificato", tipiCertificato);
			request.setAttribute("mediciInAttesa", mediciInAttesa);
			request.setAttribute("certificatiMedico", certificatiMedico);

			HttpSession session = request.getSession(false);

			if (session != null && session.getAttribute("errore") != null)
			{
				request.setAttribute("errore", session.getAttribute("errore"));
				session.removeAttribute("errore");
			}

			request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);
		}
		catch (AmministratoreException | MedicoException e)
		{

			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/admin/dashboard.jsp").forward(request, response);
		}
	}
}
