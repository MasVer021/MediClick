package it.mediclick.model.DTO;

import java.util.List;

import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Studio;

public class ProfiloMedicoPubblicoDTO
{
	private Medico medico;
	private List<Studio> studi;
	private List<ErogazionePrestazione> prestazioni;
	private List<Disponibilita> disponibilita;

	public Medico getMedico()
	{
		return medico;
	}

	public void setMedico(Medico medico)
	{
		this.medico = medico;
	}

	public List<Studio> getStudi()
	{
		return studi;
	}

	public void setStudi(List<Studio> studi)
	{
		this.studi = studi;
	}

	public List<ErogazionePrestazione> getPrestazioni()
	{
		return prestazioni;
	}

	public void setPrestazioni(List<ErogazionePrestazione> prestazioni)
	{
		this.prestazioni = prestazioni;
	}

	public List<Disponibilita> getDisponibilita()
	{
		return disponibilita;
	}

	public void setDisponibilita(List<Disponibilita> disponibilita)
	{
		this.disponibilita = disponibilita;
	}
}
