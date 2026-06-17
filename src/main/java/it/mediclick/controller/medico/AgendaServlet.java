package it.mediclick.controller.medico;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.exception.PrenotazioneException;
import it.mediclick.model.DTO.SlotAgendaDTO;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.MedicoService;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/medico/agenda")
public class AgendaServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private MedicoService medicoService;
	private PrenotazioneService prenotazioneService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		medicoService = new MedicoService(contex);
		prenotazioneService = new PrenotazioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Medico m = getMedicoConnesso(request, response);

			LocalDate dataScelta = ValidationUtils.parseLocalDateOpz(request.getParameter("data"), LocalDate.now());

			List<SlotAgendaDTO> agenda = medicoService.getAgendaGiornaliera(m.getId(), dataScelta);

			request.setAttribute("agenda", agenda);
			request.setAttribute("dataMostrata", dataScelta);

			request.getRequestDispatcher("/WEB-INF/view/medico/agenda.jsp").forward(request, response);
		}
		catch (MedicoException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			doGet(request, response);
			return;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			Medico m = getMedicoConnesso(request, response);

			String azione = ValidationUtils.parseStringOpz(request.getParameter("action"), "NoAction");
			int prenotazioneId = ValidationUtils.parseInt(request.getParameter("prenotazioneId"), -1);
			int disponibilitaId = ValidationUtils.parseInt(request.getParameter("disponibilitaId"), -1);

			switch (azione)
			{
				case "completa":
					if (prenotazioneId > 0)
					{
						prenotazioneService.concludiVisita(prenotazioneId, m.getId());
					}
					break;
				case "annulla":
					if (prenotazioneId > 0)
					{
						prenotazioneService.annullaPrenotazione(prenotazioneId, m.getId());
					}
					break;
				case "rimuovi":
					if (disponibilitaId > 0)
					{
						medicoService.rimuoviDisponibilita(disponibilitaId, m.getId());
					}
					break;
			}

			LocalDate data = ValidationUtils.parseLocalDate(request.getParameter("data"), "Data da mostrare");

			response.sendRedirect(request.getContextPath() + "/medico/agenda?data=" + data.toString());

		}
		catch (MedicoException | PrenotazioneException e)
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
