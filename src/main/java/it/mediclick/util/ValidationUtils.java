package it.mediclick.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

public class ValidationUtils
{

	public static int parseInt(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il parametro '" + nomeParametro + "' è obbligatorio.");
		}
		try
		{
			return Integer.parseInt(valore.trim());
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Il parametro '" + nomeParametro + "' deve essere un numero intero valido.");
		}
	}

	public static int parseInt(String valore, int valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}
		try
		{
			return Integer.parseInt(valore.trim());
		}
		catch (NumberFormatException e)
		{
			return valoreDiDefault;
		}
	}

	public static double parseDouble(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il parametro '" + nomeParametro + "' è obbligatorio.");
		}
		try
		{

			return Double.parseDouble(valore.trim().replace(",", "."));
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException("Il parametro '" + nomeParametro + "' deve essere un numero decimale valido.");
		}
	}

	public static double parseDouble(String valore, double valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}
		try
		{
			return Double.parseDouble(valore.trim().replace(",", "."));
		}
		catch (NumberFormatException e)
		{
			return valoreDiDefault;
		}
	}

	public static String parseString(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' è obbligatorio.");
		}
		return valore.trim();
	}

	public static Boolean parseBoolean(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' è obbligatorio.");
		}

		return Boolean.parseBoolean(valore);
	}

	public static Boolean parseBoolean(String valore, boolean valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}

		return Boolean.parseBoolean(valore);
	}

	public static String parseStringOpz(String valore, String valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}
		return valore.trim();
	}

	public static String parseNumeroTelefono(String numeroTelefono, String nomeParametro) throws IllegalArgumentException
	{
		if (numeroTelefono == null || numeroTelefono.isBlank())
		{
			throw new IllegalArgumentException("Il numero di telefono è obbligatorio.");
		}
		if (!numeroTelefono.matches("^\\+?[0-9]{8,15}$"))
		{
			throw new IllegalArgumentException("Formato telefono non valido.");
		}

		return numeroTelefono;
	}

	public static String parsePassword(String password, String passwordRipetuta) throws IllegalArgumentException
	{
		if (password == null || password.isBlank())
		{
			throw new IllegalArgumentException("La password è obbligatoria.");
		}
		if (password.length() < 8)
		{
			throw new IllegalArgumentException("La password deve essere di almeno 8 caratteri.");
		}
		if (!password.equals(passwordRipetuta))
		{
			throw new IllegalArgumentException("Le password non coincidono.");
		}

		return password;
	}

	public static String parseEmail(String valore, String nomeParametro) throws IllegalArgumentException
	{
		valore = parseString(valore, nomeParametro);

		if (!valore.matches("^[\\w.+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}$"))
		{
			throw new IllegalArgumentException("Formato " + nomeParametro + " non valido.");
		}

		return valore;
	}

	public static String parsePIva(String valore) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("La partita iva è obbligatoria.");
		}
		if (!valore.matches("^\\d{11}$"))
		{
			throw new IllegalArgumentException("Formato partita iva non valido.");
		}

		return valore;
	}

	public static byte[] parseByteArray(byte[] valore, String nomeParametro)
	{
		if (valore == null || valore.length <= 0)
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' è obbligatorio.");
		}

		return valore;
	}

	public static LocalDate parseLocalDateOpz(String valore, LocalDate valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}
		try
		{
			return LocalDate.parse(valore);
		}
		catch (DateTimeParseException e)
		{
			return valoreDiDefault;
		}
	}

	public static LocalDate parseLocalDate(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' non può essere vuoto.");
		}
		try
		{
			return LocalDate.parse(valore);
		}
		catch (DateTimeParseException e)
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' non può essere vuoto.");
		}
	}

	public static LocalTime parseLocalTime(String valore, String nomeParametro) throws IllegalArgumentException
	{
		if (valore == null || valore.isBlank())
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' non può essere vuoto.");
		}
		try
		{
			return LocalTime.parse(valore);
		}
		catch (DateTimeParseException e)
		{
			throw new IllegalArgumentException("Il campo '" + nomeParametro + "' non può essere vuoto.");
		}
	}

	public static LocalDateTime parseLocalDateTime(String valore, LocalDateTime valoreDiDefault)
	{
		if (valore == null || valore.isBlank())
		{
			return valoreDiDefault;
		}
		try
		{
			return LocalDateTime.parse(valore);
		}
		catch (DateTimeParseException e)
		{
			return valoreDiDefault;
		}
	}

}
