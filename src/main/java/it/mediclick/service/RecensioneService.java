package it.mediclick.service;

import it.mediclick.model.bean.Prenotazione;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.dao.PrenotazioneDAO;
import it.mediclick.model.dao.RecensioneDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.List;

public class RecensioneService 
{

    private RecensioneDAO recensioneDAO;
    private PrenotazioneDAO prenotazioneDAO;
    private Contex _contex;

    public RecensioneService(Contex contex) 
    {
        this._contex = contex;
        this.recensioneDAO = new RecensioneDAO(_contex);
        this.prenotazioneDAO = new PrenotazioneDAO(_contex);
    }

    public boolean lasciaRecensione(int prenotazioneId, int voto, String commento) throws SQLException 
    {
        Prenotazione p = prenotazioneDAO.findById(prenotazioneId);
        
        if (p == null || p.getStato() != Prenotazione.Stato.COMPLETATA) 
        {
            return false;
        }

        Recensione r = new Recensione();
        r.setPrenotazioneId(prenotazioneId);
        r.setVoto(voto);
        r.setCommento(commento);
        r.setVisible(true);
        
        recensioneDAO.insert(r);
        return true;
    }

    public List<Recensione> getRecensioniMedico(int medicoId) throws SQLException 
    {
        return recensioneDAO.findByMedico(medicoId);
    }

    public void moderaRecensione(int recensioneId, boolean visibile) throws SQLException 
    {
        recensioneDAO.setVisibile(recensioneId, visibile);
    }
}
