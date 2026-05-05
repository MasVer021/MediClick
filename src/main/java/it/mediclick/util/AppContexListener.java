package it.mediclick.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;


@WebListener
public class AppContexListener implements ServletContextListener {

    
    public AppContexListener() 
    {
        // TODO Auto-generated constructor stub
    }

	
    public void contextDestroyed(ServletContextEvent sce)  
    { 
         // TODO Auto-generated method stub
    }

	
    public void contextInitialized(ServletContextEvent sce)  
    { 
    	 ServletContext ctx = sce.getServletContext();
    	 
        
         String dbUrl      = ctx.getInitParameter("dbUrl");
         String dbUser     = ctx.getInitParameter("dbUser");
         String dbPassword = ctx.getInitParameter("dbPassword");
         
        
         
         Contex contex = new Contex(dbUrl, dbUser, dbPassword);
         
         
         ctx.setAttribute("contex", contex);
    }
	
}
