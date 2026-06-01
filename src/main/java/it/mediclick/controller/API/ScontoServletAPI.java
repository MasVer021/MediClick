package it.mediclick.controller.API;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.mediclick.exception.PrenotazioneException;
import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/paziente/api/sconto")
public class ScontoServletAPI extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private PrenotazioneService prenotazioneService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		prenotazioneService = new PrenotazioneService(contex);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try
		{

			String codiceSconto = ValidationUtils.parseStringOpz(request.getParameter("codiceSconto"), null);

			CodiceSconto c = prenotazioneService.findSconto(codiceSconto);

			Map<String, Object> responseData = new HashMap<>();

			if (c.isAttivo() && c.getDataScadenza().isAfter(LocalDate.now()))
			{
				responseData.put("codice", c.getCodice());
				responseData.put("percentuale", c.getValorePercentuale());
			}
			else
			{
				responseData.put("codice", "non trovato");
			}

			String json = new Gson().toJson(responseData);
			response.getWriter().write(json);
		}
		catch (PrenotazioneException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			Map<String, String> error = new HashMap<String, String>();

			error.put("error", e.getMessage());

			response.getWriter().write(new Gson().toJson(error));

		}
		catch (IllegalArgumentException e)
		{
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

			Map<String, String> error = new HashMap<String, String>();

			error.put("error", "Parametri errati: " + e.getMessage());

			response.getWriter().write(new Gson().toJson(error));
		}
	}
}