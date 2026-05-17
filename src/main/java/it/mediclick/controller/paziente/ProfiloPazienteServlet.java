package it.mediclick.controller.paziente;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.AuthException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.PazienteException;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.service.PazienteService;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;


@WebServlet("/paziente/profilo")
public class ProfiloPazienteServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	
	PazienteService pazienteService ;
	AutenticazioneService autenticazioneService;
	
       
	 @Override
    public void init() throws ServletException
    {
    	Contex contex = (Contex) getServletContext().getAttribute("contex");
    	pazienteService = new PazienteService(contex);
    	autenticazioneService = new AutenticazioneService(contex);
    	
    }

 
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		try 
		{
			Paziente p = getPazienteConnesso(request, response);
			request.setAttribute("paziente",p);
		} 
		catch (PazienteException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
		}
		
		if ("telefono".equals(request.getParameter("edit")))
		{
		    request.setAttribute("modificaTelefono", true);
		} 
		else if ("password".equals(request.getParameter("edit"))) 
		{
		    request.setAttribute("modificaPassword", true);
		}
		
		if ("Aggiornamento+completato".equals(request.getParameter("msg")))
		{
		    request.setAttribute("msg","Aggiornamento completato");
		}
		
		
		request.getRequestDispatcher("/WEB-INF/view/paziente/profilo.jsp").forward(request, response);
		
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		
		try 
		{
			Paziente p = getPazienteConnesso(request, response);
		
		
			request.setAttribute("paziente",p);
			
			String editTarget = request.getParameter("edit");
			
			
			if ("telefono".equals(editTarget)) 
			{
				try 
				{
					String numeroTelefono = ValidationUtils.parseNumeroTelefono(request.getParameter("nuovoTelefono"), "numero di telefono");
					pazienteService.modificaTelefono(p.getId(), numeroTelefono); 
				} 
				catch (IllegalArgumentException e)
				{
					throw new PazienteException(e.getMessage(), "TEL_ERROR"); 
				}
				        
				    
			} 
			else if ("password".equals(editTarget)) 
			{
				try 
				{
					String passwordAttuale = request.getParameter("attualePassword");
					
					try 
					{
						autenticazioneService.login(p.getUtente().getEmail(), passwordAttuale);
					} 
					catch (AuthException e) 
					{
						throw new PazienteException("Password Corrente non valida", "AUTH_ERROR"); 
					}
					
					String password = ValidationUtils.parsePassword(request.getParameter("nuovaPassword"),request.getParameter("confermaPassword"));
					pazienteService.modificaPassword(p.getId(), password); 
				} 
				catch (IllegalArgumentException e)
				{
					throw new PazienteException(e.getMessage(), "TEL_ERROR"); 
				}	
			}
		}
		catch (PazienteException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
			
			
			if ("telefono".equals(request.getParameter("edit")))
			{
			    request.setAttribute("modificaTelefono", true);
			} 
			else if ("password".equals(request.getParameter("edit"))) 
			{
			    request.setAttribute("modificaPassword", true);
			}
			
			request.getRequestDispatcher("/WEB-INF/view/paziente/profilo.jsp").forward(request, response);
			return;
		}
		
		response.sendRedirect(request.getContextPath() + "/paziente/profilo?msg=Aggiornamento+completato");
	}
	
	private Paziente getPazienteConnesso(HttpServletRequest request,HttpServletResponse response) throws PazienteException
	{
		Utente  u = (Utente)request.getSession(false).getAttribute("utente");
		
		if(u== null)
		{
			throw new PazienteException("Errore nel recupero delL'utente", "UTENTE_ERROR");
		}
		
		Paziente p = pazienteService.findById(u.getId());
		p.setUtente(u);
		return p;
	}

}
