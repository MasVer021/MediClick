package it.mediclick.util;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class AppContexListener implements ServletContextListener
{

	public void contextInitialized(ServletContextEvent sce)
	{
		ServletContext ctx = sce.getServletContext();

		Contex contex = new Contex();

		ctx.setAttribute("contex", contex);
	}

}
