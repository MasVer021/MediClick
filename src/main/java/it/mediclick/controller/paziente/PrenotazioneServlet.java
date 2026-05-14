package it.mediclick.controller.paziente;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.model.DTO.RiepilogoPrenotazioneDTO;
import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.Utente;
import it.mediclick.model.dao.StudioDAO;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.service.RicercaService;
import it.mediclick.util.Contex;

@WebServlet("/paziente/prenotazione")
public class PrenotazioneServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
	PrenotazioneService  prenotazioneService;
	
       
    
    @Override
    public void init() throws ServletException
    {
    	Contex contex = (Contex) getServletContext().getAttribute("contex");
    	prenotazioneService = new PrenotazioneService(contex);
    	
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		int idStudio = Integer.parseInt(request.getParameter("studio"));
		int idErogazionePrestazione = Integer.parseInt(request.getParameter("prestazione"));
		int IdDisponibilita = Integer.parseInt(request.getParameter("disponibilitaId"));
	
		RiepilogoPrenotazioneDTO riepilogo = prenotazioneService.getRiepilogoPrenotazione(idStudio, idErogazionePrestazione, IdDisponibilita);
		request.getSession().setAttribute("riepilogo", riepilogo);
		
		Utente utenteConnesso = (Utente) request.getSession().getAttribute("utente");
		
		if(!prenotazioneService.bloccaDisponibilita(IdDisponibilita, utenteConnesso.getId()))
		{
			request.setAttribute("Errore", "Errore: Lo slot non è più disponibile o il tempo è scaduto.");
		}
		
		request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Utente utenteConnesso = (Utente) request.getSession().getAttribute("utente");
		RiepilogoPrenotazioneDTO riepilogo = (RiepilogoPrenotazioneDTO) request.getSession().getAttribute("riepilogo");
		
		String codiceSconto = request.getParameter("codiceSconto");
		CodiceSconto sconto = prenotazioneService.findSconto(codiceSconto);
		
		if (riepilogo == null) 
		{
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		
		
		double prezzo_pagato;
		double prezzo_netto; 
		double prezzo_trattenuta;
		double prezzo_tasse;
		
		double percentuale_Sconto = 0;
		int idSconto=-1;
		
		
		
		
		if(prenotazioneService.isValid(sconto))
		{
			percentuale_Sconto = sconto.getValorePercentuale();
			idSconto = sconto.getId();
		}
		
		
		prezzo_pagato = riepilogo.getPrestazione().getPrezzoLordoListino();
		
		prezzo_trattenuta = (double) (prenotazioneService.getTrattenuta()/100)*prezzo_pagato;
		
		prezzo_tasse = (double) (riepilogo.getMedico().getRegimeFiscale().getAliquotaDefault()/100) * prezzo_pagato;
		
		prezzo_netto= prezzo_pagato-(prezzo_trattenuta-prezzo_tasse);
		
		int IdDisponibilita = riepilogo.getDisponibilita().getId();
		int idUtente = utenteConnesso.getId();
		int idErogazione = riepilogo.getPrestazione().getId();
		
		String metodoPagamento = request.getParameter("metodoPagamento");
		
		
		if (prenotazioneService.creaPrenotazione(idUtente, IdDisponibilita,idErogazione,prezzo_pagato, prezzo_trattenuta, prezzo_netto, prezzo_tasse, idSconto,metodoPagamento)) 
		{
			
			request.getSession().removeAttribute("riepilogo");
			response.sendRedirect(request.getContextPath() + "/WEB-INF/view/paziente/prenotazione_effettuata.jsp");
		} 
		else 
		{
			request.setAttribute("Errore", "Si è verificato un errore durante la prenotazione. Riprova.");
			request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
		}
	}

}
