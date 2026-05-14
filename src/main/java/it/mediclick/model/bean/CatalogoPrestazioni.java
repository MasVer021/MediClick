package it.mediclick.model.bean;

import java.io.Serializable;

public class CatalogoPrestazioni implements Serializable 
{

    private static final long serialVersionUID = 1L;

    public enum Stato
    {
        ATTIVA("Attiva"),
        DISATTIVA("Disattiva");

        private final String label;
        
        
        
        
        public static Stato fromString(String statoStr)
        {
        	if(statoStr == null)
        		return null;
        	
	        for (CatalogoPrestazioni.Stato s : CatalogoPrestazioni.Stato.values()) 
	        {
	            if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr)) 
	            {
	               return s;
	            }
	        }
	        
	        return null;
        }
        
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
    private String nome;
    private Stato stato;
    private String descrizione;
    
    private Categoria categoria;

    public CatalogoPrestazioni() 
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

    public String getNome() 
    { 
    	return nome; 
    }
    
    public void setNome(String nome) 
    { 
    	this.nome = nome;
    }

    public Stato getStato() 
    { 
    	return stato; 
    }
    
    public void setStato(Stato stato) 
    { 
    	this.stato = stato; 
    }

    public String getDescrizione() 
    { 
    	return descrizione; 
    }
    
    public void setDescrizione(String descrizione) 
    { 
    	this.descrizione = descrizione; 
    }

    public Categoria getCategoria() 
    { 
    	return categoria; 
    }
    
    public void setCategoria(Categoria categoria) 
    { 
    	this.categoria = categoria; 
    }

    @Override
    public String toString() 
    {
        return "CatalogoPrestazioni{id=" + id + ", nome='" + nome + "', stato=" + stato + "}";
    }
}
