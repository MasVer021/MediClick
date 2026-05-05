package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ImpostazioniSistema implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String chiave;
    private String valore;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private int updatedBy;   // FK -> Amministratore(ID)

    // Relazione opzionale
    private Amministratore amministratore;

    public ImpostazioniSistema() {}

    public ImpostazioniSistema(int id, String chiave, String valore,
                                LocalDateTime dataInizio, LocalDateTime dataFine, int updatedBy) {
        this.id = id;
        this.chiave = chiave;
        this.valore = valore;
        this.dataInizio = dataInizio;
        this.dataFine = dataFine;
        this.updatedBy = updatedBy;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getChiave() { return chiave; }
    public void setChiave(String chiave) { this.chiave = chiave; }

    public String getValore() { return valore; }
    public void setValore(String valore) { this.valore = valore; }

    public LocalDateTime getDataInizio() { return dataInizio; }
    public void setDataInizio(LocalDateTime dataInizio) { this.dataInizio = dataInizio; }

    public LocalDateTime getDataFine() { return dataFine; }
    public void setDataFine(LocalDateTime dataFine) { this.dataFine = dataFine; }

    public int getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(int updatedBy) { this.updatedBy = updatedBy; }

    public Amministratore getAmministratore() { return amministratore; }
    public void setAmministratore(Amministratore amministratore) { this.amministratore = amministratore; }

    @Override
    public String toString() {
        return "ImpostazioniSistema{id=" + id + ", chiave='" + chiave + "', valore='" + valore + "'}";
    }
}
