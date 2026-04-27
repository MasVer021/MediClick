package it.mediclick.service;

import it.mediclick.model.bean.Medico;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class RicercaService {

    private MedicoDAO medicoDAO;
    private Contex _contex;

    public RicercaService(Contex contex) {
        this._contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
    }

    
    public List<Medico> cercaMedici(String query, Integer categoriaId, String citta) throws SQLException 
    {
        // La ricerca ora avviene direttamente sul database per performance ottimali
        return medicoDAO.findAll(query, categoriaId, citta);
    }

    /**
     * Recupera il dettaglio completo di un medico.
     */
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
}
