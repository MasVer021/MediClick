package it.mediclick.service;

import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;

import java.sql.SQLException;

public class AmministrazioneService 
{

    private MedicoDAO medicoDAO;
    private CatalogoPrestazioniDAO catalogoDAO;
    private UtenteDAO utenteDAO;
    private Contex _contex;

    public AmministrazioneService(Contex contex) 
    {
        _contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
        this.catalogoDAO = new CatalogoPrestazioniDAO(_contex);
        this.utenteDAO = new UtenteDAO(_contex);
    }

    
    public void approvaMedico(int medicoId) throws SQLException 
    {
        medicoDAO.updateStatoVerifica(medicoId, Medico.StatoVerifica.APPROVATO);
    }

   
    public void aggiungiAlCatalogo(CatalogoPrestazioni cp) throws SQLException 
    {
        catalogoDAO.insert(cp);
    }

    public void aggiornaStatoPrestazione(int catalogoId, CatalogoPrestazioni.Stato stato) throws SQLException 
    {
        catalogoDAO.updateStato(catalogoId, stato);
    }

    
    public void bloccaUtente(int utenteId, boolean bloccato) throws SQLException 
    {
        utenteDAO.setAccountAttivo(utenteId, !bloccato);
    }
}
