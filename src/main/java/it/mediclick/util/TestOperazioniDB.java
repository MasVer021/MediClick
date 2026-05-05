package it.mediclick.util;

import it.mediclick.model.dao.*;
import it.mediclick.model.bean.*;


public class TestOperazioniDB {

    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/MediClick";
        String user = "root";
        String password = "";

        Contex db = new Contex(url, user, password);

        try {
            System.out.println("--- Test UtenteDAO ---");
            UtenteDAO utenteDAO = new UtenteDAO(db);
            System.out.println("findById(1): " + utenteDAO.findById(1));
            System.out.println("findByEmail('admin.rossi@mediclick.it'): " + utenteDAO.findByEmail("admin.rossi@mediclick.it"));

            System.out.println("\n--- Test MedicoDAO ---");
            MedicoDAO medicoDAO = new MedicoDAO(db);
            System.out.println("findById(1): " + medicoDAO.findById(1));
            System.out.println("findAll(): " + medicoDAO.findAll()+ " medici trovati");
            System.out.println("findAll(): " + medicoDAO.findByStato(Medico.StatoVerifica.APPROVATO) + " medici approvati trovati");
            
            
            System.out.println("\n--- Test PazienteDAO ---");
            PazienteDAO pazienteDAO = new PazienteDAO(db);
            System.out.println("findById(1): " + pazienteDAO.findById(1));

            System.out.println("\n--- Test AmministratoreDAO ---");
            AmministratoreDAO ammDAO = new AmministratoreDAO(db);
            System.out.println("findById(1): " + ammDAO.findById(1));
            System.out.println("findAll(): " + ammDAO.findAll().size() + " amministratori trovati");

            System.out.println("\n--- Test DisponibilitaDAO ---");
            DisponibilitaDAO dispDAO = new DisponibilitaDAO(db);
            System.out.println("findById(1): " + dispDAO.findById(1));
            System.out.println("findByMedico(1): " + dispDAO.findByMedico(1).size() + " disponibilità trovate");

            System.out.println("\n--- Test ErogazionePrestazioneDAO ---");
            ErogazionePrestazioneDAO epDAO = new ErogazionePrestazioneDAO(db);
            System.out.println("findById(1): " + epDAO.findById(1));
            System.out.println("findByMedico(1): " + epDAO.findByMedico(1).size() + " erogazioni trovate");

            System.out.println("\n--- Test CatalogoPrestazioniDAO ---");
            CatalogoPrestazioniDAO catDAO = new CatalogoPrestazioniDAO(db);
            System.out.println("findById(1): " + catDAO.findById(1));
            System.out.println("findAll(): " + catDAO.findAll().size() + " prestazioni in catalogo");
            System.out.println("findAllCategorie(): " + catDAO.findAllCategorie().size() + " categorie trovate");

            System.out.println("\n--- Test PrenotazioneDAO ---");
            PrenotazioneDAO prenDAO = new PrenotazioneDAO(db);
            System.out.println("findById(1): " + prenDAO.findById(1));
            System.out.println("findByMedico(1): " + prenDAO.findByMedico(1).size() + " prenotazioni trovate");

            System.out.println("\n--- Test RecensioneDAO ---");
            RecensioneDAO recDAO = new RecensioneDAO(db);
            System.out.println("findById(1): " + recDAO.findById(1));
            System.out.println("findByMedico(1): " + recDAO.findByMedico(1).size() + " recensioni trovate");

            System.out.println("\n--- Test CertificatoDAO ---");
            CertificatoDAO certDAO = new CertificatoDAO(db);
            System.out.println("findById(1): " + certDAO.findById(1));
            System.out.println("findByMedico(1): " + certDAO.findByMedico(1).size() + " certificati trovati");

            System.out.println("\n--- Test RuoloDAO ---");
            RuoloDAO ruoloDAO = new RuoloDAO(db);
            System.out.println("findById(1): " + ruoloDAO.findById(1));
            System.out.println("findAll(): " + ruoloDAO.findAll().size() + " ruoli trovati");
            System.out.println("findPermessiByRuolo(1): " + ruoloDAO.findPermessiByRuolo(1).size() + " permessi trovati");

            // --- TEST DEI SERVICE (Logica per le Servlet) ---
            System.out.println("\n--- Test AutenticazioneService (Registrazione e Login) ---");
            it.mediclick.service.AutenticazioneService authService = new it.mediclick.service.AutenticazioneService(db);
            
            String testEmail = "test.paziente" + System.currentTimeMillis() + "@email.it";
            String testPass = "Password123!";
            
           
            System.out.println( PasswordUtils.hashPassword(testPass));
            
            
            Paziente nuovoP = new Paziente();
            nuovoP.setNome("Test");
            nuovoP.setCognome("User");
            nuovoP.setCodiceFiscale("TST" + (System.currentTimeMillis() % 10000000000000L)); 
            nuovoP.setDataNascita(java.time.LocalDate.of(1990, 1, 1));
            
            Utente ut = new Utente();
            ut.setEmail(testEmail);
            ut.setRuoloId(3); 
            ut.setAccountAttivo(true);
            ut.setDataIscrizione(java.time.LocalDate.now());
            nuovoP.setUtente(ut);
            
            int idGenerato = authService.registraPaziente(nuovoP, testPass);
            System.out.println("Registrazione nuovo paziente: " + (idGenerato > 0 ? "OK (ID: " + idGenerato + ")" : "FALLITA"));
            
            Utente loggato = authService.login(testEmail, testPass);
            System.out.println("Login nuovo utente: " + (loggato != null ? "SUCCESSO" : "FALLITO"));

            System.out.println("\n--- Test RicercaService (Filtri Dinamici) ---");
            it.mediclick.service.RicercaService ricercaService = new it.mediclick.service.RicercaService(db);
            System.out.println("Ricerca medici a 'Milano': " + ricercaService.cercaMedici(null, null, "Milano").size());
            System.out.println("Ricerca medici query 'Bianchi': " + ricercaService.cercaMedici("Bianchi", null, null).size());

            System.out.println("\n--- Test PrenotazioneService (Flusso di Blocco) ---");
            it.mediclick.service.PrenotazioneService prenService = new it.mediclick.service.PrenotazioneService(db);
            // Usiamo l'ID generato sopra per essere sicuri che il paziente esista nel DB
            boolean bloccato = prenService.bloccaDisponibilita(6, idGenerato);
            System.out.println("Blocco slot ID 5 per paziente " + idGenerato + ": " + (bloccato ? "SUCCESSO" : "FALLITO (già occupato o inesistente)"));

        } 
        catch (Exception e) 
        {
            System.err.println("Errore durante i test: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
