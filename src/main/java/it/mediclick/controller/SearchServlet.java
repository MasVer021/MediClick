package it.mediclick.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.exception.*;
import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.bean.Categoria;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;

@WebServlet("/search")
public class SearchServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	RicercaService ricercaservice;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		ricercaservice = new RicercaService(contex);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		if (request.getSession().getAttribute("riepilogo") != null)
		{
			request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
			return;
		}

		List<Categoria> categorie = null;

		try
		{
			categorie = ricercaservice.getCategorie();

		}
		catch (RicercaException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
		}

		request.setAttribute("categorie", categorie);

		if (!request.getParameterNames().hasMoreElements())
		{
			request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			return;
		}
		else
		{
			int idCategoriaSpecialista = -1;
			String catSpec = request.getParameter("specialista");

			if (catSpec != null && !catSpec.isBlank())
			{
				try
				{
					idCategoriaSpecialista = Integer.parseInt(catSpec);
				}
				catch (NumberFormatException e)
				{

				}
			}

			String citta = request.getParameter("citta") != null ? request.getParameter("citta") : null;
			String querySpecialista = request.getParameter("querySpecialista") != null ? request.getParameter("querySpecialista") : null;

			try
			{
				List<MedicoCardDTO> medici = ricercaservice.cercaMediciCards(querySpecialista, idCategoriaSpecialista, citta);
				request.setAttribute("medici", medici);
			}
			catch (RicercaException e)
			{
				request.setAttribute("errore", new ErrorInfo(e));
				request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
			}

			request.getRequestDispatcher("/WEB-INF/view/search.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
