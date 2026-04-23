package it.mediclick.model.dao;

import it.mediclick.model.bean.ErogazionePrestazione;
import it.mediclick.util.Contex;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ErogazionePrestazioneDAO 
{
    private Contex _contex;

    public ErogazionePrestazioneDAO(Contex contex) 
    {
        _contex = contex;
    }

    public ErogazionePrestazione findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM ErogazionePrestazione 
                        WHERE ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, id);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca dell'erogazione prestazione by id: " + e.getMessage());
             throw e;
        }
    }

    public List<ErogazionePrestazione> findByMedico(int medicoId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM ErogazionePrestazione 
                        WHERE Medico_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<ErogazionePrestazione> list = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return list;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                list.add(mapping(map));
            }
            return list;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca erogazioni prestazioni per medico: " + e.getMessage());
             throw e;
        }
    }

    public List<ErogazionePrestazione> findByMedicoEStudio(int medicoId, int studioId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM ErogazionePrestazione 
                        WHERE Medico_ID = ? AND Studio_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId, studioId);
        List<ErogazionePrestazione> list = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return list;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                list.add(mapping(map));
            }
            return list;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca erogazioni per medico e studio: " + e.getMessage());
             throw e;
        }
    }

    public void insert(ErogazionePrestazione ep) throws SQLException 
    {
        String sql = """
                        INSERT INTO ErogazionePrestazione(Medico_ID, CatalogoPrestazioni_ID, Studio_ID, Prezzo_Lordo_Listino, Durata, Stato) 
                        VALUES (?,?,?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                ep.getMedicoId(),
                ep.getCatalogoPrestazioniId(),
                ep.getStudioId(),
                ep.getPrezzoLordoListino(),
                ep.getDurata(),
                ep.getStato().getLabel()
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento erogazione prestazione: " + e.getMessage());
             throw e;
        }
    }

    public void updateStato(int id, ErogazionePrestazione.Stato stato) throws SQLException 
    {
        String sql = """
                        UPDATE ErogazionePrestazione 
                        SET Stato = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento stato erogazione prestazione: " + e.getMessage());
             throw e;
        }
    }

    public void updatePrezzo(int id, Double prezzo) throws SQLException 
    {
        String sql = """
                        UPDATE ErogazionePrestazione 
                        SET Prezzo_Lordo_Listino = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, prezzo, id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento prezzo erogazione prestazione: " + e.getMessage());
             throw e;
        }
    }

    private ErogazionePrestazione mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            ErogazionePrestazione ep = new ErogazionePrestazione();
            ep.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            ep.setMedicoId(Integer.parseInt(String.valueOf(map.get("Medico_ID"))));
            ep.setCatalogoPrestazioniId(Integer.parseInt(String.valueOf(map.get("CatalogoPrestazioni_ID"))));
            
            if (map.get("Studio_ID") != null) 
            {
                ep.setStudioId(Integer.parseInt(String.valueOf(map.get("Studio_ID"))));
            }

            if (map.get("Prezzo_Lordo_Listino") != null) 
            {
                ep.setPrezzoLordoListino(Double.parseDouble(String.valueOf(map.get("Prezzo_Lordo_Listino"))));
            }

            if (map.get("Durata") != null) 
            {
                ep.setDurata(Integer.parseInt(String.valueOf(map.get("Durata"))));
            }
           
            ep.setStato(ErogazionePrestazione.Stato.fromString(String.valueOf(map.get("Stato"))));
           
            return ep;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di ErogazionePrestazione: " + e.getMessage(), e);
        }
    }
}
