package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;



public class Prenotazione implements Serializable
{

    private static final long serialVersionUID = 1L;

    public enum Stato
    {
        CONFERMATA("Confermata"),
        COMPLETATA("Completata"),
        CANCELLATA("Cancellata"),
        RIMBORSATA("Rimborsata");
        
        public static Stato fromString(String statoStr)
        {
            if(statoStr == null)
                return null;
            
            for (Prenotazione.Stato s : Prenotazione.Stato.values()) 
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
    private Stato stato;
    private String metodoPagamento;
    private String idTransazioneEsterno;
    private double importoPagato;
    private double ricavoNettoMedicoEuro;
    private double trattenutaPiattaformaEuro;
    private double tasseStimateEuro;
    private LocalDateTime dataPagamento;
    private Paziente paziente;
    private Disponibilita disponibilita;
    private ErogazionePrestazione erogazionePrestazione;
    private CodiceSconto codiceSconto;

    public Prenotazione()
    {
        this.stato = Stato.CONFERMATA;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
    
    public Stato getStato()
    {
        return stato;
    }

    public void setStato(Stato stato)
    {
        this.stato = stato;
    }

    public String getMetodoPagamento()
    {
        return metodoPagamento;
    }

    public void setMetodoPagamento(String metodoPagamento)
    {
        this.metodoPagamento = metodoPagamento;
    }

    public String getIdTransazioneEsterno()
    {
        return idTransazioneEsterno;
    }

    public void setIdTransazioneEsterno(String idTransazioneEsterno)
    {
        this.idTransazioneEsterno = idTransazioneEsterno;
    }

    public double getImportoPagato()
    {
        return importoPagato;
    }

    public void setImportoPagato(double importoPagato)
    {
        this.importoPagato = importoPagato;
    }

    public double getRicavoNettoMedicoEuro()
    {
        return ricavoNettoMedicoEuro;
    }

    public void setRicavoNettoMedicoEuro(double ricavoNettoMedicoEuro)
    {
        this.ricavoNettoMedicoEuro = ricavoNettoMedicoEuro;
    }

    public double getTrattenutaPiattaformaEuro()
    {
        return trattenutaPiattaformaEuro;
    }

    public void setTrattenutaPiattaformaEuro(double trattenutaPiattaformaEuro)
    {
        this.trattenutaPiattaformaEuro = trattenutaPiattaformaEuro;
    }

    public double getTasseStimateEuro()
    {
        return tasseStimateEuro;
    }

    public void setTasseStimateEuro(double tasseStimateEuro)
    {
        this.tasseStimateEuro = tasseStimateEuro;
    }

    public LocalDateTime getDataPagamento()
    {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDateTime dataPagamento)
    {
        this.dataPagamento = dataPagamento;
    }

    public Paziente getPaziente()
    {
        return paziente;
    }

    public void setPaziente(Paziente paziente)
    {
        this.paziente = paziente;
    }

    public Disponibilita getDisponibilita()
    {
        return disponibilita;
    }

    public void setDisponibilita(Disponibilita disponibilita)
    {
        this.disponibilita = disponibilita;
    }

    public ErogazionePrestazione getErogazionePrestazione()
    {
        return erogazionePrestazione;
    }

    public void setErogazionePrestazione(ErogazionePrestazione erogazionePrestazione)
    {
        this.erogazionePrestazione = erogazionePrestazione;
    }

    public CodiceSconto getCodiceSconto()
    {
        return codiceSconto;
    }

    public void setCodiceSconto(CodiceSconto codiceSconto)
    {
        this.codiceSconto = codiceSconto;
    }

    @Override
    public String toString()
    {
        return "Prenotazione{id=" + id + ", pazienteId=" + getPaziente().getId() +", importo=" + importoPagato + ", stato=" + stato + "}";
    }
}
