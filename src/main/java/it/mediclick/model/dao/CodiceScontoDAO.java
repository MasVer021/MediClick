package it.mediclick.model.dao;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;

public class CodiceScontoDAO 
{
	private Contex _contex;

    public CodiceScontoDAO(Contex contex) 
    {
        _contex = contex;
    }
    
    public CodiceSconto findById(int id) throws SQLException 
    {
       String sql = """
       				SELECT * 
       				FROM CodiceSconto 
       				WHERE ID=?;
       				""";
       List<Map<String, Object>> result = _contex.eseguiSelect(sql, id);
       if (result == null || result.isEmpty()) return null;
       return mapping(result.getFirst());
    }
    
    public CodiceSconto findByCodice(String codice) throws SQLException 
    {
    	String sql = 	"""
		   				SELECT * 
		   				FROM CodiceSconto
		   				WHERE Codice=?;
		   				""";
    	
	   List<Map<String, Object>> result = _contex.eseguiSelect(sql, codice);
       if (result == null || result.isEmpty()) return null;
       return mapping(result.getFirst());
    }
    
    public List<CodiceSconto> findAll(boolean onlyValid) throws SQLException 
    {
    	List<CodiceSconto> sconti = new ArrayList<>();
    	List<Map<String, Object>> map;
    	
    	String sql = 	"""
		   				SELECT * 
		   				FROM CodiceSconto
		   				""";
    	if(onlyValid)
    	{
    		sql += """
    				WHERE Data_Scadenza >= ?
    				AND Attivo = ?			
    				""";
    		map = _contex.eseguiSelect(sql, java.sql.Date.valueOf(LocalDate.now()), 1);
    	}
    	else
    	{
    		map = _contex.eseguiSelect(sql);
    	}

        if (map != null) {
            for(Map<String, Object> m : map)
            {
                sconti.add(mapping(m));
            }
        }
		
		return sconti;
    }
    
    public boolean isValid(CodiceSconto sconto) throws SQLException
    {
    	return sconto != null && sconto.isAttivo() && !sconto.getDataScadenza().isBefore(LocalDate.now());
    }
    
    private ResultMapper<CodiceSconto> codiceScontoMapper = row->
    {
    	CodiceSconto cs = new CodiceSconto();
    	
    	cs.setId(MapRow.getInt(row, "ID"));
    	cs.setAttivo(MapRow.getBoolean(row, "Attivo"));
    	cs.setCodice(MapRow.getString(row, "Codice"));
    	cs.setDataScadenza(MapRow.getLocalDate(row, "Data_Scadenza"));
    	cs.setValorePercentuale(MapRow.getDouble(row, "Valore_Percentuale"));
    	
    	
    	
    	
    	    	
    	return cs;
    };
   
}
