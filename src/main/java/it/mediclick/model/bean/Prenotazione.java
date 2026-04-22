package it.mediclick.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Prenotazione implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Stato {
        CONFERMATA("Confermata"),
        COMPLETATA("Completata"),
        CANCELLATA("Cancellata"),
        RIMBORSATA("Rimborsata");

        private final String label;
        Stato(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private int pazienteId;
    private int disponibilitaId;
    private Integer erogazionePrestazioneId;
    private Integer codiceScontoId;
    private Stato stato;
    private String metodoPagamento;
    private String idTransazioneEsterno;
    private BigDecimal importoPagato;
    private BigDecimal ricavoNettoMedicoEuro;
    private BigDecimal trattenutaPiattaformaEuro;
    private BigDecimal tasseStimateEuro;
    private LocalDateTime dataPagamento;

    // Relazioni opzionali
    private Paziente paziente;
    private Disponibilita disponibilita;
    private ErogazionePrestazione erogazionePrestazione;
    private CodiceSconto codiceSconto;

    public Prenotazione() {
        this.stato = Stato.CONFERMATA;
    }

    public Prenotazione(int id, int pazienteId, int disponibilitaId,
                        Integer erogazionePrestazioneId, Integer codiceScontoId,
                        Stato stato, String metodoPagamento, String idTransazioneEsterno,
                        BigDecimal importoPagato, BigDecimal ricavoNettoMedicoEuro,
                        BigDecimal trattenutaPiattaformaEuro, BigDecimal tasseStimateEuro,
                        LocalDateTime dataPagamento) {
        this.id = id;
        this.pazienteId = pazienteId;
        this.disponibilitaId = disponibilitaId;
        this.erogazionePrestazioneId = erogazionePrestazioneId;
        this.codiceScontoId = codiceScontoId;
        this.stato = stato;
        this.metodoPagamento = metodoPagamento;
        this.idTransazioneEsterno = idTransazioneEsterno;
        this.importoPagato = importoPagato;
        this.ricavoNettoMedicoEuro = ricavoNettoMedicoEuro;
        this.trattenutaPiattaformaEuro = trattenutaPiattaformaEuro;
        this.tasseStimateEuro = tasseStimateEuro;
        this.dataPagamento = dataPagamento;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPazienteId() { return pazienteId; }
    public void setPazienteId(int pazienteId) { this.pazienteId = pazienteId; }

    public int getDisponibilitaId() { return disponibilitaId; }
    public void setDisponibilitaId(int disponibilitaId) { this.disponibilitaId = disponibilitaId; }

    public Integer getErogazionePrestazioneId() { return erogazionePrestazioneId; }
    public void setErogazionePrestazioneId(Integer erogazionePrestazioneId) { this.erogazionePrestazioneId = erogazionePrestazioneId; }

    public Integer getCodiceScontoId() { return codiceScontoId; }
    public void setCodiceScontoId(Integer codiceScontoId) { this.codiceScontoId = codiceScontoId; }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }

    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }

    public String getIdTransazioneEsterno() { return idTransazioneEsterno; }
    public void setIdTransazioneEsterno(String idTransazioneEsterno) { this.idTransazioneEsterno = idTransazioneEsterno; }

    public BigDecimal getImportoPagato() { return importoPagato; }
    public void setImportoPagato(BigDecimal importoPagato) { this.importoPagato = importoPagato; }

    public BigDecimal getRicavoNettoMedicoEuro() { return ricavoNettoMedicoEuro; }
    public void setRicavoNettoMedicoEuro(BigDecimal ricavoNettoMedicoEuro) { this.ricavoNettoMedicoEuro = ricavoNettoMedicoEuro; }

    public BigDecimal getTrattenutaPiattaformaEuro() { return trattenutaPiattaformaEuro; }
    public void setTrattenutaPiattaformaEuro(BigDecimal trattenutaPiattaformaEuro) { this.trattenutaPiattaformaEuro = trattenutaPiattaformaEuro; }

    public BigDecimal getTasseStimateEuro() { return tasseStimateEuro; }
    public void setTasseStimateEuro(BigDecimal tasseStimateEuro) { this.tasseStimateEuro = tasseStimateEuro; }

    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }

    public Paziente getPaziente() { return paziente; }
    public void setPaziente(Paziente paziente) { this.paziente = paziente; }

    public Disponibilita getDisponibilita() { return disponibilita; }
    public void setDisponibilita(Disponibilita disponibilita) { this.disponibilita = disponibilita; }

    public ErogazionePrestazione getErogazionePrestazione() { return erogazionePrestazione; }
    public void setErogazionePrestazione(ErogazionePrestazione erogazionePrestazione) { this.erogazionePrestazione = erogazionePrestazione; }

    public CodiceSconto getCodiceSconto() { return codiceSconto; }
    public void setCodiceSconto(CodiceSconto codiceSconto) { this.codiceSconto = codiceSconto; }

    @Override
    public String toString() {
        return "Prenotazione{id=" + id + ", pazienteId=" + pazienteId +
               ", importo=" + importoPagato + ", stato=" + stato + "}";
    }
}
