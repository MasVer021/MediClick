package it.mediclick.model.bean;

import java.io.Serializable;

/**
 * Specializzazione di Utente - condivide lo stesso ID (PK = FK su Utente).
 */
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

    private int id;          // FK -> Utente(ID)
    private String cognome;
    private String nome;
    private String bio;
    private String pIva;
    private StatoVerifica statoVerifica;
    private Integer regimeFiscaleId;

    // Relazioni opzionali
    private Utente utente;
    private RegimeFiscale regimeFiscale;

    public Medico() {
        this.statoVerifica = StatoVerifica.IN_ATTESA;
    }

    public Medico(int id, String cognome, String nome, String bio, String pIva,
                  StatoVerifica statoVerifica, Integer regimeFiscaleId) {
        this.id = id;
        this.cognome = cognome;
        this.nome = nome;
        this.bio = bio;
        this.pIva = pIva;
        this.statoVerifica = statoVerifica;
        this.regimeFiscaleId = regimeFiscaleId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getpIva() { return pIva; }
    public void setpIva(String pIva) { this.pIva = pIva; }

    public StatoVerifica getStatoVerifica() { return statoVerifica; }
    public void setStatoVerifica(StatoVerifica statoVerifica) { this.statoVerifica = statoVerifica; }

    public Integer getRegimeFiscaleId() { return regimeFiscaleId; }
    public void setRegimeFiscaleId(Integer regimeFiscaleId) { this.regimeFiscaleId = regimeFiscaleId; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public RegimeFiscale getRegimeFiscale() { return regimeFiscale; }
    public void setRegimeFiscale(RegimeFiscale regimeFiscale) { this.regimeFiscale = regimeFiscale; }

    /** Nome completo: Cognome Nome */
    public String getNomeCompleto() {
        return cognome + " " + nome;
    }

    @Override
    public String toString() {
        return "Medico{id=" + id + ", nome='" + getNomeCompleto() + "', stato=" + statoVerifica + "}";
    }
}
