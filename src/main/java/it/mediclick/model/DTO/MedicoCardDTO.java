package it.mediclick.model.DTO;

import java.time.LocalDateTime;

import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Medico;

public class MedicoCardDTO
{
	private Medico medico;
	private double valoreRecensioni;
	private int numeroRecensioni;
	private Categoria categoria;
	private LocalDateTime primaDisponibilita;
	private String indirizzo;
	private double costo;

	public Medico getMedico()
	{
		return medico;
	}

	public void setMedico(Medico medico)
	{
		this.medico = medico;
	}

	public double getValoreRecensioni()
	{
		return valoreRecensioni;
	}

	public void setValoreRecensioni(double valoreRecensioni)
	{
		this.valoreRecensioni = valoreRecensioni;
	}

	public int getNumeroRecensioni()
	{
		return numeroRecensioni;
	}

	public void setNumeroRecensioni(int numeroRecensioni)
	{
		this.numeroRecensioni = numeroRecensioni;
	}

	public Categoria getCategoria()
	{
		return categoria;
	}

	public void setCategoria(Categoria categoria)
	{
		this.categoria = categoria;
	}

	public LocalDateTime getPrimaDisponibilita()
	{
		return primaDisponibilita;
	}

	public void setPrimaDisponibilita(LocalDateTime primaDisponibilita)
	{
		this.primaDisponibilita = primaDisponibilita;
	}

	public String getIndirizzo()
	{
		return indirizzo;
	}

	public void setIndirizzo(String indirizzo)
	{
		this.indirizzo = indirizzo;
	}

	public double getCosto()
	{
		return costo;
	}

	public void setCosto(double costo)
	{
		this.costo = costo;
	}

}
