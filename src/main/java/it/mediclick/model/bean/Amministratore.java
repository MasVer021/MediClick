package it.mediclick.model.bean;

import java.io.Serializable;

public class Amministratore implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int id = -1;

	private Utente utente;

	private Dipartimento dipartimento;
	private int dipartimentoId = -1;

	public int getDipartimentoId()
	{
		return dipartimentoId;
	}

	public void setDipartimentoId(int dipartimentoId)
	{
		this.dipartimentoId = dipartimentoId;
	}

	public Amministratore()
	{

	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Utente getUtente()
	{
		return utente;
	}

	public void setUtente(Utente utente)
	{
		this.utente = utente;
	}

	public Dipartimento getDipartimento()
	{
		return dipartimento;
	}

	public void setDipartimento(Dipartimento dipartimento)
	{
		this.dipartimento = dipartimento;
	}

	@Override
	public String toString()
	{
		return "Amministratore{id=" + id + ", dipartimentoId=" + dipartimentoId + "}";
	}
}
