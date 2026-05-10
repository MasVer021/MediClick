package it.mediclick.controller;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;

import it.mediclick.model.bean.Utente;

import javax.servlet.http.*;

@WebFilter({ "/paziente/*", "/medico/*", "/admin/*" })
public class AuthFilter extends HttpFilter implements Filter {
       

	
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException 
	{
		 	HttpServletRequest  _request  = (HttpServletRequest) request;
		    HttpServletResponse _response = (HttpServletResponse) response;
		    HttpSession session = _request.getSession(false);
		    
		    
		    Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;
		    
		    if (utente == null) 
		    {
	            _response.sendRedirect(_request.getContextPath() + "/login");
	            return;
		    }
		     
            
		    String ruolo  = utente.getRuolo().getCodice();
            
            String path   = _request.getServletPath();
            
            if (path.startsWith("/admin") && !ruolo.equals("ADMIN")) 
            {
                _response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            if (path.startsWith("/medico") && !ruolo.equals("MEDICO")) 
            {
                _response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            chain.doFilter(_request, _response);
	}

}
