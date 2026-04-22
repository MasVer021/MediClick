package it.mediclick.model.bean;

import java.io.Serializable;

public class TipoCertificato implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private boolean obbligatorio;

    public TipoCertificato() {
        this.obbligatorio = false;
    }

    public TipoCertificato(int id, String nome, boolean obbligatorio) {
        this.id = id;
        this.nome = nome;
        this.obbligatorio = obbligatorio;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public boolean isObbligatorio() { return obbligatorio; }
    public void setObbligatorio(boolean obbligatorio) { this.obbligatorio = obbligatorio; }

    @Override
    public String toString() {
        return "TipoCertificato{id=" + id + ", nome='" + nome + "', obbligatorio=" + obbligatorio + "}";
    }
}
