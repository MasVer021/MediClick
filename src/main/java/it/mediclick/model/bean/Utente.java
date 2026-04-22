package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDate;

public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String email;
    private String password;
    private LocalDate dataIscrizione;
    private boolean accountAttivo;
    private Integer ruoloId;

    
    private Ruolo ruolo;

    public Utente() {
        this.accountAttivo = true;
    }

    public Utente(int id, String email, String password, LocalDate dataIscrizione, boolean accountAttivo, Integer ruoloId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.dataIscrizione = dataIscrizione;
        this.accountAttivo = accountAttivo;
        this.ruoloId = ruoloId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDate getDataIscrizione() { return dataIscrizione; }
    public void setDataIscrizione(LocalDate dataIscrizione) { this.dataIscrizione = dataIscrizione; }

    public boolean isAccountAttivo() { return accountAttivo; }
    public void setAccountAttivo(boolean accountAttivo) { this.accountAttivo = accountAttivo; }

    public Integer getRuoloId() { return ruoloId; }
    public void setRuoloId(Integer ruoloId) { this.ruoloId = ruoloId; }

    public Ruolo getRuolo() { return ruolo; }
    public void setRuolo(Ruolo ruolo) { this.ruolo = ruolo; }

    @Override
    public String toString() {
        return "Utente{id=" + id + ", email='" + email + "', accountAttivo=" + accountAttivo + "}";
    }
}
