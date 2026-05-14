package it.mediclick.model.dao;


import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Categoria;
import it.mediclick.util.Contex;
import it.mediclick.util.MapRow;
import it.mediclick.util.ResultMapper;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CatalogoPrestazioniDAO 
{
    private Contex _contex;

    public CatalogoPrestazioniDAO(Contex contex) 
    {
        _contex = contex;
    }

    public Optional<CatalogoPrestazioni> findById(int id) throws SQLException 
    {
    	
    	 try
    	 {
	        String sql = 	"""
		                    SELECT * 
		                    FROM CatalogoPrestazioni 
		                    WHERE ID = ?
	        				""";
	                     
	        return  _contex.eseguiSelectSingolo(sql,catalogoMapper, id);  
        }
        catch(SQLException e)
        {
        	 throw new SQLException("Errore nel recupero della prestazione con ID " + id, e); 
        }
    }
    
    public List<CatalogoPrestazioni> findAll() throws SQLException 
    {
    	
    	 try
         {
	        String sql = 	"""
	                        SELECT * 
	                        FROM CatalogoPrestazioni
	                     	""";
	                     
	        return _contex.eseguiSelect(sql,catalogoMapper);
        
        }
        catch(SQLException e)
        {
        	 throw new SQLException("Errore nel recupero delle prestazioni", e); 
        }
    }

    public List<CatalogoPrestazioni> findByCategoria(int categoriaId) throws SQLException 
    {
        
        try
        {
	         String sql = 	"""
		                    SELECT * 
		                    FROM CatalogoPrestazioni 
		                    WHERE Categoria_ID = ?
	                  		""";
	                     
	        return _contex.eseguiSelect(sql,catalogoMapper,categoriaId);
       
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero delle prestazioni per la categoria con id" +categoriaId, e); 
       }
    }

    public List<CatalogoPrestazioni> findByStato(CatalogoPrestazioni.Stato stato) throws SQLException 
    {
                    
        try
        {
        	 String sql = """
                     SELECT * 
                     FROM CatalogoPrestazioni 
                     WHERE Stato = ?
                  """;
	                     
	        return _contex.eseguiSelect(sql,catalogoMapper,stato.name());
       
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero delle prestazioni per stato " +stato.name(), e); 
       }
    }

    public List<Categoria> findAllCategorie() throws SQLException 
    {             
        try
        {
        	 String sql = """
                     SELECT * 
                     FROM Categoria
                  """;
	                     
	        return _contex.eseguiSelect(sql,categoriaMapper);
       
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero delle categorie ", e); 
       }
    }
    
    public Optional<Categoria> findCategoriaById(int id) throws SQLException 
    {
                    
        try
   	 	{
        	  String sql = """
                      SELECT * 
                      FROM Categoria
                      WHERE ID = ?
                   """;
	                     
	        return  _contex.eseguiSelectSingolo(sql,categoriaMapper, id);  
       }
       catch(SQLException e)
       {
       	 throw new SQLException("Errore nel recupero della categoria con ID " + id, e); 
       }
    }

    public void insert(CatalogoPrestazioni cp) throws SQLException 
    {
        String sql = 	"""
                        INSERT INTO CatalogoPrestazioni(Nome, Stato, Descrizione, Categoria_ID) 
                        VALUES (?,?,?,?)
        				""";
        
        
        Integer categoriaId = cp.getCategoria() == null ? null : cp.getCategoria().getId();
        
        String catalogoNome = cp.getNome();
        String catalogoStato = cp.getStato() != null ? cp.getStato().getLabel() : "Attiva";
        String catalogoDescrizione = cp.getDescrizione();
  
        try
        {
            _contex.eseguiUpdate(sql,catalogoNome,catalogoStato,catalogoDescrizione,categoriaId);
        }
        catch(SQLException e)
        {
       	 	throw new SQLException("Errore nell'inserimento della prestazione", e); 
        }
    }

    public void updateStato(int id, CatalogoPrestazioni.Stato stato) throws SQLException 
    {
        String sql = 	"""
                        UPDATE CatalogoPrestazioni 
                        SET Stato = ? 
                        WHERE ID = ?
                     	""";
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
        	throw new SQLException("Errore nell'update dello stato della prestazione", e);
        }
    }
    
    private final ResultMapper<Categoria> categoriaMapper = row ->
    {
    	Categoria c = new Categoria();
    	
    	c.setId(MapRow.getInt(row,"ID"));
    	c.setNome(MapRow.getString(row, "Nome"));
    	
    	return c;
    };

    private final ResultMapper<CatalogoPrestazioni> catalogoMapper = row ->
    {
    	int id = MapRow.getInt(row, "ID");
    	
    	Integer categoriaID = MapRow.getIntOrNull(row, "Categoria_ID");
    	
    	CatalogoPrestazioni catalogo = new CatalogoPrestazioni();
    	
    	catalogo.setId(id);
    	catalogo.setNome(MapRow.getString(row, "Nome"));
    	catalogo.setStato(CatalogoPrestazioni.Stato.fromString(MapRow.getString(row, "Stato")));
    	catalogo.setDescrizione(MapRow.getString(row,"Descrizione"));
    	
    	
    	if(categoriaID!=null)
    	{
    		catalogo.setCategoria(findCategoriaById(categoriaID).orElseThrow(()->new SQLException("Categoria " + categoriaID + " non trovata")));
    	}
    	
        return catalogo;
    };
}
