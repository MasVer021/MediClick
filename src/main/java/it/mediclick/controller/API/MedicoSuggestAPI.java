package it.mediclick.controller.API;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.mediclick.exception.RicercaException;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/api/suggest")
public class MedicoSuggestAPI extends HttpServlet
{

	RicercaService ricercaService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		ricercaService = new RicercaService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String queryMedico = ValidationUtils.parseStringOpz(request.getParameter("query"), "");
		String citta = ValidationUtils.parseStringOpz(request.getParameter("citta"), "");

		try
		{

			response.getWriter().write(ricercaService.getMedicoCittaSuggestJson(queryMedico, citta));

		}
		catch (RicercaException e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			Map<String, String> error = new HashMap<String, String>();

			error.put("error", "Parametri errati: " + e.getMessage());

			response.getWriter().write(new Gson().toJson(error));

		}
	}

}
