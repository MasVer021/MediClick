package it.mediclick.model.bean;

import java.io.Serializable;

public class ErogazionePrestazione implements Serializable
{

    private static final long serialVersionUID = 1L;

    public enum Stato
    {
        ATTIVA("Attiva"),
        SOSPESA("Sospesa");
        
        public static Stato fromString(String statoStr)
        {
            if(statoStr == null)
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

    private int id;
    private double prezzoLordoListino;
    
    /** Durata in minuti, approssimata alla mezz'ora successiva (es. 31 min → 60 min) */
    private Integer durata;
    private Stato stato;

    private Medico medico;
    private CatalogoPrestazioni catalogoPrestazioni;
    private Studio studio;

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
        return "ErogazionePrestazione{id=" + id + ", medicoId=" + getMedico().getId() +", prezzoLordo=" + prezzoLordoListino + ", durata=" + durata + "min, stato=" + stato + "}";
    }
}
