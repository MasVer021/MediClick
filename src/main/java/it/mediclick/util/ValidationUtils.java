package it.mediclick.util;

import it.mediclick.exception.AuthException;

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
            throw new IllegalArgumentException("Il campo '" + nomeParametro + "' non può essere vuoto.");
        }
        return valore.trim(); 
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
    
    public static String parsePassword(String password,String passwordRipetuta) throws IllegalArgumentException
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
}
