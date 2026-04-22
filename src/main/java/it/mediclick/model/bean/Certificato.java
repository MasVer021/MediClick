package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Certificato implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Stato {
        IN_REVISIONE("In revisione"),
        APPROVATO("Approvato"),
        RIFIUTATO("Rifiutato"),
        SCADUTO("Scaduto");

        private final String label;
        Stato(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private int medicoId;
    private int tipoCertificatoId;
    private String nomeFile;
    private byte[] datiDocumento;
    private Stato stato;
    private String mimeType;
    private LocalDateTime dataCaricamento;
    private LocalDateTime dataScadenza;
    private Integer approvedBy;   // FK -> Amministratore(ID)

    // Relazioni opzionali
    private Medico medico;
    private TipoCertificato tipoCertificato;
    private Amministratore approvatore;

    public Certificato() {
        this.stato = Stato.IN_REVISIONE;
    }

    public Certificato(int id, int medicoId, int tipoCertificatoId, String nomeFile,
                       byte[] datiDocumento, Stato stato, String mimeType,
                       LocalDateTime dataCaricamento, LocalDateTime dataScadenza, Integer approvedBy) {
        this.id = id;
        this.medicoId = medicoId;
        this.tipoCertificatoId = tipoCertificatoId;
        this.nomeFile = nomeFile;
        this.datiDocumento = datiDocumento;
        this.stato = stato;
        this.mimeType = mimeType;
        this.dataCaricamento = dataCaricamento;
        this.dataScadenza = dataScadenza;
        this.approvedBy = approvedBy;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }

    public int getTipoCertificatoId() { return tipoCertificatoId; }
    public void setTipoCertificatoId(int tipoCertificatoId) { this.tipoCertificatoId = tipoCertificatoId; }

    public String getNomeFile() { return nomeFile; }
    public void setNomeFile(String nomeFile) { this.nomeFile = nomeFile; }

    public byte[] getDatiDocumento() { return datiDocumento; }
    public void setDatiDocumento(byte[] datiDocumento) { this.datiDocumento = datiDocumento; }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }

    public String getMimeType() { return mimeType; }
    public void setMimeType(String mimeType) { this.mimeType = mimeType; }

    public LocalDateTime getDataCaricamento() { return dataCaricamento; }
    public void setDataCaricamento(LocalDateTime dataCaricamento) { this.dataCaricamento = dataCaricamento; }

    public LocalDateTime getDataScadenza() { return dataScadenza; }
    public void setDataScadenza(LocalDateTime dataScadenza) { this.dataScadenza = dataScadenza; }

    public Integer getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Integer approvedBy) { this.approvedBy = approvedBy; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public TipoCertificato getTipoCertificato() { return tipoCertificato; }
    public void setTipoCertificato(TipoCertificato tipoCertificato) { this.tipoCertificato = tipoCertificato; }

    public Amministratore getApprovatore() { return approvatore; }
    public void setApprovatore(Amministratore approvatore) { this.approvatore = approvatore; }

    @Override
    public String toString() {
        return "Certificato{id=" + id + ", medicoId=" + medicoId +
               ", nomeFile='" + nomeFile + "', stato=" + stato + "}";
    }
}
