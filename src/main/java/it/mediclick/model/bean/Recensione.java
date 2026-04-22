package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Recensione implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int prenotazioneId;
    private byte voto;             // 1-5
    private String commento;
    private boolean isVisible;
    private LocalDateTime dataPubblicazione;

    // Relazione opzionale
    private Prenotazione prenotazione;

    public Recensione() {
        this.isVisible = true;
    }

    public Recensione(int id, int prenotazioneId, byte voto, String commento,
                      boolean isVisible, LocalDateTime dataPubblicazione) {
        if (voto < 1 || voto > 5) throw new IllegalArgumentException("Voto deve essere tra 1 e 5");
        this.id = id;
        this.prenotazioneId = prenotazioneId;
        this.voto = voto;
        this.commento = commento;
        this.isVisible = isVisible;
        this.dataPubblicazione = dataPubblicazione;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPrenotazioneId() { return prenotazioneId; }
    public void setPrenotazioneId(int prenotazioneId) { this.prenotazioneId = prenotazioneId; }

    public byte getVoto() { return voto; }
    public void setVoto(byte voto) {
        if (voto < 1 || voto > 5) throw new IllegalArgumentException("Voto deve essere tra 1 e 5");
        this.voto = voto;
    }

    public String getCommento() { return commento; }
    public void setCommento(String commento) { this.commento = commento; }

    public boolean isVisible() { return isVisible; }
    public void setVisible(boolean visible) { isVisible = visible; }

    public LocalDateTime getDataPubblicazione() { return dataPubblicazione; }
    public void setDataPubblicazione(LocalDateTime dataPubblicazione) { this.dataPubblicazione = dataPubblicazione; }

    public Prenotazione getPrenotazione() { return prenotazione; }
    public void setPrenotazione(Prenotazione prenotazione) { this.prenotazione = prenotazione; }

    @Override
    public String toString() {
        return "Recensione{id=" + id + ", prenotazioneId=" + prenotazioneId +
               ", voto=" + voto + ", visible=" + isVisible + "}";
    }
}
