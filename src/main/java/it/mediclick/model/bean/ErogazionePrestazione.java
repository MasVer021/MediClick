package it.mediclick.model.bean;

import java.io.Serializable;
import java.math.BigDecimal;

public class ErogazionePrestazione implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Stato {
        ATTIVA("Attiva"),
        SOSPESA("Sospesa");

        private final String label;
        Stato(String label) { this.label = label; }
        public String getLabel() { return label; }
    }

    private int id;
    private int medicoId;
    private int catalogoPrestazioniId;
    private Integer studioId;
    private BigDecimal prezzoLordoListino;
    /** Durata in minuti, approssimata alla mezz'ora successiva (es. 31 min → 60 min) */
    private Integer durata;
    private Stato stato;

    // Relazioni opzionali
    private Medico medico;
    private CatalogoPrestazioni catalogoPrestazioni;
    private Studio studio;

    public ErogazionePrestazione() {
        this.stato = Stato.ATTIVA;
    }

    public ErogazionePrestazione(int id, int medicoId, int catalogoPrestazioniId,
                                  Integer studioId, BigDecimal prezzoLordoListino,
                                  Integer durata, Stato stato) {
        this.id = id;
        this.medicoId = medicoId;
        this.catalogoPrestazioniId = catalogoPrestazioniId;
        this.studioId = studioId;
        this.prezzoLordoListino = prezzoLordoListino;
        this.durata = durata;
        this.stato = stato;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }

    public int getCatalogoPrestazioniId() { return catalogoPrestazioniId; }
    public void setCatalogoPrestazioniId(int catalogoPrestazioniId) { this.catalogoPrestazioniId = catalogoPrestazioniId; }

    public Integer getStudioId() { return studioId; }
    public void setStudioId(Integer studioId) { this.studioId = studioId; }

    public BigDecimal getPrezzoLordoListino() { return prezzoLordoListino; }
    public void setPrezzoLordoListino(BigDecimal prezzoLordoListino) { this.prezzoLordoListino = prezzoLordoListino; }

    public Integer getDurata() { return durata; }
    public void setDurata(Integer durata) { this.durata = durata; }

    public Stato getStato() { return stato; }
    public void setStato(Stato stato) { this.stato = stato; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public CatalogoPrestazioni getCatalogoPrestazioni() { return catalogoPrestazioni; }
    public void setCatalogoPrestazioni(CatalogoPrestazioni catalogoPrestazioni) { this.catalogoPrestazioni = catalogoPrestazioni; }

    public Studio getStudio() { return studio; }
    public void setStudio(Studio studio) { this.studio = studio; }

    @Override
    public String toString() {
        return "ErogazionePrestazione{id=" + id + ", medicoId=" + medicoId +
               ", prezzoLordo=" + prezzoLordoListino + ", durata=" + durata + "min, stato=" + stato + "}";
    }
}
