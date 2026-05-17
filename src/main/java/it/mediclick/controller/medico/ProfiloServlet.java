package it.mediclick.controller.medico;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.MedicoService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/medico/profilo")

@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1, 
    maxFileSize = 1024 * 1024 * 5,      
    maxRequestSize = 1024 * 1024 * 10   
)
public class ProfiloServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	MedicoService medicoService;
	
    
	   
	public void init() throws ServletException 
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		medicoService = new MedicoService(contex);	
	}


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		try 
        {
            Medico m = getMedicoConnesso(request, response);
            
            
            List<RegimeFiscale> regimi = medicoService.findAllRegimiFiscali();
            List<ErogazionePrestazione> miePrestazioni = medicoService.getMiePrestazioni(m.getId());
            List<Studio> studi = medicoService.findAllStudio();
            List<CatalogoPrestazioni> prestazioniCatalogo = medicoService.findAllPrestazioni();
            
           
            request.setAttribute("medico", m);
            request.setAttribute("regimi", regimi);
            request.setAttribute("miePrestazioni", miePrestazioni);
            request.setAttribute("studi", studi);
            request.setAttribute("prestazioniCatalogo", prestazioniCatalogo);
            
            request.getRequestDispatcher("/WEB-INF/view/medico/profilo.jsp").forward(request, response);
        } 
        catch (MedicoException e) 
        {
            request.setAttribute("errore", new ErrorInfo(e));
        }   
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		String successoMsg = null;
        try 
        {
        	
            Medico m = getMedicoConnesso(request, response);
            String azione = ValidationUtils.parseStringOpz(request.getParameter("action"), "NoAction");
            
            switch (azione) 
            {
                case "salva-dati":
                    String nome = request.getParameter("nome");
                    String cognome = request.getParameter("cognome");
                    String bio = request.getParameter("bio");
                    String pIva = request.getParameter("pIva");
                    int regimeId = ValidationUtils.parseInt(request.getParameter("regimeFiscaleId"), -1);
                    
                    medicoService.aggiornaProfilo(m.getId(), cognome, nome, bio, pIva, regimeId, null);
                    
                    successoMsg = "Dati del profilo aggiornati con successo!";
                    
                    break;
                    
                case "carica-foto":
                	 try 
                	 {
	            	        Part fotoPart = request.getPart("foto"); 
	            	        
	            	        if (fotoPart != null && fotoPart.getSize() > 0) 
	            	        {
	            	            byte[] fotoBytes = fotoPart.getInputStream().readAllBytes();
	            	            
	            	       
	            	            medicoService.aggiornaProfilo(m.getId(), null, null, null, null, -1, fotoBytes);
	            	            successoMsg = "Foto profilo aggiornata con successo!";
	            	        }
                	    } 
                	    catch (Exception e) 
                	    {
                	        throw new MedicoException("Errore durante il caricamento della foto profilo.", "REG_FOTO_ERROR");
                	    }
                    break;
                    
                case "associa-prestazione":
                    int studioId = ValidationUtils.parseInt(request.getParameter("studioId"), -1);
                    int catalogoId = ValidationUtils.parseInt(request.getParameter("catalogoId"), -1);
                    int durata = ValidationUtils.parseInt(request.getParameter("durata"), 30); 
                    double prezzo = ValidationUtils.parseDouble(request.getParameter("prezzo"), 0.0);
                    
                   
                    medicoService.associaPrestazione(m.getId(), catalogoId, studioId, prezzo, durata);
                    
                   
                    
                  
                    successoMsg = "Nuova prestazione associata con successo!";
                    break;
                    
                case "rimuovi-prestazione":
                    int erogazioneId = ValidationUtils.parseInt(request.getParameter("erogazioneId"), -1);
                    if (erogazioneId > 0) 
                    {
                        medicoService.rimuoviPrestazione(erogazioneId);
                        successoMsg = "Prestazione rimossa/sospesa con successo!";
                    }
                    break;
            }
            
            response.sendRedirect(request.getContextPath() + "/medico/profilo");
        } 
        catch (MedicoException e) 
        {
            request.setAttribute("errore", new ErrorInfo(e));
            doGet(request, response);
            return;
        }
        
 
       
	}
	
	private Medico getMedicoConnesso(HttpServletRequest request,HttpServletResponse response) throws MedicoException
	{
		Utente  u = (Utente)request.getSession(false).getAttribute("utente");
		
		if(u== null)
		{
			throw new MedicoException("Errore nel recupero dell'utente", "UTENTE_ERROR");
		}
		
		Medico m = medicoService.findById(u.getId());
		m.setUtente(u);
		return m;
	}

}
