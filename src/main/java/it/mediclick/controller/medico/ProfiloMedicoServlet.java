package it.mediclick.controller.medico;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		int id = Integer.parseInt(request.getParameter("id"));
		
		ProfiloMedicoPubblicoDTO profiloMedico = null;
		
		try 
		{
			profiloMedico = ricercaservice.dettagliProfiloMedico(id);
		} 
		catch (SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		request.setAttribute("profiloMedico", profiloMedico);
		request.getRequestDispatcher("/WEB-INF/view/medico_pubblic.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
