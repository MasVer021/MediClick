package it.mediclick.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Medico;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;

@WebServlet("/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
    RicercaService ricercaservice;
    
    public void init() throws ServletException 
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		ricercaservice = new RicercaService(contex);
		
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
			List<Categoria> categorie = null;
			try 
			{
				categorie = ricercaservice.getCategorie();
				
				for( var i : categorie)
				{
					System.out.print(i);
				}
			} 
			catch (SQLException e) 
			{
				
				request.setAttribute("errore", "Errore interno. Riprova più tardi.");
			    request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			}
			
			request.setAttribute("categorie", categorie);
			
		if(!request.getParameterNames().hasMoreElements())
		{	
			request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			return;
		}
		else
		{
			int idCategoriaSpecialista = request.getParameter("specialista")!=null ? Integer.parseInt(request.getParameter("specialista")):-1;
			String citta = request.getParameter("citta")!=null ? request.getParameter("citta"):"";
			String querySpecialista = request.getParameter("querySpecialista")!=null ? request.getParameter("querySpecialista"):"";
			
			try 
			{
				List <MedicoCardDTO> medici = ricercaservice.cercaMediciCards(querySpecialista, idCategoriaSpecialista, citta);
				
				
				request.setAttribute("medici", medici);
			} 
			catch (SQLException e) 
			{
				// TODO cambiare errore
				request.setAttribute("errore", "Errore interno. Riprova più tardi.");
			    request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			}
			request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			return;	
		}
		
		
			
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		doGet(request, response);
	}

}
