package it.mediclick.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class CodiceSconto implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String codice;
    private BigDecimal valorePercentuale;
    private LocalDate dataScadenza;
    private boolean attivo;

    public CodiceSconto() {
        this.attivo = true;
    }

    public CodiceSconto(int id, String codice, BigDecimal valorePercentuale,
                        LocalDate dataScadenza, boolean attivo) {
        this.id = id;
        this.codice = codice;
        this.valorePercentuale = valorePercentuale;
        this.dataScadenza = dataScadenza;
        this.attivo = attivo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodice() { return codice; }
    public void setCodice(String codice) { this.codice = codice; }

    public BigDecimal getValorePercentuale() { return valorePercentuale; }
    public void setValorePercentuale(BigDecimal valorePercentuale) { this.valorePercentuale = valorePercentuale; }

    public LocalDate getDataScadenza() { return dataScadenza; }
    public void setDataScadenza(LocalDate dataScadenza) { this.dataScadenza = dataScadenza; }

    public boolean isAttivo() { return attivo; }
    public void setAttivo(boolean attivo) { this.attivo = attivo; }

    @Override
    public String toString() {
        return "CodiceSconto{id=" + id + ", codice='" + codice +
               "', valore=" + valorePercentuale + "%, attivo=" + attivo + "}";
    }
}
