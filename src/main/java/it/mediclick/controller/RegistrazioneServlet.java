package it.mediclick.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Ruolo;
import it.mediclick.model.bean.Utente;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PazienteDAO;
import it.mediclick.model.dao.RuoloDAO;
import it.mediclick.util.Contex;


@WebServlet("/singin")
public class RegistrazioneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	List<Ruolo> ruoli;
	
	MedicoDAO medicoController;
	PazienteDAO pazienteController;
	
	
       

	public void init() throws ServletException 
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		try 
		{
			ruoli = new RuoloDAO(contex).findAll();
			medicoController = new MedicoDAO(contex);
			pazienteController = new PazienteDAO(contex);
		} 
		catch (SQLException e) 
		{
			throw new ServletException();
		}
	}
	
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		 request.getRequestDispatcher("/WEB-INF/view/singin.jsp").forward(request, response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		
		
		Utente u = new Utente();
		
		u.setDataIscrizione(LocalDate.now());
		
		//TODO validazione
		String email = (String)request.getParameter("email");
		String password = (String)request.getParameter("password");
		u.setEmail(email);  
		u.setPassword(password);		
		
		boolean isMedico = Boolean.parseBoolean((String)request.getParameter("isMedico"));
		
		String nome = (String)request.getParameter("nome");
		String cognome = (String)request.getParameter("cognome");
		
		if(!isMedico)
		{
			//TODO validazione 
			
			
			
			
			LocalDate dataNascita = LocalDate.parse((String)request.getParameter("DataNascita"));
			String numeroTelefono = (String)request.getParameter("telefono");
			String codiceFiscale = (String)request.getParameter("CF");
			
			Paziente p = new Paziente();
			p.setCodiceFiscale(codiceFiscale);
			p.setCognome(cognome);
			p.setNome(nome);
			p.setDataNascita(dataNascita);
			p.setTelefono(numeroTelefono);
			
			int idRuolo = ruoli.stream().filter(f->f.getCodice().equals("PAZIENTE")).findFirst().orElse(null).getId();
		
			u.setRuoloId(idRuolo);
			p.setUtente(u);
			
			try 
			{
				pazienteController.insert(p);
			} 
			catch (SQLException e) 
			{
				request.setAttribute("errore", "Errore interno. Riprova più tardi.");
		        request.getRequestDispatcher("/WEB-INF/view/singin.jsp").forward(request, response);
		        return;
			}
			
			
		}
		else
		{
			//TODO validazione 
			String bio = (String)request.getParameter("Bio");
			String partitaIva = (String)request.getParameter("PIva");
			int regime = Integer.parseInt((String) request.getParameter("RegimeFiscale"));
			
			Medico m = new Medico();
			m.setCognome(cognome);
			m.setNome(nome);
			m.setBio(bio);
			m.setpIva(partitaIva);
			m.setRegimeFiscaleId(regime);
			
			int idRuolo = ruoli.stream().filter(f->f.getCodice().equals("MEDICO")).findFirst().orElse(null).getId();
		
			u.setRuoloId(idRuolo);
			m.setUtente(u);
			
			try 
			{
				medicoController.insert(m);
			} 
			catch (SQLException e) 
			{
				request.setAttribute("errore", "Errore interno. Riprova più tardi.");
		        request.getRequestDispatcher("/WEB-INF/view/singin.jsp").forward(request, response);
		        return;
			}
		}
		
		response.sendRedirect(request.getContextPath()+"/login");
	}

}
