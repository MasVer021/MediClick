package it.mediclick.controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.mediclick.model.bean.Utente;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;

@WebServlet("/login")
public class LoginServlet extends HttpServlet 
{
	
	private AutenticazioneService autenticazioneService;
	
    public LoginServlet() 
    {
        super();
    }

	
	public void init() throws ServletException 
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		autenticazioneService = new AutenticazioneService(contex);
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession session = request.getSession(false);
		if(session != null && session.getAttribute("utente")!=null)
		{
			redirectByRole((Utente)session.getAttribute("utente"), response, request);
		}
		
		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String email    = request.getParameter("email");
	    String password = request.getParameter("password");
	    
	    if (email == null || email.isBlank() || password == null || password.isBlank()) 
	    {
	        request.setAttribute("errore", "Inserisci email e password.");
	        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	        return;
	    }
	    
	    try 
	    {
	        Utente utente = autenticazioneService.login(email.trim(), password);
	        
	        if (utente == null) 
	        {
	            request.setAttribute("errore", "Credenziali non valide.");
	            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	            return;
	        }
	        if (!utente.isAccountAttivo()) 
	        {
	            request.setAttribute("errore", "Account sospeso. Contatta l'assistenza.");
	            request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	            return;
	        }
	        
	        HttpSession session = request.getSession(true);
	        session.setAttribute("utente", utente);
	        redirectByRole(utente, response,request);
	    } 
	    catch (SQLException e) 
	    {
	        request.setAttribute("errore", "Errore interno. Riprova più tardi.");
	        request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	    }
	}
	
	private void redirectByRole (Utente utente,HttpServletResponse response, HttpServletRequest request) throws IOException
	{
		int ruolo = utente.getRuoloId();
		
		switch(ruolo)
		{
			case 3 :
				response.sendRedirect(request.getContextPath() + "/paziente/profilo");
				break;
	        case 2 :
	        	response.sendRedirect(request.getContextPath() + "/medico/agenda");
	        	break;
	        case 1 :
	        	response.sendRedirect(request.getContextPath() + "/admin/dashboard");
	        	break;
	        default : 
	        	response.sendRedirect(request.getContextPath() + "/ricerca");
	        	break;
		
		}
	}

}
