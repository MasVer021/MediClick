package it.mediclick.model.bean;

import java.io.Serializable;
import java.util.Objects;

 
public class Caratterizzato implements Serializable {

    private static final long serialVersionUID = 1L;

    private int ruoloId;
    private int permessoId;

    private Ruolo ruolo;
    private Permesso permesso;

    public Caratterizzato() {}

    public Integer getRuoloId() 
    { 
    	return ruolo.getId(); 
    }
    
    public void setRuolo(Ruolo ruolo) 
    { 
    	this.ruolo = ruolo; 
    }

    public Permesso getPermesso() 
    { 
    	return permesso; 
    }
    
    public void setPermesso(Permesso permesso) 
    { 
    	this.permesso = permesso; 
    }

    @Override
    public boolean equals(Object o) 
    {
        if (this == o) 
        	return true;
        if (!(o instanceof Caratterizzato)) 
        	return false;
        Caratterizzato that = (Caratterizzato) o;
        
        return ruoloId == that.ruoloId && permessoId == that.permessoId;
    }

    @Override
    public int hashCode() 
    {
        return Objects.hash(ruoloId, permessoId);
    }

    @Override
    public String toString() 
    {
        return "Caratterizzato{ruoloId=" + ruoloId + ", permessoId=" + permessoId + "}";
    }
}
