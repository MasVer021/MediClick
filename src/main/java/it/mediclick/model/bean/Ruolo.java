package it.mediclick.model.bean;

import java.io.Serializable;
import java.util.List;

public class Ruolo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String codice;
    private String descrizione;

    // Relazione N:N con Permesso (opzionale, per uso con ORM/DAO)
    private List<Permesso> permessi;

    public Ruolo() {}

    public Ruolo(int id, String codice, String descrizione) {
        this.id = id;
        this.codice = codice;
        this.descrizione = descrizione;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public List<Permesso> getPermessi() { return permessi; }
    public void setPermessi(List<Permesso> permessi) { this.permessi = permessi; }

    @Override
    public String toString() {
        return "Ruolo{id=" + id + ", codice='" + codice + "', descrizione='" + descrizione + "'}";
    }
}
