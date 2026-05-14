package it.mediclick.model.dao;

import it.mediclick.model.bean.Amministratore;
import it.mediclick.model.bean.Dipartimento;
import it.mediclick.model.bean.Utente;
import it.mediclick.util.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AmministratoreDAO 
{
    private Contex _contex;
    private UtenteDAO utente;
    
    public AmministratoreDAO(Contex contex) 
    {
        _contex = contex;
        utente = new UtenteDAO(contex);
    }

    public Optional<Amministratore> findById(int id) throws SQLException 
    {
        try 
        {
			String sqlAmmnistratore = 	"""
				                        SELECT * 
				                        FROM Amministratore 
				                        WHERE ID = ?
					                     """;
			
			return  _contex.eseguiSelectSingolo(sqlAmmnistratore,amministratoreMapper,id);
		} 
        catch (SQLException e)
        {
        	 throw new SQLException("Errore nel recupero dell'amministratore con ID " + id, e);
		}
                     
        
    }
    
    public Optional<Dipartimento> dipartimentoFindById(int id) throws SQLException 
    {
        try 
        {
			String sqlAmmnistratore = 	"""
				                        SELECT *
										FROM dipartimento
										WHERE id = ?
					                     """;
			
			return  _contex.eseguiSelectSingolo(sqlAmmnistratore,dipartimentoMapper,id);
		} 
        catch (SQLException e)
        {
        	 throw new SQLException("Errore nel recupero del dipartimento con ID " + id, e);
		}
                     
        
    }

    public List<Amministratore> findAll() throws SQLException 
    {
        try 
        {
			String sqlAmmnistratoreAll = 	"""
					                        SELECT * 
					                        FROM Amministratore
						                     """;
			
			return _contex.eseguiSelect(sqlAmmnistratoreAll, amministratoreMapper);
		} 
        catch (SQLException e) 
        {
			 throw new SQLException("Errore nel recupero di tutti gli amministratori", e);
		}
        
        
    }

    public int insert(Amministratore a) throws SQLException 
    {
        
                           
        String sqlAmministratore = """
                                    INSERT INTO Amministratore(ID,Dipartimento_ID) 
                                    VALUES (?,?)
                                   """;
        
        Connection conn = _contex.getConnection();
        conn.setAutoCommit(false);
        
        try
        {
        	Utente u = a.getUtente();
        	
	        int utenteId = utente.insert(conn,u);
	        Dipartimento d = a.getDipartimento().orElse(null);
	        
	        Integer dipartimentoId = d==null ? null : d.getId();
	
	        _contex.eseguiUpdate(sqlAmministratore,conn,utenteId,dipartimentoId);
	
	        a.setId(utenteId);
	        a.getUtente().setId(utenteId);
	        
	        conn.commit();
	        return utenteId;
        } 
	    catch (SQLException e) 
	    {
	    	conn.rollback();
	    	throw new SQLException("Errore nell'inserimento dell'amministratore: " , e);
	    }
        finally
        {
        	conn.close();
        }
    }
    
    public void updatePassword(int id, String password) throws SQLException
    {
    	utente.updatePassword(id, password);
    }
    
    private final ResultMapper <Dipartimento> dipartimentoMapper = rowDipartimento->
   	{
   		 Dipartimento dipartimento = new Dipartimento();
   		 
   		 dipartimento.setId(MapRow.getInt(rowDipartimento, "ID"));
   		 dipartimento.setNome(MapRow.getString(rowDipartimento, "Nome"));
   		 
   		 return dipartimento;
   	};
 
    private final ResultMapper<Amministratore> amministratoreMapper = row ->
    {
    	int id = MapRow.getInt(row, "ID");
    	Integer idDipartimento = MapRow.getIntOrNull(row, "Dipartimento_ID");
    	
    	Amministratore a = new Amministratore();
    	Utente u = utente.findById(id);
    	
    	a.setUtente(u);
     	a.setId(id);
    	
    	
    	if(idDipartimento!=null)
    	{
			a.setDipartimento(dipartimentoFindById(idDipartimento).orElseThrow(() -> new SQLException("Dipartimento " + idDipartimento + " non trovato")));
    	}
    	
        return a;
    };
    
   
}
