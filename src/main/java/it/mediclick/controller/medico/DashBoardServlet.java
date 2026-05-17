package it.mediclick.controller.medico;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.mediclick.exception.ErrorInfo;
import it.mediclick.exception.MedicoException;
import it.mediclick.exception.PrenotazioneException;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Studio;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.MedicoService;
import it.mediclick.service.PrenotazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;


@WebServlet("/medico/dashboard")
public class DashBoardServlet extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
	MedicoService medicoService;
    PrenotazioneService prenotazioneService;
    
    public void init() throws ServletException 
    {
        Contex contex = (Contex) getServletContext().getAttribute("contex");
        medicoService = new MedicoService(contex);  
        prenotazioneService = new PrenotazioneService(contex);
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
    {
        try 
        {
            Medico m = getMedicoConnesso(request, response);
           
            LocalDate dataInizio = ValidationUtils.parseLocalDateOpz(request.getParameter("dataInizio"), LocalDate.now().minusDays(30));
            		
            LocalDate dataFine  = ValidationUtils.parseLocalDateOpz(request.getParameter("dataFine"), LocalDate.now());
            
            
            Map<String, Object> stats = prenotazioneService.getStatistiche(m.getId(),dataInizio,dataFine);
            
            request.setAttribute("dataInizio", dataInizio);
            request.setAttribute("dataFine", dataFine);
            request.setAttribute("stats", stats);
            request.getRequestDispatcher("/WEB-INF/view/medico/dashboard.jsp").forward(request, response); 
        } 
        
        catch (MedicoException | it.mediclick.exception.PrenotazioneException e) 
        {
            request.setAttribute("errore", new ErrorInfo(e));
            request.getRequestDispatcher("/WEB-INF/view/medico/dashboard.jsp").forward(request, response);
            return;
        }
    }
    private Medico getMedicoConnesso(HttpServletRequest request, HttpServletResponse response) throws MedicoException
    {
        Utente u = (Utente) request.getSession(false).getAttribute("utente");
        
        if (u == null)
        {
            throw new MedicoException("Errore nel recupero dell'utente", "UTENTE_ERROR");
        }
        
        Medico m = medicoService.findById(u.getId());
        m.setUtente(u);
        return m;
    }
}
