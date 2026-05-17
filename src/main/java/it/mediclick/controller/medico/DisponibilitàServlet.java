package it.mediclick.controller.medico;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import it.mediclick.model.bean.Studio;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.MedicoService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/medico/disponibilita")
public class DisponibilitàServlet extends HttpServlet {
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
			
			
			List<Studio> s = medicoService.findAllStudio();
			
			request.setAttribute("studi", s);
			request.getRequestDispatcher("/WEB-INF/view/medico/disponibilita.jsp").forward(request, response);
		} 
		catch (MedicoException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/medico/disponibilita.jsp").forward(request, response);
			return;
		}
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		try 
		{
			Medico m = getMedicoConnesso(request, response);	
			
			try 
			{
				
				
				
				LocalDate data = ValidationUtils.parseLocalDate(request.getParameter("dataGiornata"), "Data");
				LocalTime oraInizio = ValidationUtils.parseLocalTime(request.getParameter("oraInizio"), "Ora inizio");
				LocalTime oraFine = ValidationUtils.parseLocalTime(request.getParameter("oraFine"), "Ora fine");
				
				
				LocalDateTime dataIn = LocalDateTime.of(data, oraInizio);
				LocalDateTime dataOut = LocalDateTime.of(data, oraFine);
				
				int studioId = ValidationUtils.parseInt(request.getParameter("studioId"), "Studio");
				
				Studio s = medicoService.findStudioById(studioId);
			
				medicoService.configuraOrario(m.getId(), dataIn, dataOut, s);
				
				response.sendRedirect(request.getContextPath() + "/medico/agenda?data=" + data);
					
			} 
			catch (IllegalArgumentException e) 
			{
				throw new MedicoException(e.getMessage(), "PARAM_ERROR");
			}	
		} 
		catch (MedicoException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/medico/disponibilita.jsp").forward(request, response);
			return;
		}
		
		
		
		
		
	}
	
	private Medico getMedicoConnesso(HttpServletRequest request,HttpServletResponse response) throws MedicoException
	{
		Utente  u = (Utente)request.getSession(false).getAttribute("utente");
		
		if(u== null)
		{
			throw new MedicoException("Errore nel recupero dell'utente", "UTENTE_ERROR");
		}
		
		Medico m = medicoService.findById(u.getId());
		m.setUtente(u);
		return m;
	}

}
