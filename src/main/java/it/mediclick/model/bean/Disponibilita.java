package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Disponibilita implements Serializable
{

    private static final long serialVersionUID = 1L;

    public enum Stato 
    {
        DISPONIBILE("Disponibile"),
        PRENOTATA("Prenotata"),
        BLOCCATA("Bloccata"),
        COMPLETATA("Completata"),
        CANCELLATA("Cancellata");

    	 public static Stato fromString(String statoStr)
         {
         	if(statoStr == null)
         		return null;
         	
 	        for (Disponibilita.Stato s : Disponibilita.Stato.values()) 
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
    private LocalDateTime dataOraInizio;
    private LocalDateTime dataOraFine;
    private Stato stato;
    private LocalDateTime timestampBlocco;
        
    private Medico medico;
    private Studio studio;
    private Paziente paziente;

    public Paziente getPaziente() 
    {
		return paziente;
	}

	public void setPaziente(Paziente paziente) 
	{
		this.paziente = paziente;
	}

	public Disponibilita()
    {
        this.stato = Stato.DISPONIBILE;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public LocalDateTime getDataOraInizio()
    {
        return dataOraInizio;
    }

    public void setDataOraInizio(LocalDateTime dataOraInizio)
    {
        this.dataOraInizio = dataOraInizio;
    }

    public LocalDateTime getDataOraFine()
    {
        return dataOraFine;
    }

    public void setDataOraFine(LocalDateTime dataOraFine)
    {
        this.dataOraFine = dataOraFine;
    }

    public Stato getStato()
    {
        return stato;
    }

    public void setStato(Stato stato)
    {
        this.stato = stato;
    }

    public LocalDateTime getTimestampBlocco()
    {
        return timestampBlocco;
    }

    public void setTimestampBlocco(LocalDateTime timestampBlocco)
    {
        this.timestampBlocco = timestampBlocco;
    }

    public Medico getMedico()
    {
        return medico;
    }

    public void setMedico(Medico medico)
    {
        this.medico = medico;
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
        return "Disponibilita{id=" + id + ", medicoId=" + getMedico().getId() +", inizio=" + dataOraInizio + ", fine=" + dataOraFine+", stato=" + stato + "}";
    }
}
