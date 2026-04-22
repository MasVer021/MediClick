package it.mediclick.model.bean;

import java.io.Serializable;

/**
 * Specializzazione di Utente - condivide lo stesso ID (PK = FK su Utente).
 */
public class Amministratore implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;           // FK -> Utente(ID)
    private Integer dipartimentoId;

    // Relazioni opzionali
    private Utente utente;
    private Dipartimento dipartimento;

    public Amministratore() {}

    public Amministratore(int id, Integer dipartimentoId) {
        this.id = id;
        this.dipartimentoId = dipartimentoId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getDipartimentoId() { return dipartimentoId; }
    public void setDipartimentoId(Integer dipartimentoId) { this.dipartimentoId = dipartimentoId; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public Dipartimento getDipartimento() { return dipartimento; }
    public void setDipartimento(Dipartimento dipartimento) { this.dipartimento = dipartimento; }

    @Override
    public String toString() {
        return "Amministratore{id=" + id + ", dipartimentoId=" + dipartimentoId + "}";
    }
}
