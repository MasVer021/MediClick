package it.mediclick.controller.Auth;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private AutenticazioneService autenticazioneService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		autenticazioneService = new AutenticazioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		HttpSession session = request.getSession(false);

		if (session != null && session.getAttribute("utente") != null)
		{
			session.invalidate();
		}

		Cookie[] cookies = request.getCookies();

		if (cookies != null)
		{
			Arrays.stream(cookies).filter(c -> "tokenID".equals(c.getName())).forEach(c ->
			{
				try
				{
					autenticazioneService.revocaToken(c.getValue());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				c.setMaxAge(0);
				c.setPath(request.getContextPath());
				response.addCookie(c);
			});
		}

		response.sendRedirect(request.getContextPath() + "/login");
	}

}
