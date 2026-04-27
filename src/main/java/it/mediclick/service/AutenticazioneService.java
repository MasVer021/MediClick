package it.mediclick.service;

import it.mediclick.model.bean.Amministratore;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.Utente;
import it.mediclick.model.dao.AmministratoreDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PazienteDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;
import it.mediclick.util.PasswordUtils;
import java.sql.SQLException;

public class AutenticazioneService 
{

    private UtenteDAO utenteDAO;
    private PazienteDAO pazienteDAO;
    private MedicoDAO medicoDAO;
    private AmministratoreDAO adminDAO;
    private Contex _contex;

    public AutenticazioneService(Contex contex) 
    {
    	if(contex != null)
    		_contex = contex;
  
        this.utenteDAO = new UtenteDAO(_contex);
        this.pazienteDAO = new PazienteDAO(_contex);
        this.medicoDAO = new MedicoDAO(_contex);
        this.adminDAO = new AmministratoreDAO(_contex);
    }

    
    public Utente login(String email, String password) throws SQLException 
    {
        Utente u = utenteDAO.findByEmail(email);
        if (u != null && PasswordUtils.checkPassword(password, u.getPassword())) 
        {
            return u;
        }
        return null;
    }

    public int registraPaziente(Paziente p, String password) 
    {
        p.getUtente().setPassword(password);
        
        try 
        {
            return pazienteDAO.insert(p);                 
        } 
        catch (SQLException e) 
        {
           
            System.err.println("Errore durante registraPaziente: " + e.getMessage());
            return -1;
        }
        
        
    }

    public int registraMedico(Medico m, String password) {
        m.getUtente().setPassword(password);
        m.setStatoVerifica(Medico.StatoVerifica.IN_ATTESA); 
        try 
        {
            return medicoDAO.insert(m); 
        } 
        catch (SQLException e) 
        {
            System.err.println("Errore durante registraMedico: " + e.getMessage());
            return -1;
        }
    }

    
    public int registraAdmin(Amministratore a, String password) 
    {
        a.getUtente().setPassword(password);
        try 
        {
            return adminDAO.insert(a); 
           
        }
        catch (SQLException e) 
        {
            System.err.println("Errore durante registraAdmin: " + e.getMessage());
            return -1;
        }
    }

    public boolean cambiaPassword(String email, String vecchia, String nuova) throws SQLException 
    {
        Utente u = utenteDAO.findByEmail(email);
        if (u != null && PasswordUtils.checkPassword(vecchia, u.getPassword())) 
        {
            utenteDAO.updatePassword(u.getId(), nuova);
            return true;
        }
        return false;
    }
}
