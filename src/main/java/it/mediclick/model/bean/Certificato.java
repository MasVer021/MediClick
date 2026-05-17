package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;



public class Certificato implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Stato 
    {
        IN_REVISIONE("In revisione"),
        APPROVATO("Approvato"),
        RIFIUTATO("Rifiutato"),
        SCADUTO("Scaduto"),
    	ELIMINATO("Eliminato");
    	
    	
    	public static Stato fromString(String statoStr)
        {
        	if(statoStr == null)
        		return null;
        	
	        for (Certificato.Stato s : Certificato.Stato.values()) 
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
    private String nomeFile;
    private byte[] datiDocumento;
    private Stato stato;
    private String mimeType;
    private LocalDateTime dataCaricamento;
    private LocalDateTime dataScadenza;
    

    private int medicoId = -1;
    private Medico medico;

    private int tipoCertificatoId = -1;
    private TipoCertificato tipoCertificato;

    private int approvatoreId = -1;
    private Amministratore approvatore;

    public int getMedicoId() {
        return medicoId;
    }

    public void setMedicoId(int medicoId) 
    {
        this.medicoId = medicoId;
    }

    public int getTipoCertificatoId() 
    {
        return tipoCertificatoId;
    }

    public void setTipoCertificatoId(int tipoCertificatoId) 
    {
        this.tipoCertificatoId = tipoCertificatoId;
    }

    public int getApprovatoreId() 
    {
        return approvatoreId;
    }

    public void setApprovatoreId(int approvatoreId) 
    {
        this.approvatoreId = approvatoreId;
    }

    public Certificato() 
    {
        this.stato = Stato.IN_REVISIONE;
    }

    public int getId() 
    { 
    	return id; 
    }
    
    public void setId(int id) 
    { 
    	this.id = id; 
    }
    
    public String getNomeFile() 
    {
    	return nomeFile;
    }
    
    public void setNomeFile(String nomeFile) 
    { 
    	this.nomeFile = nomeFile; 
    }

    public byte[] getDatiDocumento() 
    { 
    	return datiDocumento; 
    }
    
    public void setDatiDocumento(byte[] datiDocumento) 
    { 
    	this.datiDocumento = datiDocumento; 
    }

    public Stato getStato() 
    {
		return stato; 
    }	
    
    public void setStato(Stato stato) 
    { 
    	this.stato = stato;
    }

    public String getMimeType() 
    { 
    	return mimeType; 
    }
    
    public void setMimeType(String mimeType)
    { 
    	this.mimeType = mimeType;
    }

    public LocalDateTime getDataCaricamento() 
    { 
    	return dataCaricamento; 
    }
    
    public void setDataCaricamento(LocalDateTime dataCaricamento) 
    { 
    	this.dataCaricamento = dataCaricamento; 
    }

    public LocalDateTime getDataScadenza() 
    { 
    	return dataScadenza; 
    }
    
    public void setDataScadenza(LocalDateTime dataScadenza) 
    { 
    	this.dataScadenza = dataScadenza; 
    }

    public Medico getMedico() 
    { 
    	return medico; 
    }
    
    public void setMedico(Medico medico) 
    { 
    	this.medico = medico; 
    }

    public TipoCertificato getTipoCertificato() 
    { 
    	return tipoCertificato; 
    }
    
    public void setTipoCertificato(TipoCertificato tipoCertificato) 
    { 
    	this.tipoCertificato = tipoCertificato; 
    }

    public Amministratore getApprovatore() 
    {
    	return approvatore; 
    }
    
    public void setApprovatore(Amministratore approvatore)
    { 
    	this.approvatore = approvatore; 
    }

    @Override
    public String toString() 
    {
        return "Certificato{id=" + id + ", medicoId=" + getMedicoId() +", nomeFile='" + nomeFile + "', stato=" + stato + "}";
    }
}
