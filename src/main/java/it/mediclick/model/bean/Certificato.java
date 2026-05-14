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
        SCADUTO("Scaduto");
    	
    	
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

    private int id;
    private String nomeFile;
    private byte[] datiDocumento;
    private Stato stato;
    private String mimeType;
    private LocalDateTime dataCaricamento;
    private LocalDateTime dataScadenza;
    
    private Medico medico;
    private TipoCertificato tipoCertificato;
    private Amministratore approvatore;

    public Certificato() 
    {
        this.stato = Stato.IN_REVISIONE;
    }

    public Integer getId() 
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
        return "Certificato{id=" + id + ", medicoId=" + getMedico().getId() +", nomeFile='" + nomeFile + "', stato=" + stato + "}";
    }
}
