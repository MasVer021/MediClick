package it.mediclick.model.dao;

import it.mediclick.model.bean.CodiceSconto;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class CodiceScontoDAO 
{
	private final Contex _contex;

    public CodiceScontoDAO(Contex contex) 
    {
        _contex = contex;
    }
    
    public Optional<CodiceSconto> findById(int id) throws SQLException 
    {

		try
		{
			String sql = 	"""
							SELECT * 
							FROM CodiceSconto 
							WHERE ID=?;
							""";
			return _contex.eseguiSelectSingolo(sql, codiceScontoMapper, id);
		}
		catch(SQLException e)
		{
			throw new SQLException("Errore durante la ricerca del codice sconto con ID: " + id, e);
		}
      
    }
    
    public Optional<CodiceSconto> findByCodice(String codice) throws SQLException 
    {
       	
		try
		{
			String sql = 	"""
							SELECT * 
		   					FROM CodiceSconto
		   					WHERE Codice=?;
							""";
			return _contex.eseguiSelectSingolo(sql, codiceScontoMapper, codice);
		}
		catch(SQLException e)
		{
			throw new SQLException("Errore durante la ricerca del codice sconto con codice: " + codice, e);
		}
    }
    
    public List<CodiceSconto> findAll(boolean onlyValid) throws SQLException 
    {
    	List<CodiceSconto> sconti;
		try
		{
		
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
				sconti = _contex.eseguiSelect(sql,codiceScontoMapper,java.sql.Date.valueOf(LocalDate.now()), 1);
			}
			else
			{
				sconti = _contex.eseguiSelect(sql, codiceScontoMapper);
			}

			return sconti;
		}
		catch(SQLException e)
		{
			throw new SQLException("Errore durante la ricerca dei codici sconto", e);
		}
    }
    
    public boolean isValid(CodiceSconto sconto) throws SQLException
    {
    	return sconto != null && sconto.isAttivo() && !sconto.getDataScadenza().isBefore(LocalDate.now());
    }
    
    private final ResultMapper<CodiceSconto> codiceScontoMapper = row->
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
