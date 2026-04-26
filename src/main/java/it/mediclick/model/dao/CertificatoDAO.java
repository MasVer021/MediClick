package it.mediclick.model.dao;

import it.mediclick.model.bean.Certificato;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CertificatoDAO 
{
    private Contex _contex;

    public CertificatoDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Certificato findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Certificato 
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
             System.err.println("Errore nella ricerca del certificato by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Certificato> findByMedico(int medicoId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Certificato 
                        WHERE Medico_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<Certificato> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca certificati per medico: " + e.getMessage());
             throw e;
        }
    }

    public Certificato findByMedicoETipo(int medicoId, int tipoCertificatoId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Certificato 
                        WHERE Medico_ID = ? AND TipoCertificato_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId, tipoCertificatoId);
        
        if (result == null || result.isEmpty()) 
            return null;
            
        try
        {
            return mapping(result.get(0));
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca certificato per medico e tipo: " + e.getMessage());
             throw e;
        }
    }

    public void insert(Certificato c) throws SQLException 
    {
        String sql = """
                        INSERT INTO Certificato(Medico_ID, TipoCertificato_ID, Nome_File, Dati_Documento, Stato, Mime_Type, Data_Caricamento, Data_Scadenza, Approved_by) 
                        VALUES (?,?,?,?,?,?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                c.getMedicoId(),
                c.getTipoCertificatoId(),
                c.getNomeFile(),
                c.getDatiDocumento(),
                c.getStato() != null ? c.getStato().getLabel() : "In revisione",
                c.getMimeType(),
                c.getDataCaricamento() != null ? Timestamp.valueOf(c.getDataCaricamento()) : null,
                c.getDataScadenza() != null ? Timestamp.valueOf(c.getDataScadenza()) : null,
                c.getApprovedBy()
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento del certificato: " + e.getMessage());
             throw e;
        }
    }

    public void updateStato(int id, Certificato.Stato stato) throws SQLException 
    {
        String sql = """
                        UPDATE Certificato 
                        SET Stato = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento stato certificato: " + e.getMessage());
             throw e;
        }
    }

    private Certificato mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Certificato c = new Certificato();
            c.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            c.setMedicoId(Integer.parseInt(String.valueOf(map.get("Medico_ID"))));
            c.setTipoCertificatoId(Integer.parseInt(String.valueOf(map.get("TipoCertificato_ID"))));
            c.setNomeFile((String) map.get("Nome_File"));
            
            if (map.get("Dati_Documento") != null) 
            {
                c.setDatiDocumento((byte[]) map.get("Dati_Documento"));
            }

            c.setStato(Certificato.Stato.fromString(String.valueOf(map.get("Stato"))));
            
            c.setMimeType((String) map.get("Mime_Type"));

            if (map.get("Data_Caricamento") != null) 
            {
                c.setDataCaricamento(LocalDateTime.parse(String.valueOf(map.get("Data_Caricamento"))));
            }
            
            if (map.get("Data_Scadenza") != null) 
            {
                c.setDataScadenza(LocalDateTime.parse(String.valueOf(map.get("Data_Scadenza"))));
            }

            if (map.get("Approved_by") != null) 
            {
                c.setApprovedBy(Integer.parseInt(String.valueOf(map.get("Approved_by"))));
            }

            return c;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di Certificato: " + e.getMessage(), e);
        }
    }
}
