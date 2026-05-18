package it.mediclick.service;

import java.sql.SQLException;
import java.util.List;

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
			Utente u = utenteDAO.findByEmail(email).orElseThrow(() -> new AuthException("Email o password non valide.", "AUTH_BAD_CREDENTIALS"));
			if (PasswordUtils.checkPassword(password, u.getPassword()))
			{
				return u;
			}
			else
			{
				throw new AuthException("Email o password non valide.", "AUTH_BAD_CREDENTIALS");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new AuthException("Errore di sistema durante l'accesso.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new AuthException("Errore del sistema nel recupero dei dettagli utente.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new AuthException("Errore del sistema nel recupero del ruolo.", "SYS_DATABASE_ERROR");
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
			e.printStackTrace();
			throw new AuthException("Errore del sistema nella verifica del regime fiscale.", "SYS_DATABASE_ERROR");
		}
	}

	public int registraPaziente(Paziente p) throws AuthException
	{
		try
		{
			return pazienteDAO.insert(p);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new AuthException("Errore durante la registrazione del paziente. L'email potrebbe essere già registrata.", "AUTH_REGISTRAZIONE_FALLITA");
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
			e.printStackTrace();
			throw new AuthException("Errore durante la registrazione del medico. L'email o la P.IVA potrebbero essere già registrate.", "AUTH_REGISTRAZIONE_FALLITA");
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
			e.printStackTrace();
			throw new AuthException("Errore del sistema nel recupero dei tipi di certificato.", "SYS_DATABASE_ERROR");
		}
	}

	public List<RegimeFiscale> findAllRegimeFiscale() throws AuthException
	{
		try
		{
			return medicoDAO.findAllRegimeFiscale();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new AuthException("Errore del sistema nel recupero dei regimi fiscali.", "SYS_DATABASE_ERROR");
		}
	}

	public void inserisciCertificati(int medicoId, List<Certificato> certificati) throws AuthException
	{
		for (Certificato c : certificati)
		{
			try
			{
				certificatoDAO.insert(c);
			}
			catch (SQLException e)
			{
				e.printStackTrace();
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
			e.printStackTrace();
			throw new AuthException("Errore durante la registrazione dell'amministratore.", "AUTH_REGISTRAZIONE_FALLITA");
		}
	}

	public boolean cambiaPassword(String email, String vecchia, String nuova) throws AuthException
	{
		try
		{
			Utente u = utenteDAO.findByEmail(email).orElse(null);
			if (u != null && PasswordUtils.checkPassword(vecchia, u.getPassword()))
			{
				utenteDAO.updatePassword(u.getId(), nuova);
				return true;
			}
			throw new AuthException("La vecchia password inserita non è corretta.", "AUTH_BAD_CREDENTIALS");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			throw new AuthException("Errore del sistema durante il cambio password.", "SYS_DATABASE_ERROR");
		}
	}
}
