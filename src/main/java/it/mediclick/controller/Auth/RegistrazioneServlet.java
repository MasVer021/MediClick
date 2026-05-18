package it.mediclick.controller.Auth;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.mediclick.exception.AuthException;
import it.mediclick.exception.ErrorInfo;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Paziente;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.model.bean.Utente;
import it.mediclick.service.AutenticazioneService;
import it.mediclick.util.Contex;
import it.mediclick.util.ValidationUtils;

@WebServlet("/singin")

@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 10, maxRequestSize = 1024 * 1024 * 50)

public class RegistrazioneServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	private AutenticazioneService autenticazioneService;

	public void init() throws ServletException
	{
		Contex contex = (Contex) getServletContext().getAttribute("contex");
		autenticazioneService = new AutenticazioneService(contex);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		boolean isMedico = "true".equalsIgnoreCase(request.getParameter("medico"));

		try
		{
			List<RegimeFiscale> regimiFiscali = autenticazioneService.findAllRegimeFiscale();
			request.setAttribute("regimiFiscali", regimiFiscali);
		}
		catch (AuthException e)
		{
			throw new ServletException("Errore nella comunicazione con il database", e);
		}

		request.setAttribute("isMedico", isMedico);
		request.getRequestDispatcher("/WEB-INF/view/singin.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			boolean isMedico = "true".equalsIgnoreCase(request.getParameter("medico"));

			if (!isMedico)
			{
				Paziente p = mapPaziente(request);
				autenticazioneService.registraPaziente(p);
			}
			else
			{
				Medico m = mapMedico(request);
				autenticazioneService.registraMedico(m);
			}

			response.sendRedirect(request.getContextPath() + "/login");
		}
		catch (AuthException e)
		{
			request.setAttribute("errore", new ErrorInfo(e));
			request.getRequestDispatcher("/WEB-INF/view/singin.jsp").forward(request, response);
		}
	}

	private Utente mapUtente(HttpServletRequest request, int ruoloId) throws AuthException
	{
		Utente u = new Utente();

		try
		{
			String email = ValidationUtils.parseEmail(request.getParameter("email"), "email");
			String password = ValidationUtils.parsePassword(request.getParameter("password"), request.getParameter("passwordRipetuta"));

			u.setEmail(email);
			u.setPassword(password);
			u.setDataIscrizione(LocalDate.now());
			u.setRuoloId(ruoloId);

			return u;
		}
		catch (IllegalArgumentException e)
		{
			throw new AuthException(e.getMessage(), "CREDENTIAL_ERROR");
		}

	}

	private Medico mapMedico(HttpServletRequest request) throws AuthException
	{

		Medico m = new Medico();

		String nome = request.getParameter("nome");
		String cognome = (String) request.getParameter("cognome");
		String bio = (String) request.getParameter("Bio");
		String partitaIva = (String) request.getParameter("PIva");
		String regimeFiscaleStr = (String) request.getParameter("RegimeFiscale");

		try
		{
			Part fotoPart = request.getPart("fotoprofilo");
			if (fotoPart != null && fotoPart.getSize() > 0)
			{
				m.setFotoprofilo(fotoPart.getInputStream().readAllBytes());
			}
		}
		catch (Exception e)
		{
			throw new AuthException("Errore durante il caricamento della foto profilo.", "REG_FOTO_ERROR");
		}

		int ruoloId = autenticazioneService.getRuoloIdByCodice("MEDICO");

		if (nome == null || nome.isBlank())
		{
			throw new AuthException("Il nome è obbligatorio.", "REG_NOME_BLANK");
		}

		if (cognome == null || cognome.isBlank())
		{
			throw new AuthException("Il cognome è obbligatorio.", "REG_COGNOME_BLANK");
		}

		if (bio == null || bio.isBlank())
		{
			throw new AuthException("La bio è obbligatoria.", "REG_BIO_BLANK");
		}

		if (regimeFiscaleStr == null || regimeFiscaleStr.isBlank())
		{
			throw new AuthException("Il regime fiscale è obbligatoria.", "REGIME_BLANK");
		}

		if (partitaIva == null || partitaIva.isBlank())
		{
			throw new AuthException("La partita iva è obbligatoria.", "PIVA_BLANK");
		}

		if (!partitaIva.matches("^\\d{11}$"))
		{
			throw new AuthException("Formato partita iva non valido.", "PIVA_INVALID");
		}

		int regime;

		try
		{
			regime = Integer.parseInt(regimeFiscaleStr);
		}
		catch (NumberFormatException e)
		{
			throw new AuthException("Formato regime fiscale non valido.", "REGIME_INVALID_FORMAT");
		}

		if (!autenticazioneService.isValidRegimeFiscale(regime))
		{
			throw new AuthException("Regime fiscale non valido.", "REGIME_INVALID");
		}

		m.setNome(nome);
		m.setCognome(cognome);
		m.setBio(bio);
		m.setpIva(partitaIva);
		m.setRegimeFiscaleId(regime);

		Utente u = mapUtente(request, ruoloId);

		m.setUtente(u);

		return m;
	}

	private Paziente mapPaziente(HttpServletRequest request) throws AuthException
	{
		Paziente p = new Paziente();

		String nome = (String) request.getParameter("nome");
		String cognome = (String) request.getParameter("cognome");
		String dataNascitaStr = (String) request.getParameter("DataNascita");
		LocalDate dataNascita;
		String numeroTelefono = (String) request.getParameter("telefono");
		String codiceFiscale = (String) request.getParameter("CF");

		int ruoloId = autenticazioneService.getRuoloIdByCodice("PAZIENTE");

		if (nome == null || nome.isBlank())
		{
			throw new AuthException("Il nome è obbligatorio.", "REG_NOME_BLANK");
		}
		if (cognome == null || cognome.isBlank())
		{
			throw new AuthException("Il cognome è obbligatorio.", "REG_COGNOME_BLANK");
		}
		if (dataNascitaStr == null || dataNascitaStr.isBlank())
		{
			throw new AuthException("La data di nascita è obbligatoria.", "REG_DATA_NASCITA_BLANK");
		}

		try
		{
			dataNascita = LocalDate.parse(dataNascitaStr);
		}
		catch (DateTimeParseException e)
		{
			throw new AuthException("Formato data non valido. Usa il formato AAAA-MM-GG.", "REG_DATA_NASCITA_INVALID");
		}

		if (dataNascita.isAfter(LocalDate.now().minusYears(18)))
		{
			throw new AuthException("Devi avere almeno 18 anni per registrarti.", "REG_ETA_MINIMA");
		}
		if (numeroTelefono == null || numeroTelefono.isBlank())
		{
			throw new AuthException("Il numero di telefono è obbligatorio.", "REG_TELEFONO_BLANK");
		}
		if (!numeroTelefono.matches("^\\+?[0-9]{8,15}$"))
		{
			throw new AuthException("Formato telefono non valido.", "REG_TELEFONO_INVALID");
		}
		if (codiceFiscale == null || codiceFiscale.isBlank())
		{
			throw new AuthException("Il codice fiscale è obbligatorio.", "REG_CF_BLANK");
		}
		if (!codiceFiscale.matches("^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$"))
		{
			throw new AuthException("Formato codice fiscale non valido.", "REG_CF_INVALID");
		}

		p.setNome(nome);
		p.setCognome(cognome);
		p.setDataNascita(dataNascita);
		p.setTelefono(numeroTelefono);
		p.setCodiceFiscale(codiceFiscale);

		Utente u = mapUtente(request, ruoloId);

		p.setUtente(u);

		return p;
	}

}
