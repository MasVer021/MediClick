package it.mediclick.model.dao;

import it.mediclick.model.bean.Amministratore;
import it.mediclick.model.bean.Certificato;
import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.TipoCertificato;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CertificatoDAO 
{
    private final Contex _contex;
    private final MedicoDAO medicoDAO;
    private final AmministratoreDAO amministratoreDAO;

    public CertificatoDAO(Contex contex) 
    {
        _contex = contex;
        medicoDAO = new MedicoDAO(contex);
        amministratoreDAO = new AmministratoreDAO(contex);
    }

    public Optional<Certificato> findById(int id) throws SQLException 
    {
                     
        try
   	 	{
        	 String sql = 	"""
		                     SELECT * 
		                     FROM Certificato 
		                     WHERE ID = ?
		        	 		""";
        	 
	        return  _contex.eseguiSelectSingolo(sql,certificatoMapper, id);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero del Certificato con ID " + id, e); 
       }
    }
    
    public Optional<TipoCertificato> tipoCertificatofindById(int id) throws SQLException 
    {
                     
        try
   	 	{
        	 String sql = 	"""
		                    SELECT * 
						    FROM tipocertificato
						   	WHERE ID = ?
		        	 		""";
        	 
        	 return  _contex.eseguiSelectSingolo(sql,tipoCertificatoMapper, id);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero del tipo di certificato con ID " + id, e); 
       }
    }
    
    public List<TipoCertificato> tipoCertificatofindAll() throws SQLException 
    {         
        try
   	 	{
        	 String sql = 	"""
		                    SELECT * 
						    FROM tipocertificato
		        	 		""";
        	 
        	 return  _contex.eseguiSelect(sql,tipoCertificatoMapper);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero dei tipi di certificati ", e); 
       }
    }

    public List<Certificato> findByMedico(int medicoId) throws SQLException 
    {
                     
        try
   	 	{
        	 String sql = 	"""
		                     SELECT * 
		                     FROM Certificato 
		                     WHERE Medico_ID = ?
	                  		""";
        	 
	        return  _contex.eseguiSelect(sql,certificatoMapper, medicoId);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero dell Certificato con medico id " + medicoId, e); 
       }
    }

    public Optional <Certificato> findByMedicoETipo(int medicoId, int tipoCertificatoId) throws SQLException 
    {
                  
        try
   	 	{
        	String sql = """
                    SELECT * 
                    FROM Certificato 
                    WHERE Medico_ID = ? AND TipoCertificato_ID = ?
                 """;
        	 
	        return  _contex.eseguiSelectSingolo(sql,certificatoMapper, medicoId,tipoCertificatoId);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero dell Certificato con medico id " + medicoId + " e ID tipo " + tipoCertificatoId, e); 
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
        	Integer medicoId = c.getMedicoId();
        	Integer tipoCertificatoId = c.getTipoCertificatoId();
        	Integer approvatoreId = c.getApprovatoreId() > 0 ? c.getApprovatoreId() : null;
        	
        	String stato =  c.getStato() != null ? c.getStato().getLabel() : "In revisione";
        	
            _contex.eseguiUpdate(sql, 
            	medicoId,
               	tipoCertificatoId,
                c.getNomeFile(),
                c.getDatiDocumento(),
                stato,
                c.getMimeType(),
                c.getDataCaricamento(),
                c.getDataScadenza(),
                approvatoreId
            );
        }
        catch(SQLException e)
        {
        	throw new SQLException("Errore nell'inserimento del certificato: " , e); 
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
        	throw new SQLException("Errore nell' aggiornamento dello stato del certificato: " , e); 
        }
    }


    public void getCompleto(Certificato c) throws SQLException
	{

        int medicoId = c.getMedicoId();
    	Integer amministratoreId = c.getApprovatoreId();
    	Integer tipoCertificatoId = c.getTipoCertificatoId();
        
        TipoCertificato tc = tipoCertificatofindById(tipoCertificatoId).orElseThrow(() -> new SQLException("TipoCertificato " + tipoCertificatoId + " non trovato"));
        Medico m = medicoDAO.findById(medicoId).orElseThrow(() -> new SQLException("Medico " + medicoId + " non trovato"));
        if(amministratoreId >0)
        {
            Amministratore a = amministratoreDAO.findById(amministratoreId).orElse(null);
            c.setApprovatore(a);
        }
        

		c.setMedico(m);
    	c.setTipoCertificato(tc);
    	
    }
       
    private final ResultMapper<TipoCertificato> tipoCertificatoMapper = row ->
    {
    	TipoCertificato tc = new TipoCertificato();
    	
    	tc.setId(MapRow.getInt(row, "ID"));
    	tc.setNome(MapRow.getString(row, "Nome"));
    	tc.setObbligatorio(MapRow.getBoolean(row, "Obbligatorio"));
    	
    	return tc;
    };
    
    private final ResultMapper<Certificato> certificatoMapper = row ->
    {
    	Certificato c = new Certificato();
    	
    	Integer medicoId = MapRow.getIntOrNull(row,"Medico_ID");
        medicoId = medicoId != null ? medicoId : -1; 

    	Integer amministratoreId = MapRow.getIntOrNull(row,"Approved_by");
        amministratoreId = amministratoreId != null ? amministratoreId : -1;

        Integer tipoCertificatoId = MapRow.getIntOrNull(row, "TipoCertificato_ID");
        tipoCertificatoId = tipoCertificatoId != null ? tipoCertificatoId : -1;

        String dati = MapRow.getString(row, "Dati_Documento");
    		
    	c.setId(MapRow.getInt(row, "ID"));
    	c.setNomeFile(MapRow.getString(row,"Nome_File"));
    	c.setDatiDocumento(dati != null ? dati.getBytes(StandardCharsets.UTF_8) : null);
    	c.setStato(Certificato.Stato.fromString(MapRow.getString(row,"Stato")));
    	c.setMimeType(MapRow.getString(row,"Mime_Type"));
    	c.setDataCaricamento(MapRow.getLocalDateTime(row,"Data_Caricamento"));
    	c.setDataScadenza(MapRow.getLocalDateTime(row,"Data_Scadenza"));

        c.setApprovatoreId(amministratoreId);
        c.setMedicoId(medicoId);
        c.setTipoCertificatoId(tipoCertificatoId);
    	
    	return c;
    };
}