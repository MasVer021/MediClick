package it.mediclick.service;

import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.bean.Categoria;
import it.mediclick.model.bean.Disponibilita;
import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.Recensione;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.DisponibilitaDAO;
import it.mediclick.model.dao.ErogazionePrestazioneDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.RecensioneDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.jasper.tagplugins.jstl.core.ForEach;

public class RicercaService {

    private MedicoDAO medicoDAO;
    private Contex _contex;

    public RicercaService(Contex contex) {
        this._contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
    }

    
    public List<MedicoCardDTO> cercaMedici(String query, Integer categoriaId, String citta) throws SQLException 
    {
        return medicoDAO.findCards(query, categoriaId, citta);
    }
    
    
   

    public Medico getDettaglioMedico(int medicoId) throws SQLException 
    {
        return medicoDAO.findById(medicoId);
    }

    public List<Medico> getMediciConsigliati() throws SQLException 
    {
        return medicoDAO.findByStato(Medico.StatoVerifica.APPROVATO).stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    public List<Categoria> getCategorie() throws SQLException 
    {
        CatalogoPrestazioniDAO catalogoDAO = new CatalogoPrestazioniDAO(_contex);
        return catalogoDAO.findAllCategorie();
    }
}
