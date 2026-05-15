package it.mediclick.model.bean;

import java.io.Serializable;

public class Medico implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum StatoVerifica 
    {
        IN_ATTESA("In attesa"),
        APPROVATO("Approvato"),
        RIFIUTATO("Rifiutato"),
        SOSPESO("Sospeso");

        private final String label;
        StatoVerifica(String label) { this.label = label; }
        public String getLabel() { return label; }
        
        public static StatoVerifica fromString(String statoStr)
        {
        	if(statoStr == null)
        		return null;
        	
	        for (Medico.StatoVerifica s : Medico.StatoVerifica.values()) 
	        {
	            if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr)) 
	            {
	               return s;
	            }
	        }
	        
	        return null;
        }  
    }

    private int id = -1;
    private String cognome;
    private String nome;
    private byte[] fotoprofilo;
    private String bio;
    private String pIva;
    private StatoVerifica statoVerifica;
   
    private Utente utente;

    private int regimeFiscaleId = -1;
    private RegimeFiscale regimeFiscale;

    public int getRegimeFiscaleId() 
    {
        return regimeFiscaleId;
    }

    public void setRegimeFiscaleId(int regimeFiscaleId) 
    {
        this.regimeFiscaleId = regimeFiscaleId;
    }

    public Medico()
    {
        this.statoVerifica = StatoVerifica.IN_ATTESA;
    }

    public int getId() 
    { 
    	return id; 
    }
    public void setId(int id) 
    { 
    	this.id = id; 
    }

    public String getCognome() 
    { 
    	return cognome; 
    }
    public void setCognome(String cognome) 
    {
    	this.cognome = cognome;
	}

    public String getNome()
    { 
    	return nome; 
    }
    
    public void setNome(String nome)
    { 
    	this.nome = nome; 
    }

    public byte[] getFotoprofilo() 
    { 
    	return fotoprofilo; 
    }
    
    public void setFotoprofilo(byte[] fotoprofilo) 
    { 
    	this.fotoprofilo = fotoprofilo;
    }

    public String getBio() 
    { 
    	return bio; 
    }
    
    public void setBio(String bio) 
    { 
    	this.bio = bio;
    }

    public String getpIva() 
    { 
    	return pIva; 
    }
    
    public void setpIva(String pIva) 
    { 
    	this.pIva = pIva; 
    }

    public StatoVerifica getStatoVerifica() 
    { 
    	return statoVerifica; 
    }
    
    public void setStatoVerifica(StatoVerifica statoVerifica) 
    { 
    	this.statoVerifica = statoVerifica; 
    }

    public Utente getUtente() 
    { 
    	return utente; 
    }
    public void setUtente(Utente utente) 
    { 
    	this.utente = utente; 
    }

    public RegimeFiscale getRegimeFiscale() 
    { 
    	return regimeFiscale; 
    }
    
    public void setRegimeFiscale(RegimeFiscale regimeFiscale) 
    { 
    	this.regimeFiscale = regimeFiscale; 
    }

    
    public String getNomeCompleto() 
    {
        return cognome + " " + nome;
    }

    @Override
    public String toString()
    {
        return "Medico{id=" + id + ", nome='" + getNomeCompleto() + "', stato=" + statoVerifica + "}";
    }
}
