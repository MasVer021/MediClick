package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ImpostazioniSistema implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id = -1;
    private String chiave;
    private String valore;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;  

    private int amministratoreId = -1;
    private Amministratore amministratore;

    public int getAmministratoreId()
    {
        return amministratoreId;
    }

    public void setAmministratoreId(int amministratoreId) 
    {
        this.amministratoreId = amministratoreId;
    }

    public ImpostazioniSistema()
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

    public String getChiave()
    {
        return chiave;
    }

    public void setChiave(String chiave)
    {
        this.chiave = chiave;
    }

    public String getValore()
    {
        return valore;
    }

    public void setValore(String valore)
    {
        this.valore = valore;
    }

    public LocalDateTime getDataInizio()
    {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio)
    {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine()
    {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine)
    {
        this.dataFine = dataFine;
    }

    public Amministratore getAmministratore()
    {
        return amministratore;
    }

    public void setAmministratore(Amministratore amministratore)
    {
        this.amministratore = amministratore;
    }

    @Override
    public String toString()
    {
        return "ImpostazioniSistema{id=" + id + ", chiave='" + chiave + "', valore='" + valore + "'}";
    }
}
