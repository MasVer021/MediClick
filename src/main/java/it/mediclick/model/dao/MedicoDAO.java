package it.mediclick.model.dao;

import it.mediclick.model.bean.Medico;
import it.mediclick.model.bean.RegimeFiscale;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.mediclick.model.DTO.MedicoCardDTO;
import it.mediclick.model.bean.Categoria;

public class MedicoDAO 
{
    private Contex _contex;
    private UtenteDAO utente;

    public MedicoDAO(Contex contex) 
    {
        _contex = contex;
        utente = new UtenteDAO(contex);
    }

    public Optional<Medico> findById(int id) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Medico 
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
             System.err.println("Errore nella ricerca del medico by id: " + e.getMessage());
             throw e;
        }
    }

    public List<Medico> findAll(String query, Integer categoriaId, String citta) throws SQLException 
    {
        StringBuilder sql = new StringBuilder("SELECT DISTINCT M.* FROM Medico M ");
        
        if (categoriaId != null) {
            sql.append("JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID ");
            sql.append("JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID ");
        }
        
        if (citta != null && !citta.isEmpty()) 
        {
            if (categoriaId == null) 
            {
                sql.append("JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID ");
            }
            sql.append("JOIN Studio S ON EP.Studio_ID = S.ID ");
        }

        sql.append("WHERE M.Stato_verifica = 'Approvato' ");
        List<Object> params = new ArrayList<>();

        if (query != null && !query.isEmpty()) 
        {
            sql.append("AND (M.Cognome LIKE ? OR M.Nome LIKE ?) ");
            params.add("%" + query + "%");
            params.add("%" + query + "%");
        }

        if (categoriaId != null) 
        {
            sql.append("AND CP.Categoria_ID = ? ");
            params.add(categoriaId);
        }

        if (citta != null && !citta.isEmpty()) 
        {
            sql.append("AND S.Citta = ? ");
            params.add(citta);
        }

        List<Map<String, Object>> result = _contex.eseguiSelect(sql.toString(), params.toArray());
        List<Medico> medici = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return medici;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                medici.add(mapping(map));
            }
            return medici;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca filtrata dei medici: " + e.getMessage());
             throw e;
        }
    }

    public List<Medico> findAll() throws SQLException 
    {
        return findAll(null, null, null);
    }

    public List<MedicoCardDTO> findCards(String query, Integer categoriaId, String citta) throws SQLException 
    {
        StringBuilder sql = new StringBuilder("""
            SELECT 
                M.*, 
                MIN(C.ID) as Cat_ID, MIN(C.Nome) as Categoria_Nome,
        		MIN(S.Indirizzo_Maps) as Indirizzo_Studio, MIN(S.Citta) as Citta_Studio,
        		MIN(EP.Prezzo_Lordo_Listino) as Costo,
                (SELECT AVG(Voto) FROM Recensione WHERE Medico_ID = M.ID) as Media_Recensioni,
                (SELECT COUNT(*) FROM Recensione WHERE Medico_ID = M.ID) as Num_Recensioni,
                (SELECT MIN(Data_Ora_Inizio) FROM Disponibilita D 
                 WHERE D.Medico_ID = M.ID AND D.Stato = 'Disponibile' AND D.Data_Ora_Inizio > NOW()) as Prima_Disponibilita
            FROM Medico M
            JOIN ErogazionePrestazione EP ON M.ID = EP.Medico_ID
            JOIN CatalogoPrestazioni CP ON EP.CatalogoPrestazioni_ID = CP.ID
            JOIN Categoria C ON CP.Categoria_ID = C.ID
            JOIN Studio S ON EP.Studio_ID = S.ID
            WHERE M.Stato_verifica = 'Approvato' 
        """);

        List<Object> params = new ArrayList<>();

        if (query != null && !query.isEmpty()) 
        {
            sql.append("AND (M.Cognome LIKE ? OR M.Nome LIKE ?) ");
            params.add("%" + query + "%");
            params.add("%" + query + "%");
        }

        if (categoriaId != null && categoriaId > 0) 
        {
            sql.append("AND CP.Categoria_ID = ? ");
            params.add(categoriaId);
        }

        if (citta != null && !citta.isEmpty()) 
        {
            sql.append("AND S.Citta = ? ");
            params.add(citta);
        }
        
        sql.append("GROUP BY M.ID ");

        List<Map<String, Object>> result = _contex.eseguiSelect(sql.toString(), params.toArray());
        List<MedicoCardDTO> cards = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return cards;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                cards.add(mappingCard(map));
            }
            return cards;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca delle card medici: " + e.getMessage());
             throw e;
        }
    }

    public List<Medico> findByStato(Medico.StatoVerifica stato) throws SQLException 
    {
        String sql = """
                        SELECT * 
                        FROM Medico 
                        WHERE Stato_verifica = ?
                     """;
                     
        List<Map<String, Object>> result = _contex.eseguiSelect(sql, stato.getLabel());
        List<Medico> medici = new ArrayList<>();
        
        if (result == null || result.isEmpty())
            return medici;
            
        try
        {
            for (Map<String, Object> map : result) 
            {
                medici.add(mapping(map));
            }
            return medici;
        }
        catch(SQLException e)
        {
             System.err.println("Errore nella ricerca medici per stato: " + e.getMessage());
             throw e;
        }
    }

    public int insert(Medico m) throws SQLException 
    {                          
        String sqlMedico = """
                            INSERT INTO Medico(ID,Cognome,Nome,Fotoprofilo,Bio,P_Iva,Stato_verifica,Regime_fiscale) 
                            VALUES (?,?,?,?,?,?,?,?)
                           """;
        
        Connection conn = _contex.getConnection();
        conn.setAutoCommit(false);
        try
        {
        	int utenteId = utente.insert(conn,m.getUtente());
        	
            _contex.eseguiUpdate(sqlMedico,
            	conn,
                utenteId,
                m.getCognome(),
                m.getNome(),
                m.getFotoprofilo(),
                m.getBio(),
                m.getpIva(),
                m.getStatoVerifica() != null ? m.getStatoVerifica().getLabel() : "In attesa",
                m.getRegimeFiscaleId()
            );

            m.setId(utenteId);
           
            m.getUtente().setId(utenteId);
            conn.commit();
            return utenteId;
        } 
        catch (SQLException e) 
        {
        	conn.rollback();
            System.err.println("Errore nell'inserimento del medico: " + e.getMessage());
            throw e;
        }
        finally
        {
        	conn.close();
        }
    }
    
    public void update(Medico m) throws SQLException
    {
    	String sql = """
		                UPDATE Medico 
		                SET Cognome = ?, Nome = ?, Fotoprofilo = ?, Bio = ?,P_Iva = ?  
		                WHERE ID = ?
    				""";
    	try 
    	{
			_contex.eseguiUpdate(sql, m.getCognome(), m.getNome(), m.getFotoprofilo(), m.getBio(), m.getpIva(), m.getId());
		} 
    	catch (SQLException e) 
    	{
			 System.err.println("Errore nell'aggiornamento del medico: " + e.getMessage());
             throw e;
		}
    }
    
    public void updatePassword(int id, String password) throws SQLException
    {
    	utente.updatePassword(id, password);
    }
    
    public void updateStatoVerifica(int id, Medico.StatoVerifica stato) throws SQLException 
    {
        String sql = """
                        UPDATE Medico 
                        SET Stato_verifica = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, stato.getLabel(), id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento stato verifica medico: " + e.getMessage());
             throw e;
        }
    }

    public void updateRegimeFiscale(int id, int regimeFiscaleId) throws SQLException 
    {
        String sql = """
                        UPDATE Medico 
                        SET Regime_fiscale = ? 
                        WHERE ID = ?
                     """;
        try
        {
            _contex.eseguiUpdate(sql, regimeFiscaleId, id);
        }
        catch(SQLException e)
        {
             System.err.println("Errore nell'aggiornamento regime fiscale medico: " + e.getMessage());
             throw e;
        }
    }

    private Medico mapping(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            Medico m = new Medico();
            int id = Integer.parseInt(String.valueOf(map.get("ID")));
            
            String SQLregimeFiscale = 	"""
            						SELECT *
            						FROM regime_fiscale
            						WHERE ID = ?
            						""";
            
           RegimeFiscale regimeFiscale = mappingRegimeFiscale(_contex.eseguiSelect(SQLregimeFiscale, map.get("regime_fiscale")).getFirst());
           
           
          
            
            
            
            
            m.setId(id);
            m.setCognome((String) map.get("Cognome"));
            m.setNome((String) map.get("Nome"));
            m.setFotoprofilo((String) map.get("Fotoprofilo"));
            m.setBio((String) map.get("Bio"));
            m.setpIva((String) map.get("P_Iva"));
            m.setRegimeFiscale(regimeFiscale);

            String statoStr = (String) map.get("Stato_verifica");
            
            m.setStatoVerifica(Medico.StatoVerifica.fromString(statoStr));
            
            
            if (map.get("Regime_fiscale") != null) 
            {
                m.setRegimeFiscaleId(Integer.parseInt(String.valueOf(map.get("Regime_fiscale"))));
            }
            
            m.setUtente(utente.findById(id));
            
            return m;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping del Medico: " + e.getMessage(), e);
        }
    }
    
    
    private RegimeFiscale mappingRegimeFiscale(Map<String, Object> map) throws SQLException
	{
		RegimeFiscale regimeFiscale = new RegimeFiscale();
		
		int id =Integer.parseInt(String.valueOf(map.get("id")));
		int aliquota = Integer.parseInt(String.valueOf(map.get("Aliquota_Default")));
		String descrizione = String.valueOf(map.get("Descrizione"));
		String nome = String.valueOf(map.get("Nome"));
		
		regimeFiscale.setId(id);
		regimeFiscale.setAliquotaDefault(aliquota);
		regimeFiscale.setDescrizione(descrizione);
		regimeFiscale.setNome(nome);
		
		return regimeFiscale;
	}
    
    
    
    

    private MedicoCardDTO mappingCard(Map<String, Object> map) throws SQLException 
    {
        if (map == null) 
            return null;
            
        try 
        {
            MedicoCardDTO dto = new MedicoCardDTO();
            
           
            Medico m = mapping(map); 
            dto.setMedico(m);
            
           
            Categoria c = new Categoria();
            if (map.get("Cat_ID") != null) 
            {
                c.setId(Integer.parseInt(String.valueOf(map.get("Cat_ID"))));
                c.setNome((String) map.get("Categoria_Nome"));
            }
            dto.setCategoria(c);
            
           
            String indirizzo = (String) map.get("Indirizzo_Studio");
            String citta = (String) map.get("Citta_Studio");
            if (indirizzo != null && citta != null) 
            {
                dto.setIndirizzo(indirizzo + ", " + citta);
            }
            
            if (map.get("Costo") != null) 
            {
                dto.setCosto(Double.parseDouble(String.valueOf(map.get("Costo"))));
            }
            
           
            if (map.get("Media_Recensioni") != null) 
            {
 
                dto.setValoreRecensioni((int) Math.round(Double.parseDouble(String.valueOf(map.get("Media_Recensioni")))));
            }
            if (map.get("Num_Recensioni") != null) 
            {
                dto.setNumeroRecensioni(Integer.parseInt(String.valueOf(map.get("Num_Recensioni"))));
            }
            
            
            if (map.get("Prima_Disponibilita") != null) 
            {
                Timestamp ts = Timestamp.valueOf(String.valueOf(map.get("Prima_Disponibilita")));
                dto.setPrimaDisponibilita(ts.toLocalDateTime());
            }
            
            return dto;
        } 
        catch (Exception e) 
        {
            throw new SQLException("Errore durante il mapping di MedicoCardDTO: " + e.getMessage(), e);
        }
    }
}
