package it.mediclick.controller.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.mediclick.exception.AuthException;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;

@WebFilter(filterName = "1_CookieAutologinFilter", urlPatterns = "/*")
public class CookieAutologinFilter extends HttpFilter implements Filter
{

	private AutenticazioneService autenticazioneService;

	public void init(FilterConfig fConfig) throws ServletException
	{
		Contex contex = (Contex) fConfig.getServletContext().getAttribute("contex");
		autenticazioneService = new AutenticazioneService(contex);
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest _request = (HttpServletRequest) request;
		HttpServletResponse _response = (HttpServletResponse) response;

		HttpSession session = _request.getSession(false);

		Utente utente = (session != null) ? (Utente) session.getAttribute("utente") : null;

		if (utente == null)
		{
			checkCookie(_request, _response);
		}
		chain.doFilter(request, response);
	}

	private void checkCookie(HttpServletRequest request, HttpServletResponse response)
	{
		Cookie[] cookies = request.getCookies();

		Utente utente = null;

		Cookie cookie = null;

		if (cookies != null)
		{
			cookie = Arrays.stream(cookies).filter(c -> "tokenID".equals(c.getName())).findFirst().orElse(null);
		}

		if (cookie != null)
		{
			try
			{
				utente = autenticazioneService.tokenLogin(cookie.getValue());
				if (utente != null)
				{
					autenticazioneService.getUtenteCompleto(utente);
					HttpSession session = request.getSession(true);
					session.setAttribute("utente", utente);
				}
				else
				{
					cookie.setMaxAge(0);
					cookie.setPath(request.getContextPath());
					response.addCookie(cookie);
				}
			}
			catch (AuthException e)
			{
				e.printStackTrace();

				cookie.setMaxAge(0);
				cookie.setPath(request.getContextPath());
				response.addCookie(cookie);
			}
		}
	}

}
