package it.mediclick.model.DTO;

import java.io.Serializable;

public class StatistichePiattaformaDTO implements Serializable
{
	private static final long serialVersionUID = 1L;
	private int totaleMedici;
	private int mediciDaApprovare;
	private double guadagniPiattaforma;

	public StatistichePiattaformaDTO()
	{
	}

	public StatistichePiattaformaDTO(int totaleMedici, int mediciDaApprovare, double guadagniPiattaforma)
	{
		this.totaleMedici = totaleMedici;
		this.mediciDaApprovare = mediciDaApprovare;
		this.guadagniPiattaforma = guadagniPiattaforma;
	}

	public int getTotaleMedici()
	{
		return totaleMedici;
	}

	public void setTotaleMedici(int totaleMedici)
	{
		this.totaleMedici = totaleMedici;
	}

	public int getMediciDaApprovare()
	{
		return mediciDaApprovare;
	}

	public void setMediciDaApprovare(int mediciDaApprovare)
	{
		this.mediciDaApprovare = mediciDaApprovare;
	}

	public double getGuadagniPiattaforma()
	{
		return guadagniPiattaforma;
	}

	public void setGuadagniPiattaforma(double guadagniPiattaforma)
	{
		this.guadagniPiattaforma = guadagniPiattaforma;
	}
}