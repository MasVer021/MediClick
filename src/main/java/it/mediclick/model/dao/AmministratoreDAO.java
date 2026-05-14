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
    private final Contex _contex;
    private final UtenteDAO utenteDAO;
    
    public AmministratoreDAO(Contex contex) 
    {
        _contex = contex;
        utenteDAO = new UtenteDAO(contex);
    }

    public Optional<Amministratore> findById(int id) throws SQLException 
    {
        try 
        {
			String sqlAmmnistratore = 	"""
				                        SELECT * 
				                        FROM Amministratore as a
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
        	
	        int utenteId = utenteDAO.insert(conn,u);
	      
	        Integer dipartimentoId =a.getDipartimentoId() > 0 ? a.getDipartimentoId() : null;
	
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
    	utenteDAO.updatePassword(id, password);
    }

	public void getCompleto(Amministratore a) throws SQLException
	{

		if(a.getDipartimentoId() > 0)
		{
			Dipartimento d = dipartimentoFindById(a.getDipartimentoId()).orElseThrow(() -> new SQLException("Dipartimento con ID " + a.getDipartimentoId() + " non trovato"));
			a.setDipartimento(d);
		}

		Utente u = utenteDAO.findById(a.getId()).orElseThrow(() -> new SQLException("Utente con ID " + a.getId() + " non trovato"));
		a.setUtente(u);
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
		
    	Integer idDipartimento = MapRow.getIntOrNull(row, "Dipartimento_ID") ;
		idDipartimento = idDipartimento != null ? idDipartimento : -1;

    	Amministratore a = new Amministratore();
    	
		a.setDipartimentoId(idDipartimento);
     	a.setId(id);

        return a;
    };
    
   
}
