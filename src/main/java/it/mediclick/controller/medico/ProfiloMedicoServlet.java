package it.mediclick.controller.medico;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.RicercaException;
import it.mediclick.model.DTO.ProfiloMedicoPubblicoDTO;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;


@WebServlet("/profiloMedico")
public class ProfiloMedicoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	 RicercaService ricercaservice;
   @Override
	public void init() throws ServletException 
   	{
	   Contex contex = (Contex) getServletContext().getAttribute("contex");
		ricercaservice = new RicercaService(contex);
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		String idmedicoStr = request.getParameter("id");
		if(idmedicoStr != null && !idmedicoStr.isBlank())
		{
			try
			{
				int id = Integer.parseInt(idmedicoStr);
		
				ProfiloMedicoPubblicoDTO profiloMedico = null;
				
				try 
				{
					profiloMedico = ricercaservice.dettagliProfiloMedico(id);
				} 
				catch (RicercaException e) 
				{
					request.setAttribute("errore", new ErrorInfo(e));
				}
				
				request.setAttribute("profiloMedico", profiloMedico);
				request.getRequestDispatcher("/WEB-INF/view/medico_pubblic.jsp").forward(request, response);
			}
			catch (NumberFormatException e)
			{
				request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			}
		}
		else
		{
			
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
