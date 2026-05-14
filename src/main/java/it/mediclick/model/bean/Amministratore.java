package it.mediclick.model.bean;

import java.io.Serializable;
import java.util.Optional;


public class Amministratore implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;           // FK -> Utente(ID)
    private Integer dipartimentoId;

    private Utente utente;
    private Dipartimento dipartimento;

    public Amministratore() 
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


    public Utente getUtente()
    { 
    	return utente; 
    }
    
    public void setUtente(Utente utente) 
    { 
    	this.utente = utente; 
    }

    public Optional<Dipartimento> getDipartimento() 
    { 
    	return Optional.ofNullable(dipartimento); 
    }
    
    public void setDipartimento(Dipartimento dipartimento) 
    {
    	this.dipartimento = dipartimento; 
    }

    @Override
    public String toString() 
    {
        return "Amministratore{id=" + id + ", dipartimentoId=" + dipartimentoId + "}";
    }
}
