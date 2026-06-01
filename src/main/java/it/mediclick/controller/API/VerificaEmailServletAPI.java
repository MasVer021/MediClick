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

import it.mediclick.exception.AuthException;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/api/checkEmail")
public class VerificaEmailServletAPI extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	private AutenticazioneService autenticazioneService;

	@Override
	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		autenticazioneService = new AutenticazioneService(contex);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		try
		{
			String email = ValidationUtils.parseStringOpz(request.getParameter("email"), "").trim();

			boolean emailEsistente = false;
			if (!email.isEmpty())
			{
				emailEsistente = autenticazioneService.verificaEmailEsistente(email);
			}

			Map<String, Object> responseData = new HashMap<>();
			responseData.put("exists", emailEsistente);

			String json = new Gson().toJson(responseData);
			response.getWriter().write(json);
		}
		catch (AuthException e)
		{
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

			Map<String, String> error = new HashMap<String, String>();

			error.put("error", e.getMessage());

			response.getWriter().write(new Gson().toJson(error));
		}
	}
}