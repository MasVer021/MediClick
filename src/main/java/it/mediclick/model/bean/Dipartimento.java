package it.mediclick.model.bean;

import java.io.Serializable;

public class Dipartimento implements Serializable
{

	private static final long serialVersionUID = 1L;

	private int id = -1;
	private String nome;

	public Dipartimento()
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

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	@Override
	public String toString()
	{
		return "Dipartimento{id=" + id + ", nome='" + nome + "'}";
	}
}
