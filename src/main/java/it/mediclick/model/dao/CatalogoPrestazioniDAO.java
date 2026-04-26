package it.mediclick.model.dao;

import it.mediclick.model.bean.CatalogoPrestazioni;
import it.mediclick.model.bean.Categoria;
import it.mediclick.util.Contex;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CatalogoPrestazioniDAO 
{
    private Contex _contex;

    public CatalogoPrestazioniDAO(Contex contex) 
    {
        _contex = contex;
    }

    public CatalogoPrestazioni findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM CatalogoPrestazioni 
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
             System.err.println("Errore nella ricerca del catalogo prestazioni by id: " + e.getMessage());
             throw e;
        }
    }

    public List<CatalogoPrestazioni> findAll() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM CatalogoPrestazioni
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<CatalogoPrestazioni> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca di tutti i cataloghi prestazioni: " + e.getMessage());
             throw e;
        }
    }

    public List<CatalogoPrestazioni> findByCategoria(int categoriaId) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM CatalogoPrestazioni 
                        WHERE Categoria_ID = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, categoriaId);
        List<CatalogoPrestazioni> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca cataloghi prestazioni per categoria: " + e.getMessage());
             throw e;
        }
    }

    public List<CatalogoPrestazioni> findByStato(CatalogoPrestazioni.Stato stato) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM CatalogoPrestazioni 
                        WHERE Stato = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, stato.getLabel());
        List<CatalogoPrestazioni> list = new ArrayList<>();
        
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
             System.err.println("Errore nella ricerca cataloghi prestazioni per stato: " + e.getMessage());
             throw e;
        }
    }

    public List<Categoria> findAllCategorie() throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Categoria
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql);
        List<Categoria> list = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return list;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                list.add(mappingCat(map));
            }
            return list;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca di tutte le categorie: " + e.getMessage());
             throw e;
        }
    }

    public void insert(CatalogoPrestazioni cp) throws SQLException 
    {
        String sql = """
                        INSERT INTO CatalogoPrestazioni(Nome, Stato, Descrizione, Categoria_ID) 
                        VALUES (?,?,?,?)
                     """;
        try
        {
            _contex.eseguiUpdate(sql, 
                cp.getNome(),
                cp.getStato() != null ? cp.getStato().getLabel() : "Attiva",
                cp.getDescrizione(),
                cp.getCategoriaId()
            );
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'inserimento del catalogo prestazioni: " + e.getMessage());
             throw e;
        }
    }

    public void updateStato(int id, CatalogoPrestazioni.Stato stato) throws SQLException 
    {
        String sql = """
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
             System.err.println("Errore nell'aggiornamento stato del catalogo prestazioni: " + e.getMessage());
             throw e;
        }
    }
    
    private Categoria mappingCat (Map<String, Object> map) throws SQLException
    {
    	if (map == null) 
            return null;
    	
    	try 
    	{
			Categoria c = new Categoria();
			c.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
			c.setNome((String) map.get("Nome"));
			
			return c;
		} 
    	catch (Exception e) 
    	{
			
			throw new SQLException("Errore durante il mapping delle categorie : " + e.getMessage(), e);
		}
    }

    private CatalogoPrestazioni mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            CatalogoPrestazioni cp = new CatalogoPrestazioni();
            cp.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            cp.setNome((String) map.get("Nome"));
            cp.setDescrizione((String) map.get("Descrizione"));
            
            if (map.get("Categoria_ID") != null) 
            {
                cp.setCategoriaId(Integer.parseInt(String.valueOf(map.get("Categoria_ID"))));
            }

            String statoStr = (String) map.get("Stato");
            if (statoStr != null) 
            {
                for (CatalogoPrestazioni.Stato s : CatalogoPrestazioni.Stato.values()) 
                {
                    if (s.getLabel().equalsIgnoreCase(statoStr) || s.name().equalsIgnoreCase(statoStr)) 
                    {
                        cp.setStato(s);
                        break;
                    }
                }
            }

            return cp;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di CatalogoPrestazioni: " + e.getMessage(), e);
        }
    }
}
