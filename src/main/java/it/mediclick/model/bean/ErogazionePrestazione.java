package it.mediclick.model.bean;

import java.io.Serializable;

public class ErogazionePrestazione implements Serializable
{

	private static final long serialVersionUID = 1L;

	public enum Stato {
		ATTIVA("Attiva"), SOSPESA("Sospesa");

		public static Stato fromString(String statoStr)
		{
			if (statoStr == null)
				return null;

			for (ErogazionePrestazione.Stato s : ErogazionePrestazione.Stato.values())
			{
				if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr))
				{
					return s;
				}
			}

			return null;
		}

		private final String label;

		Stato(String label)
		{
			this.label = label;
		}

		public String getLabel()
		{
			return label;
		}
	}

	private int id = -1;
	private double prezzoLordoListino;

	private Integer durata;
	private Stato stato;

	private int medicoId = -1;
	private Medico medico;

	private int catalogoPrestazioniId = -1;
	private CatalogoPrestazioni catalogoPrestazioni;

	private int studioId = -1;
	private Studio studio;

	public int getMedicoId()
	{
		return medicoId;
	}

	public void setMedicoId(int medicoId)
	{
		this.medicoId = medicoId;
	}

	public int getCatalogoPrestazioniId()
	{
		return catalogoPrestazioniId;
	}

	public void setCatalogoPrestazioniId(int catalogoPrestazioniId)
	{
		this.catalogoPrestazioniId = catalogoPrestazioniId;
	}

	public int getStudioId()
	{
		return studioId;
	}

	public void setStudioId(int studioId)
	{
		this.studioId = studioId;
	}

	public ErogazionePrestazione()
	{
		this.stato = Stato.ATTIVA;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public double getPrezzoLordoListino()
	{
		return prezzoLordoListino;
	}

	public void setPrezzoLordoListino(double prezzoLordoListino)
	{
		this.prezzoLordoListino = prezzoLordoListino;
	}

	public Integer getDurata()
	{
		return durata;
	}

	public void setDurata(Integer durata)
	{
		this.durata = durata;
	}

	public Stato getStato()
	{
		return stato;
	}

	public void setStato(Stato stato)
	{
		this.stato = stato;
	}

	public Medico getMedico()
	{
		return medico;
	}

	public void setMedico(Medico medico)
	{
		this.medico = medico;
	}

	public CatalogoPrestazioni getCatalogoPrestazioni()
	{
		return catalogoPrestazioni;
	}

	public void setCatalogoPrestazioni(CatalogoPrestazioni catalogoPrestazioni)
	{
		this.catalogoPrestazioni = catalogoPrestazioni;
	}

	public Studio getStudio()
	{
		return studio;
	}

	public void setStudio(Studio studio)
	{
		this.studio = studio;
	}

	@Override
	public String toString()
	{
		return "ErogazionePrestazione{id=" + id + ", medicoId=" + getMedicoId() + ", prezzoLordo=" + prezzoLordoListino + ", durata=" + durata + "min, stato=" + stato + "}";
	}
}
