package it.mediclick.model.bean;

import java.io.Serializable;
import java.time.LocalDate;


public class Paziente implements Serializable
{

    private static final long serialVersionUID = 1L;

    private int id = -1;         
    private String cognome;
    private String nome;
    private String codiceFiscale;
    private String telefono;
    private LocalDate dataNascita;

    private Utente utente;

    public Paziente()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCognome()
    {
        return cognome;
    }

    public void setCognome(String cognome)
    {
        this.cognome = cognome;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public String getCodiceFiscale()
    {
        return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale)
    {
        this.codiceFiscale = codiceFiscale;
    }

    public String getTelefono()
    {
        return telefono;
    }

    public void setTelefono(String telefono)
    {
        this.telefono = telefono;
    }

    public LocalDate getDataNascita()
    {
        return dataNascita;
    }

    public void setDataNascita(LocalDate dataNascita)
    {
        this.dataNascita = dataNascita;
    }

    public Utente getUtente()
    {
        return utente;
    }

    public void setUtente(Utente utente)
    {
        this.utente = utente;
    }

    
    public String getNomeCompleto()
    {
        return cognome + " " + nome;
    }

    @Override
    public String toString()
    {
        return "Paziente{id=" + id + ", nome='" + getNomeCompleto() + "', cf='" + codiceFiscale + "'}";
    }
}
