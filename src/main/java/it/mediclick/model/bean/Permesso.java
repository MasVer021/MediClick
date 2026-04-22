package it.mediclick.model.bean;

import java.io.Serializable;

public class Permesso implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String codice;
    private String descrizione;

    public Permesso() {}

    public Permesso(int id, String codice, String descrizione) {
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

    @Override
    public String toString() {
        return "Permesso{id=" + id + ", codice='" + codice + "', descrizione='" + descrizione + "'}";
    }
}
