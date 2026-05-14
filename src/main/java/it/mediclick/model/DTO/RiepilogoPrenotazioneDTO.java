package it.mediclick.model.DTO;

import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Studio;

public class RiepilogoPrenotazioneDTO {
    private Medico medico;
    private ErogazionePrestazione prestazione;
    private Disponibilita disponibilita;
    private Studio studio;
    private CatalogoPrestazioni catalogoPrestazioni;

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public ErogazionePrestazione getPrestazione() {
        return prestazione;
    }

    public void setPrestazione(ErogazionePrestazione prestazione) {
        this.prestazione = prestazione;
    }

    public Disponibilita getDisponibilita() {
        return disponibilita;
    }

    public void setDisponibilita(Disponibilita disponibilita) {
        this.disponibilita = disponibilita;
    }

    public Studio getStudio() {
        return studio;
    }

    public void setStudio(Studio studio) {
        this.studio = studio;
    }

    public CatalogoPrestazioni getCatalogoPrestazioni() {
        return catalogoPrestazioni;
    }

    public void setCatalogoPrestazioni(CatalogoPrestazioni catalogoPrestazioni) {
        this.catalogoPrestazioni = catalogoPrestazioni;
    }
}
