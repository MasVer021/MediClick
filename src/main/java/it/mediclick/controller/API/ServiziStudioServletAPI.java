package it.mediclick.controller.API;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import it.mediclick.exception.RicercaException;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/api/serviziStudio")
public class ServiziStudioServletAPI extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private RicercaService ricercaService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		ricercaService = new RicercaService(contex);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try
		{

			int studioId = ValidationUtils.parseInt(request.getParameter("studioId"), "studioId");
			int medicoId = ValidationUtils.parseInt(request.getParameter("medicoId"), "medicoId");

			List<ErogazionePrestazione> erogazioni = ricercaService.cercaPrestazioniByMedicoEStudio(medicoId, studioId);

			List<Disponibilita> disponibilita = ricercaService.cercaDisponibilitaByMedicoeStudio(medicoId, studioId);

			Map<String, Object> responseData = new HashMap<>();

			List<Map<String, Object>> listPrestazioni = new ArrayList<>();
			for (ErogazionePrestazione ep : erogazioni)
			{
				Map<String, Object> map = new HashMap<>();
				map.put("id", ep.getId());
				map.put("nome", ep.getCatalogoPrestazioni().getNome());
				map.put("durata", ep.getDurata());
				map.put("prezzo", ep.getPrezzoLordoListino());
				listPrestazioni.add(map);
			}

			List<Map<String, Object>> listDisponibilita = new ArrayList<>();
			for (Disponibilita d : disponibilita)
			{
				Map<String, Object> map = new HashMap<>();
				map.put("id", d.getId());
				map.put("data", d.getDataOraInizio().toLocalDate().toString());
				map.put("ora", d.getDataOraInizio().toLocalTime().toString());
				listDisponibilita.add(map);
			}

			responseData.put("prestazioni", listPrestazioni);
			responseData.put("disponibilita", listDisponibilita);

			String json = new Gson().toJson(responseData);
			response.getWriter().write(json);
		}
		catch (RicercaException e)
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