package it.mediclick.controller.paziente;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.PrenotazioneException;
import it.mediclick.model.DTO.RiepilogoPrenotazioneDTO;
import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

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
		String action = request.getParameter("action");
		
		
		int idStudio;
		int idErogazionePrestazione;
		int IdDisponibilita;
	
		try 
		{
			
			if ("annulla".equals(action))
			{
				annullaPrenotaz(request);
				 response.sendRedirect(request.getContextPath() + "/search");
				return;
			}
			
			boolean isNuovaPrenotazione = request.getParameter("studio") != null 
                    && request.getParameter("prestazione") != null 
                    && request.getParameter("disponibilitaId") != null;
			
			boolean haCarrelloInSospeso = request.getSession().getAttribute("riepilogo") != null;
			
			if (!isNuovaPrenotazione && haCarrelloInSospeso)
			{
			    request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
			    return;
			}
			
			try
			{
				idStudio = ValidationUtils.parseInt(request.getParameter("studio"), "studio");
				idErogazionePrestazione = ValidationUtils.parseInt(request.getParameter("prestazione"),"prestazione");
				IdDisponibilita = ValidationUtils.parseInt(request.getParameter("disponibilitaId"),"disponibilitaId");
				
			}
			catch (IllegalArgumentException e) 
			{
				throw new PrenotazioneException(e.getMessage(), "PARAM_ERROR");
			}
			
			Utente utenteConnesso = (Utente) request.getSession().getAttribute("utente");
			
			if(utenteConnesso == null) 
			{
			    response.sendRedirect(request.getContextPath() + "/login");
			    return;
			}
			
			prenotazioneService.bloccaDisponibilita(IdDisponibilita, utenteConnesso.getId());
		
			RiepilogoPrenotazioneDTO riepilogo = prenotazioneService.getRiepilogoPrenotazione(idStudio, idErogazionePrestazione, IdDisponibilita);
			request.getSession().setAttribute("riepilogo", riepilogo);
			
		}	 
		catch (PrenotazioneException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/medico_pubblic.jsp").forward(request, response);
			return;
		}
			
		request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Utente utenteConnesso = (Utente) request.getSession().getAttribute("utente");
		
		if(utenteConnesso == null)
		{
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		
		RiepilogoPrenotazioneDTO riepilogo = (RiepilogoPrenotazioneDTO) request.getSession().getAttribute("riepilogo");
		
		if (riepilogo == null) 
		{
			response.sendRedirect(request.getContextPath() + "/");
			return;
		}
		
		try
		{
			
			
			double prezzo_lordo =  riepilogo.getPrestazione().getPrezzoLordoListino();
			prenotazioneService.getCompleto(riepilogo.getMedico());
			double aliquota = riepilogo.getMedico().getRegimeFiscale().getAliquotaDefault();
			
			
			int idSconto = -1;
			double percentuale_Sconto = 0;
			
			String codiceSconto = ValidationUtils.parseStringOpz(request.getParameter("codiceSconto"), null);
			
			if(codiceSconto!=null)
			{
				CodiceSconto sconto = prenotazioneService.findSconto(codiceSconto);
				
				if(prenotazioneService.isValid(sconto))
				{
					percentuale_Sconto = sconto.getValorePercentuale();
					idSconto = sconto.getId();
				}
			}
			
			
			
			double prezzo_pagato = prenotazioneService.getPrezzoPagato(prezzo_lordo, percentuale_Sconto);
			
			double prezzo_trattenuta = prenotazioneService.getPrezzoTrattenuta(prezzo_lordo, percentuale_Sconto);
			
			double prezzo_tasse = prenotazioneService.getTasse(prezzo_lordo, percentuale_Sconto,aliquota);
			
			double prezzo_netto=	prenotazioneService.getPrezzoNetto(prezzo_lordo, percentuale_Sconto,aliquota);
			
			int IdDisponibilita = riepilogo.getDisponibilita().getId();
			int idUtente = utenteConnesso.getId();
			int idErogazione = riepilogo.getPrestazione().getId();
			String metodoPagamento;
			
			
			try 
			{
				metodoPagamento = ValidationUtils.parseString( request.getParameter("metodoPagamento"), "metodo di pagamento");
			} 
			catch (IllegalArgumentException e) 
			{
				throw new PrenotazioneException(e.getMessage(), "PARAM_ERROR");
			}
			
			String idTransazioneEsterno = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
			
			if (prenotazioneService.creaPrenotazione(idUtente, IdDisponibilita,idTransazioneEsterno,idErogazione,prezzo_pagato, prezzo_trattenuta, prezzo_netto, prezzo_tasse, idSconto,metodoPagamento)) 
			{
				request.getSession().removeAttribute("riepilogo");
				request.getRequestDispatcher("/WEB-INF/view/paziente/prenotazione_effettuata.jsp").forward(request, response);
			} 
			
			
		}
		catch (PrenotazioneException e) 
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/paziente/conferma_prenotazione.jsp").forward(request, response);
			return;
		}	
	}
	
	private void annullaPrenotaz(HttpServletRequest request) throws PrenotazioneException
	{
		 RiepilogoPrenotazioneDTO riepilogo = (RiepilogoPrenotazioneDTO) request.getSession().getAttribute("riepilogo");
		 
	        if (riepilogo != null) 
	        {
	           
	            prenotazioneService.sbloccaDisponibilita(riepilogo.getDisponibilita().getId());
	           
	            request.getSession().removeAttribute("riepilogo");
	        }
	}
}
