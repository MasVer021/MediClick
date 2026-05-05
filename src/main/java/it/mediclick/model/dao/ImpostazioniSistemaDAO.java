package it.mediclick.model.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.mediclick.model.bean.ImpostazioniSistema;
import it.mediclick.util.Contex;

public class ImpostazioniSistemaDAO 
{
    private Contex _contex;
    
    public ImpostazioniSistemaDAO(Contex contex) 
    {
        _contex = contex;
    }
    
    public ImpostazioniSistema getImpByID(int id) throws SQLException
    {
    	   String sql = """
		                   SELECT *
		                   FROM ImpostazioniSistema
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
                System.err.println("Errore nella ricerca delle impostazioni per id " + e.getMessage());
                throw e;
           }
    }
    
    public List<ImpostazioniSistema> getAllImp() throws SQLException
    {
    	   String sql = """
		                   SELECT *
		                   FROM ImpostazioniSistema
		                   WHERE Data_Fine is NULL
    	   				""";
    	   
    	   List<Map<String, Object>> result = _contex.eseguiSelect(sql);
           
           if (result == null || result.isEmpty()) 
               return null;
           
           List<ImpostazioniSistema> impostazioni = new ArrayList<ImpostazioniSistema>();
               
           try
           {
        	   for (Map<String, Object> map : result) 
               {
        		   impostazioni.add(mapping(map));
               }
               return impostazioni;
           }
           catch(SQLException e)
           {
                System.err.println("Errore nella ricerca delle impostazioni: " + e.getMessage());
                throw e;
           }
    }
    
    public int getIDImpByKey(String key) throws SQLException
    {
    	   String sql = """
		                   SELECT *
		                   FROM ImpostazioniSistema
		                   WHERE Chiave = ?
		                   AND Data_Fine is NULL
    	   				""";
    	   
    	   List<Map<String, Object>> result = _contex.eseguiSelect(sql, key);
           
           if (result == null || result.isEmpty()) 
               return -1;
               
           try
           {
               return mapping(result.get(0)).getId();
           }
           catch(SQLException e)
           {
                System.err.println("Errore nella ricerca delle impostazioni per id " + e.getMessage());
                throw e;
           }
    }
    
    public int insertImp(String chiave,String valore,int idAmministatore) throws SQLException
    {
    	   String sql = """
		                  INSERT 
		                  INTO ImpostazioniSistema (Chiave, Valore, Data_Inizio, Data_Fine, Updated_by) 
		                  VALUES('?', '?','?', NULL,?),
    	   				""";
    	   Connection conn = _contex.getConnection();
    	   conn.setAutoCommit(false);
    	   try
           {
	    	   int idOld = getIDImpByKey(chiave);
	    	   if(idOld != -1)
	    	   {
	    		   String sqlUpdateOld = 	"""
		    		   							UPDATE ImpoImpostazioniSistema
		    		   							set Data_Fine = ?
		    		   							where id = ?
	    		   							""";
	    		   _contex.eseguiUpdate(sqlUpdateOld, conn,LocalDateTime.now(),idOld);
	    	   }
	    	   
	    	   int idnew = _contex.eseguiUpdate(sql, conn,chiave,valore,LocalDateTime.now(),idAmministatore);
    	  
        	   conn.commit();
               return idnew;
           }
           catch(SQLException e)
           {
        	   conn.rollback();
                System.err.println("Errore nella modifica delle impostazioni: " + e.getMessage());
                throw e;
           }
    	   finally 
    	   {
			conn.close();
		}
    }
    
    private ImpostazioniSistema mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            ImpostazioniSistema i = new ImpostazioniSistema();
            
            int id = Integer.parseInt(String.valueOf(map.get("ID")));
           
            i.setId(id);
            i.setChiave(String.valueOf(map.get("Chiave")));
            i.setValore(String.valueOf(map.get("Valore")));
            i.setDataInizio(LocalDateTime.parse(String.valueOf(map.get("Data_Inizio"))));
            i.setDataFine(LocalDateTime.parse(String.valueOf(map.get("Data_Fine"))));
            i.setUpdatedBy(Integer.parseInt(String.valueOf(map.get("Updated_by"))));
            
            
            return i;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping delle impostazioni di sistema: " + e.getMessage(), e);
        }
    }

   
}

