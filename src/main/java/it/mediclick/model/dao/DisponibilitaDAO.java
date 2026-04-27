package it.mediclick.model.dao;

import it.mediclick.model.bean.Disponibilita;
import it.mediclick.util.Contex;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DisponibilitaDAO {
    private Contex _contex;

    public DisponibilitaDAO(Contex contex) {
        _contex = contex;
    }

    public Disponibilita findById(int id) throws SQLException {
        String sql = """
                   SELECT *
                   FROM Disponibilita
                   WHERE ID = ?
                """;

        List<Map<String, Object>> result = _contex.eseguiSelect(sql, id);

        if (result == null || result.isEmpty())
            return null;

        try {
            return mapping(result.get(0));
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca della disponibilita by id: " + e.getMessage());
            throw e;
        }
    }

    public List<Disponibilita> findByMedico(int medicoId) throws SQLException {
        String sql = """
                   SELECT *
                   FROM Disponibilita
                   WHERE Medico_ID = ?
                """;

        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<Disponibilita> list = new ArrayList<>();

        if (result == null || result.isEmpty())
            return list;

        try {
            for (Map<String, Object> map : result) {
                list.add(mapping(map));
            }
            return list;
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca disponibilita per medico: " + e.getMessage());
            throw e;
        }
    }

    public List<Disponibilita> findByMedicoEData(int medicoId, LocalDate data) throws SQLException {
        String sql = """
                   SELECT *
                   FROM Disponibilita
                   WHERE Medico_ID = ? AND DATE(Data_Ora_Inizio) = ?
                """;

        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId, java.sql.Date.valueOf(data));
        List<Disponibilita> list = new ArrayList<>();

        if (result == null || result.isEmpty())
            return list;

        try {
            for (Map<String, Object> map : result) {
                list.add(mapping(map));
            }
            return list;
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca disponibilita per medico e data: " + e.getMessage());
            throw e;
        }
    }

    public List<Disponibilita> findDisponibili(int medicoId) throws SQLException {
        String sql = """
                   SELECT *
                   FROM Disponibilita
                   WHERE Medico_ID = ?
                   AND (Stato = 'Disponibile' OR (Stato = 'Bloccata' AND Timestamp_Blocco < DATE_SUB(NOW(), INTERVAL 15 MINUTE)))
                   AND Data_Ora_Inizio > NOW()
                """;

        List<Map<String, Object>> result = _contex.eseguiSelect(sql, medicoId);
        List<Disponibilita> list = new ArrayList<>();

        if (result == null || result.isEmpty())
            return list;

        try {
            for (Map<String, Object> map : result) {
                list.add(mapping(map));
            }
            return list;
        } catch (SQLException e) {
            System.err.println("Errore nella ricerca delle disponibilita attive per medico: " + e.getMessage());
            throw e;
        }
    }

    public void insert(Disponibilita d) throws SQLException {
        String sql = """
                   INSERT INTO Disponibilita(Medico_ID, Studio_ID, Data_Ora_Inizio, Data_Ora_Fine, Stato, Timestamp_Blocco, Paziente_ID_Blocco)
                   VALUES (?,?,?,?,?,?,?)
                """;
        try {
            _contex.eseguiUpdate(sql,
                    d.getMedicoId(),
                    d.getStudioId(),
                    d.getDataOraInizio() != null ? Timestamp.valueOf(d.getDataOraInizio()) : null,
                    d.getDataOraFine() != null ? Timestamp.valueOf(d.getDataOraFine()) : null,
                    d.getStato().getLabel(),
                    d.getTimestampBlocco() != null ? Timestamp.valueOf(d.getTimestampBlocco()) : null,
                    d.getPazienteIdBlocco());
        } catch (SQLException e) {
            System.err.println("Errore nell'inserimento della disponibilita: " + e.getMessage());
            throw e;
        }
    }
    
    public void updateStato(int id, Disponibilita.Stato stato) throws SQLException 
    {
    	try(Connection conn = _contex.getConnection())
    	{
    		updateStato(id, stato,conn);	
    	}
        
    }
    
    public void updateStato(int id, Disponibilita.Stato stato,Connection conn) throws SQLException {
        String sql = """
                   UPDATE Disponibilita
                   SET Stato = ?
                   WHERE ID = ?
                """;
        try {
            if (!stato.equals(Disponibilita.Stato.BLOCCATA))
                _contex.eseguiUpdate(sql,conn,stato.getLabel(), id);
        } catch (SQLException e) {
            System.err.println("Errore nell'aggiornamento stato della disponibilita: " + e.getMessage());
            throw e;
        }
    }

    

    public void setBlocco(int id, Integer pazienteId, boolean blocca) throws SQLException {
        String sql = """
                   UPDATE Disponibilita
                   SET Stato = ?, Timestamp_Blocco = ?, Paziente_ID_Blocco = ?
                   WHERE ID = ?
                """;
        String stato = blocca ? Disponibilita.Stato.BLOCCATA.getLabel() : Disponibilita.Stato.DISPONIBILE.getLabel();
        LocalDateTime ts = blocca ? LocalDateTime.now() : null;
        Integer pId = blocca ? pazienteId : null;

        try {
            _contex.eseguiUpdate(sql, stato, ts, pId, id);
        } catch (SQLException e) {
            System.err.println("Errore nel settare il blocco della disponibilita: " + e.getMessage());
            throw e;
        }
    }

    public void deleteLogic(int id) throws SQLException {
        String sql = """
                   UPDATE Disponibilita
                   SET Stato = ?
                   WHERE ID = ?
                """;
        try {
            _contex.eseguiUpdate(sql, Disponibilita.Stato.CANCELLATA.getLabel(), id);
        } catch (SQLException e) {
            System.err.println("Errore nella cancellazione logica della disponibilita: " + e.getMessage());
            throw e;
        }
    }

    public void delete(int id) throws SQLException {
        String sql = """
                   DELETE FROM Disponibilita
                   WHERE ID = ?
                """;
        try {
            _contex.eseguiUpdate(sql, id);
        } catch (SQLException e) {
            System.err.println("Errore nella cancellazione della disponibilita: " + e.getMessage());
            throw e;
        }
    }

    private Disponibilita mapping(Map<String, Object> map) throws SQLException {
        if (map == null)
            return null;

        try {
            Disponibilita d = new Disponibilita();
            d.setId(Integer.parseInt(String.valueOf(map.get("ID"))));
            d.setMedicoId(Integer.parseInt(String.valueOf(map.get("Medico_ID"))));

            if (map.get("Studio_ID") != null) {
                d.setStudioId(Integer.parseInt(String.valueOf(map.get("Studio_ID"))));
            }

            if (map.get("Data_Ora_Inizio") != null) {
                LocalDateTime dataOraInizio = LocalDateTime.parse(String.valueOf(map.get("Data_Ora_Inizio")));
                d.setDataOraInizio(dataOraInizio);
            }

            if (map.get("Data_Ora_Fine") != null) {
                LocalDateTime dataOraFine = LocalDateTime.parse(String.valueOf(map.get("Data_Ora_Fine")));
                d.setDataOraFine(dataOraFine);
            }

            d.setStato(Disponibilita.Stato.fromString((String) map.get("Stato")));

            if (map.get("Timestamp_Blocco") != null) {
                LocalDateTime dataOraBlocco = LocalDateTime.parse(String.valueOf(map.get("Timestamp_Blocco")));
                d.setTimestampBlocco(dataOraBlocco);
            }

            if (map.get("Paziente_ID_Blocco") != null) {
                d.setPazienteIdBlocco(Integer.parseInt(String.valueOf(map.get("Paziente_ID_Blocco"))));
            }

            return d;
        } catch (Exception e) {
            throw new SQLException("Errore durante il mapping della Disponibilita: " + e.getMessage(), e);
        }
    }
}
