package it.mediclick.model.bean;

import java.io.Serializable;
import java.util.Objects;

/**
 * Rappresenta la tabella associativa N:N tra Ruolo e Permesso.
 * La chiave primaria è composta da (ruoloId, permessoId).
 */
public class Caratterizzato implements Serializable {

    private static final long serialVersionUID = 1L;

    private int ruoloId;
    private int permessoId;

    // Relazioni opzionali
    private Ruolo ruolo;
    private Permesso permesso;

    public Caratterizzato() {}

    public Caratterizzato(int ruoloId, int permessoId) {
        this.ruoloId = ruoloId;
        this.permessoId = permessoId;
    }

    public int getRuoloId() { return ruoloId; }
    public void setRuoloId(int ruoloId) { this.ruoloId = ruoloId; }

    public int getPermessoId() { return permessoId; }
    public void setPermessoId(int permessoId) { this.permessoId = permessoId; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    public Permesso getPermesso() { return permesso; }
    public void setPermesso(Permesso permesso) { this.permesso = permesso; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caratterizzato)) return false;
        Caratterizzato that = (Caratterizzato) o;
        return ruoloId == that.ruoloId && permessoId == that.permessoId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ruoloId, permessoId);
    }

    @Override
    public String toString() {
        return "Caratterizzato{ruoloId=" + ruoloId + ", permessoId=" + permessoId + "}";
    }
}
