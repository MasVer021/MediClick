package it.mediclick.service;

import it.mediclick.exception.*;
import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.dao.CatalogoPrestazioniDAO;
import it.mediclick.model.dao.MedicoDAO;
import it.mediclick.model.dao.UtenteDAO;
import it.mediclick.util.Contex;
import java.sql.SQLException;
import java.util.List;


public class AmministrazioneService 
{

    private final MedicoDAO medicoDAO;
    private final CatalogoPrestazioniDAO catalogoDAO;
    private final UtenteDAO utenteDAO;
    private final Contex _contex;

    public AmministrazioneService(Contex contex) 
    {
        _contex = contex;
        this.medicoDAO = new MedicoDAO(_contex);
        this.catalogoDAO = new CatalogoPrestazioniDAO(_contex);
        this.utenteDAO = new UtenteDAO(_contex);
    }

    
    public void approvaMedico(int medicoId) throws AmministratoreException 
    {
        try 
        {
            Medico medico = medicoDAO.findById(medicoId).orElseThrow(() -> new AmministratoreException("Medico non trovato con ID: " + medicoId,"AMMINISTRATORE_APPROVA_MEDICO_NOT_FOUND"));

            if (medico.getStatoVerifica() != Medico.StatoVerifica.IN_ATTESA) 
            {
                throw new AmministratoreException("Il medico con ID " + medicoId + " non è in attesa di approvazione.","AMMINISTRATORE_APPROVA_MEDICO_INVALID_STATE");
            }
            medicoDAO.updateStatoVerifica(medicoId, Medico.StatoVerifica.APPROVATO);
        } 
        catch (SQLException e) 
        {
            throw new AmministratoreException("Errore durante l'approvazione del medico con ID " + medicoId + ": " + e.getMessage(), "AMMINISTRATORE_APPROVA_MEDICO_ERROR");
        }
       
    }

   
    public void aggiungiAlCatalogo(CatalogoPrestazioni cp) throws AmministratoreException 
    {
        try 
        {
            catalogoDAO.insert(cp);
        } 
        catch (SQLException e) 
        {
            throw new AmministratoreException("Errore durante l'aggiunta della prestazione al catalogo: " + e.getMessage(), "AMMINISTRATORE_AGGIUNGI_CATALOGO_ERROR");
        }
    }

    public void aggiornaStatoPrestazione(int catalogoId, CatalogoPrestazioni.Stato stato) throws AmministratoreException 
    {
        try 
        {
            catalogoDAO.updateStato(catalogoId, stato);
        } 
        catch (SQLException e) 
        {
            throw new AmministratoreException("Errore durante l'aggiornamento dello stato della prestazione con ID " + catalogoId + ": " + e.getMessage(), "AMMINISTRATORE_AGGIORNA_STATO_CATALOGO_ERROR");
        }
    }

    
    public void bloccaUtente(int utenteId, boolean bloccato) throws AmministratoreException 
    {
        try 
        {
            utenteDAO.setAccountAttivo(utenteId, !bloccato);
        } 
        catch (SQLException e) 
        {
            throw new AmministratoreException("Errore durante il blocco dell'utente con ID " + utenteId + ": " + e.getMessage(), "AMMINISTRATORE_BLOCCA_UTENTE_ERROR");
        }
    }
    
    public List<Medico> getMediciInAttesa() throws AmministratoreException
    {
        try 
        {
    	    return  medicoDAO.findByStato(Medico.StatoVerifica.IN_ATTESA);
        } 
        catch (SQLException e) 
        {
            throw new AmministratoreException("Errore durante il recupero dei medici in attesa: " + e.getMessage(), "AMMINISTRATORE_GET_MEDICI_IN_ATTESA_ERROR");
        }
    }
}
