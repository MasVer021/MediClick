package it.mediclick.service;

import it.mediclick.exception.AuthException;
import it.mediclick.model.bean.Amministratore;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.model.bean.Utente;
import it.mediclick.model.dao.AmministratoreDAO;
import it.mediclick.model.dao.CertificatoDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.PazienteDAO;
import it.mediclick.model.dao.RuoloDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;
import it.mediclick.util.PasswordUtils;
import java.sql.SQLException;
import java.util.List;

public class AutenticazioneService 
{

    private final UtenteDAO utenteDAO;
    private final PazienteDAO pazienteDAO;
    private final MedicoDAO medicoDAO;
    private final AmministratoreDAO adminDAO;
    private final CertificatoDAO certificatoDAO;
    private final RuoloDAO ruoloDAO;
    private final Contex _contex;

    public AutenticazioneService(Contex contex) 
    {
    	_contex = contex;
  
        this.utenteDAO = new UtenteDAO(_contex);
        this.pazienteDAO = new PazienteDAO(_contex);
        this.medicoDAO = new MedicoDAO(_contex);
        this.adminDAO = new AmministratoreDAO(_contex);
        this.ruoloDAO = new RuoloDAO(_contex);
        this.certificatoDAO = new CertificatoDAO(_contex);
    }

    
    public Utente login(String email, String password) throws AuthException 
    {
        try 
        {
            Utente u = utenteDAO.findByEmail(email).orElseThrow(() -> new AuthException("Utente non trovato con email: " + email, "AUTH_USER_NOT_FOUND"));
            if (PasswordUtils.checkPassword(password, u.getPassword())) 
            {
            	System.out.print("si si trova");
                return u;
            } 
            else 
            {
                throw new AuthException("Password errata per l'utente con email: " + email, "AUTH_INVALID_PASSWORD");
            }
        } 
        catch (SQLException e) 
        {
            throw new AuthException("Errore durante il login per l'email " + email + ": " + e.getMessage(), "AUTH_LOGIN_ERROR");
        }
    }
    
    public void getUtenteCompleto(Utente u) throws AuthException
    {
    	try 
    	{
			utenteDAO.getCompleto(u);
		} 
    	catch (SQLException e) 
    	{
    		throw new AuthException("Errore durante il recupero dell'utente" + e.getMessage(), "AUTH_LOGIN_ERROR");
		}
    }
    
    
    public int getRuoloIdByCodice(String codice) throws AuthException
    {
    	try 
    	{
			return ruoloDAO.findByCodice(codice);
		} 
    	catch (SQLException e) 
    	{
			throw new AuthException("Errore durante il recupero dell ruolo" + e.getMessage(),"AUTH_ROLE_ERROR");
		}
    }
    
    
    public boolean isValidRegimeFiscale(int regimeFiscaleId) throws AuthException 
    {
    	try 
    	{
			return medicoDAO.findRegimeFiscaleById(regimeFiscaleId).isPresent();	
		} 
    	catch (SQLException e)
    	{
    		throw new AuthException("Errore durante il recupero del regime fiscale" + e.getMessage(),"AUTH_REGIME_ERROR");
		}
    }
  

    public int registraPaziente(Paziente p)  throws AuthException 
    {
        try 
        {
            return pazienteDAO.insert(p);                 
        } 
        catch (SQLException e) 
        {
           
           throw new AuthException("Errore durante la registrazione del paziente: " + e.getMessage(), "AUTH_REGISTRA_PAZIENTE_ERROR");
        } 
    }
    
    public int registraMedico(Medico m) throws AuthException 
    {
        m.setStatoVerifica(Medico.StatoVerifica.IN_ATTESA); 
        try 
        {
            return medicoDAO.insert(m); 
        } 
        catch (SQLException e) 
        {
            throw new AuthException("Errore durante la registrazione del medico: " + e.getMessage(), "AUTH_REGISTRA_MEDICO_ERROR");
        }
    }
    
    public List<TipoCertificato> findAllTipiCerticato() throws AuthException
    {
    	try 
    	{
			return certificatoDAO.tipoCertificatofindAll();
		} 
    	catch (SQLException e) 
    	{
    		throw new AuthException("Errore durante il recupero dei tipi di certificati: ", "AUTH_TIPIC_ERROR");
		}
    }
    
    
    public List<RegimeFiscale> findAllRegimeFiscale () throws AuthException
    {
    	try 
    	{
			return medicoDAO.findAllRegimeFiscale();
		} 
    	catch (SQLException e) 
    	{
    		throw new AuthException("Errore durante il recupero dei regimi fiscali supportati: ", "AUTH_REGFIS_ERROR");
		}
    }
    
    public void inserisciCertificati(int medicoId, List<Certificato> certificati) throws AuthException
    {
    	for(Certificato c : certificati)
    	{
    		try 
    		{
				certificatoDAO.insert(c);
			} 
    		catch (SQLException e) 
    		{
				
			}
    	}
    }
    

    
    public int registraAdmin(Amministratore a) throws AuthException 
    {
        try 
        {
            return adminDAO.insert(a); 
           
        }
        catch (SQLException e) 
        {
            throw new AuthException("Errore durante la registrazione dell'amministratore: " + e.getMessage(), "AUTH_REGISTRA_ADMIN_ERROR");
        }
    }

    public boolean cambiaPassword(String email, String vecchia, String nuova) throws AuthException 
    {
        try
        {
            Utente u = utenteDAO.findByEmail(email).get();
            if (u != null && PasswordUtils.checkPassword(vecchia, u.getPassword())) 
            {
                utenteDAO.updatePassword(u.getId(), nuova);
                return true;
            }

            return false;
        }
        catch(SQLException e)
        {
            throw new AuthException("Errore durante il cambio password per l'email " + email + ": " + e.getMessage(), "AUTH_CAMBIA_PASSWORD_ERROR");
        }
       
    }
}
