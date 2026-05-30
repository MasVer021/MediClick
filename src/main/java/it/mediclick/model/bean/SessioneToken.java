package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class SessioneToken implements Serializable
{

	private static final long serialVersionUID = 1L;

	int Id;
	int UtenteId;
	String token;
	LocalDate scadenza;

	public int getId()
	{
		return Id;
	}

	public void setId(int id)
	{
		Id = id;
	}

	public int getUtenteId()
	{
		return UtenteId;
	}

	public void setUtenteId(int utenteId)
	{
		UtenteId = utenteId;
	}

	public String getToken()
	{
		return token;
	}

	public void setToken(String token)
	{
		this.token = token;
	}

	public LocalDate getScadenza()
	{
		return scadenza;
	}

	public void setScadenza(LocalDate scadenza)
	{
		this.scadenza = scadenza;
	}

}
