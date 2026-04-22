package it.mediclick.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class RegimeFiscale implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private BigDecimal aliquotaDefault;
    private String descrizione;

    public RegimeFiscale() {}

    public RegimeFiscale(int id, String nome, BigDecimal aliquotaDefault, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.aliquotaDefault = aliquotaDefault;
        this.descrizione = descrizione;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public BigDecimal getAliquotaDefault() { return aliquotaDefault; }
    public void setAliquotaDefault(BigDecimal aliquotaDefault) { this.aliquotaDefault = aliquotaDefault; }

    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    @Override
    public String toString() {
        return "RegimeFiscale{id=" + id + ", nome='" + nome + "', aliquotaDefault=" + aliquotaDefault + "}";
    }
}
