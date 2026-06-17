package it.mediclick.controller.Auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.mediclick.exception.AuthException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/login")
public class LoginServlet extends HttpServlet
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

		session = request.getSession(false);

		if (session != null && session.getAttribute("utente") != null)
		{
			redirectByRole((Utente) session.getAttribute("utente"), response, request);
			return;
		}

		request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		try
		{
			try
			{
				String email = ValidationUtils.parseString(request.getParameter("email"), "Email");
				String password = ValidationUtils.parseString(request.getParameter("password"), "Password");
				boolean keepLogin = ValidationUtils.parseBoolean(request.getParameter("rememberMe"), false);

				Utente utente = autenticazioneService.login(email.trim(), password);

				if (keepLogin)
				{
					String token = autenticazioneService.inserToken(utente.getId());
					Cookie cookie = new Cookie("tokenID", token);
					cookie.setPath(request.getContextPath());
					cookie.setMaxAge(365 * 24 * 60 * 60);
					cookie.setHttpOnly(true);
					cookie.setSecure(true);
					response.addCookie(cookie);
				}

				if (!utente.isAccountAttivo())
				{
					throw new AuthException("L'account risulta bloccato al momento", "AUTH_BLOCKED_ACCOUNT");
				}

				HttpSession session = request.getSession(true);

				autenticazioneService.getUtenteCompleto(utente);

				session.setAttribute("utente", utente);

				String redirectUrl = (String) session.getAttribute("redirectDopoLogin");

				if (redirectUrl != null && utente.getRuolo().getCodice().equals("PAZIENTE"))
				{
					session.removeAttribute("redirectDopoLogin");
					response.sendRedirect(redirectUrl);
				}
				else
				{
					redirectByRole(utente, response, request);
				}
			}
			catch (IllegalArgumentException e)
			{
				throw new AuthException(e.getMessage(), "ERROR_CREDENTIAL");
			}
		}
		catch (AuthException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/login.jsp").forward(request, response);
		}
	}

	private void redirectByRole(Utente utente, HttpServletResponse response, HttpServletRequest request) throws IOException
	{
		int ruolo = utente.getRuoloId();

		switch (ruolo)
		{
			case 3:
				response.sendRedirect(request.getContextPath() + "/search");
				break;
			case 2:
				response.sendRedirect(request.getContextPath() + "/medico/agenda");
				break;
			case 1:
				response.sendRedirect(request.getContextPath() + "/admin/dashboard");
				break;
		}
	}

}
