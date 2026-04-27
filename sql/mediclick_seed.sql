-- ============================================================
-- MediClick - Dati di Test (SEED)
-- Eseguire DOPO mediclick_schema.sql
-- ============================================================
use mediclick;
-- ============================================================
-- 1. PERMESSI
-- ============================================================
INSERT INTO Permesso (Codice, Descrizione) VALUES
('GESTIONE_UTENTI',       'Crea, modifica ed elimina utenti'),
('APPROVAZIONE_MEDICI',   'Approva o rifiuta la verifica dei medici'),
('GESTIONE_CATALOGO',     'Gestisce il catalogo prestazioni'),
('GESTIONE_IMPOSTAZIONI', 'Modifica le impostazioni di sistema'),
('VISUALIZZA_REPORT',     'Accede ai report e statistiche'),
('PRENOTA_VISITA',        'Effettua prenotazioni'),
('GESTIONE_AGENDA',       'Gestisce disponibilità e agenda'),
('VISUALIZZA_RECENSIONI', 'Visualizza e modera le recensioni');

-- ============================================================
-- 2. RUOLI
-- ============================================================
INSERT INTO Ruolo (Codice, Descrizione) VALUES
('ADMIN',    'Amministratore di sistema'),
('MEDICO',   'Medico professionista'),
('PAZIENTE', 'Paziente registrato');

-- Permessi per ADMIN (tutti)
INSERT INTO Caratterizzato (Ruolo_ID, Permesso_ID) VALUES
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8);

-- Permessi per MEDICO
INSERT INTO Caratterizzato (Ruolo_ID, Permesso_ID) VALUES
(2,7),(2,8),(2,5);

-- Permessi per PAZIENTE
INSERT INTO Caratterizzato (Ruolo_ID, Permesso_ID) VALUES
(3,6),(3,8);

-- ============================================================
-- 3. DIPARTIMENTI
-- ============================================================
INSERT INTO Dipartimento (Nome) VALUES
('Amministrazione'),
('Supporto Medico'),
('Compliance');

-- ============================================================
-- 4. REGIME FISCALE
-- ============================================================
INSERT INTO Regime_Fiscale (Nome, Aliquota_Default, Descrizione) VALUES
('Regime Ordinario',       22.00, 'IVA ordinaria al 22%'),
('Regime Forfettario',      0.00, 'Regime agevolato per professionisti under soglia'),
('Regime Minimi',           5.00, 'Imposta sostitutiva al 5% per i primi 5 anni'),
('Esenzione IVA Art.10',    0.00, 'Prestazioni sanitarie esenti IVA ex art.10 DPR 633/72');

-- ============================================================
-- 5. CATEGORIE PRESTAZIONI
-- ============================================================
INSERT INTO Categoria (Nome) VALUES
('Medicina Generale'),
('Cardiologia'),
('Dermatologia'),
('Ortopedia'),
('Psicologia'),
('Fisioterapia'),
('Oculistica'),
('Ginecologia');

-- ============================================================
-- 6. CATALOGO PRESTAZIONI
-- ============================================================
INSERT INTO CatalogoPrestazioni (Nome, Stato, Descrizione, Categoria_ID) VALUES
('Visita Medica Generica',       'Attiva', 'Visita ambulatoriale di medicina generale',               1),
('ECG a Riposo',                 'Attiva', 'Elettrocardiogramma standard a 12 derivazioni',           2),
('Ecocardiogramma',              'Attiva', 'Ecografia del cuore con Doppler',                         2),
('Visita Dermatologica',         'Attiva', 'Valutazione dermatologica generale e mappatura nei',      3),
('Dermatoscopia',                'Attiva', 'Analisi dermoscopica dei nei',                            3),
('Visita Ortopedica',            'Attiva', 'Valutazione ortopedica e funzionale',                     4),
('Rx Colonna Lombare',           'Attiva', 'Radiografia colonna lombare AP e LL',                     4),
('Seduta Psicologica',           'Attiva', 'Colloquio psicologico individuale 50 minuti',             5),
('Test Psicodiagnostico',        'Attiva', 'Somministrazione e scoring test standardizzati',          5),
('Fisioterapia Manuale',         'Attiva', 'Trattamento manuale di fisioterapia 45 minuti',           6),
('Tecar Terapia',                'Attiva', 'Tecarterapia resistiva e capacitiva',                     6),
('Visita Oculistica',            'Attiva', 'Visita oculistica completa con refrazione',               7),
('Fondo Oculare',                'Attiva', 'Esame del fundus oculi con oftalmoscopio',                7),
('Visita Ginecologica',          'Attiva', 'Visita ginecologica con pap-test',                        8),
('Ecografia Pelvica',            'Attiva', 'Ecografia pelvica trans-addominale e trans-vaginale',     8),
('Visita Cardiologica',          'Disattiva','Visita cardiologica completa (non più disponibile)',     2);

-- ============================================================
-- 7. TIPI CERTIFICATO
-- ============================================================
INSERT INTO TipoCertificato (Nome, Obbligatorio) VALUES
('Laurea in Medicina e Chirurgia', TRUE),
('Abilitazione Professionale',     TRUE),
('Iscrizione Ordine dei Medici',   TRUE),
('Specializzazione',               FALSE),
('Assicurazione RC Professionale', TRUE),
('Certificato Penale',             FALSE);

-- ============================================================
-- 8. CODICI SCONTO
-- ============================================================
INSERT INTO CodiceSconto (Codice, Valore_Percentuale, Data_Scadenza, Attivo) VALUES
('BENVENUTO10',  10.00, '2025-12-31', TRUE),
('ESTATE20',     20.00, '2024-09-30', FALSE),
('FEDELTA15',    15.00, '2026-06-30', TRUE),
('PROMO5',        5.00, '2025-03-31', TRUE),
('VIP25',        25.00, '2026-12-31', TRUE);

-- ============================================================
-- 9. UTENTI  (password = hash bcrypt di "Password123!")
-- ============================================================
INSERT INTO Utente (Email, Password, Data_Iscrizione, Account_attivo, Ruolo_ID) VALUES
-- Amministratori (Ruolo 1)
('admin.rossi@mediclick.it',      '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-01-10', TRUE,  1),
('admin.ferrari@mediclick.it',    '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-01-10', TRUE,  1),
-- Medici (Ruolo 2)
('dr.bianchi@mediclick.it',       '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-03-15', TRUE,  2),
('dr.verdi@mediclick.it',         '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-04-20', TRUE,  2),
('dr.esposito@mediclick.it',      '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-05-05', TRUE,  2),
('dr.romano@mediclick.it',        '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-06-01', TRUE,  2),
('dr.conti@mediclick.it',         '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-07-12', FALSE, 2),
-- Pazienti (Ruolo 3)
('luca.marini@email.it',          '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-08-01', TRUE,  3),
('giulia.ricci@email.it',         '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-08-15', TRUE,  3),
('marco.lombardi@email.it',       '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-09-03', TRUE,  3),
('sara.gallo@email.it',           '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-09-20', TRUE,  3),
('antonio.mancini@email.it',      '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-10-10', TRUE,  3),
('chiara.greco@email.it',         '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2023-11-05', TRUE,  3),
('roberto.brunetti@email.it',     '$2b$12$xLzM3QpKtVn8YrWfHjD2aO', '2024-01-08', TRUE,  3);

-- ============================================================
-- 10. AMMINISTRATORI  (ID = ID Utente corrispondente)
-- ============================================================
INSERT INTO Amministratore (ID, Dipartimento_ID) VALUES
(1, 1),  -- admin.rossi      → Amministrazione
(2, 3);  -- admin.ferrari     → Compliance

-- ============================================================
-- 11. IMPOSTAZIONI SISTEMA
-- ============================================================
INSERT INTO ImpostazioniSistema (Chiave, Valore, Data_Inizio, Data_Fine, Updated_by) VALUES
('COMMISSIONE_PIATTAFORMA_PCT', '15',         '2023-01-01', NULL,         1),
('MAX_PRENOTAZIONI_GIORNO',     '20',         '2023-01-01', NULL,         1),
('TASSE_REGIME_ORDINARIO_PCT',  '22',         '2023-01-01', NULL,         2),
('EMAIL_SUPPORTO',              'support@mediclick.it', '2023-01-01', NULL, 1),
('GIORNI_CANCELLAZIONE_FREE',   '2',          '2023-06-01', NULL,         2);

-- ============================================================
-- 12. MEDICI  (ID = ID Utente corrispondente)
-- ============================================================
INSERT INTO Medico (ID, Cognome, Nome, Bio, P_Iva, Stato_verifica, Regime_fiscale) VALUES
(3, 'Bianchi',  'Marco',    'Cardiologo con 15 anni di esperienza. Specializzato in ecocardiografia.',  '12345678901', 'Approvato',  1),
(4, 'Verdi',    'Laura',    'Dermatologa e chirurga della pelle. Esperta in dermatoscopia digitale.',    '23456789012', 'Approvato',  4),
(5, 'Esposito', 'Giovanni', 'Psicologo clinico e psicoterapeuta cognitivo-comportamentale.',             '34567890123', 'Approvato',  2),
(6, 'Romano',   'Alessia',  'Fisioterapista specializzata in riabilitazione post-chirurgica.',           '45678901234', 'In attesa',  2),
(7, 'Conti',    'Filippo',  'Medico di base. Account sospeso per verifica documentazione.',              '56789012345', 'Sospeso',    1);

-- ============================================================
-- 13. PAZIENTI  (ID = ID Utente corrispondente)
-- ============================================================
INSERT INTO Paziente (ID, Cognome, Nome, Codice_Fiscale, Telefono, Data_Nascita) VALUES
(8,  'Marini',   'Luca',     'MRNLCU85M10H501Z', '+39 333 1234567', '1985-08-10'),
(9,  'Ricci',    'Giulia',   'RCCGLI92F45F205Y', '+39 347 9876543', '1992-06-05'),
(10, 'Lombardi', 'Marco',    'LMBMRC78C22G224W', '+39 320 1112233', '1978-03-22'),
(11, 'Gallo',    'Sara',     'GLLSRA99P50A662K', '+39 366 4455667', '1999-09-10'),
(12, 'Mancini',  'Antonio',  'MNCNTN65L18F839P', '+39 338 7788990', '1965-07-18'),
(13, 'Greco',    'Chiara',   'GRCCHR01D41H224R', '+39 391 3344556', '2001-04-01'),
(14, 'Brunetti', 'Roberto',  'BRNRRT88B04L219X', '+39 349 6677889', '1988-02-04');

-- ============================================================
-- 14. STUDI MEDICI
-- ============================================================
INSERT INTO Studio (Place_ID, Nome_Sede, Indirizzo_Maps, Citta, Lat, Lng) VALUES
('ChIJN1t_tDeuEmsRUsoyG83frY4', 'Studio Medico Milano Centro',  'Via Montenapoleone 12, Milano',      'Milano',  45.4654219, 9.1859243),
('ChIJP3KeRYdMKBMRPvJkq2bP3bM', 'Poliambulatorio Roma Nord',    'Via Nomentana 45, Roma',             'Roma',    41.9143878, 12.5185429),
('ChIJLU7jZCBQKRMRr89JKJF6uCo', 'Centro Medico Torino Sud',     'Corso Unità d\'Italia 100, Torino',  'Torino',  45.0372282, 7.6698279),
('ChIJT3AxEGcMKBMRd2eTX4Y4xSo', 'Studio Salus Napoli',          'Via Toledo 220, Napoli',             'Napoli',  40.8359832, 14.2487905),
('ChIJd8BlQ2BZwokRNrHzHm9UAPU', 'Clinica Privata Firenze',      'Lungarno Corsini 8, Firenze',        'Firenze', 43.7695604, 11.2558136);

-- ============================================================
-- 15. EROGAZIONE PRESTAZIONI  (medico → prestazione → studio)
-- ============================================================
INSERT INTO ErogazionePrestazione (Medico_ID, CatalogoPrestazioni_ID, Studio_ID, Prezzo_Lordo_Listino, Durata, Stato) VALUES
-- Bianchi (Cardiologo) → Studio Milano
(3, 2,  1,  80.00, 30,  'Attiva'),   -- ECG a riposo
(3, 3,  1, 150.00, 60,  'Attiva'),   -- Ecocardiogramma
(3, 1,  1,  60.00, 30,  'Attiva'),   -- Visita generica
-- Verdi (Dermatologa) → Studio Roma
(4, 4,  2,  90.00, 30,  'Attiva'),   -- Visita Dermatologica
(4, 5,  2, 120.00, 30,  'Attiva'),   -- Dermatoscopia
-- Esposito (Psicologo) → Studio Torino
(5, 8,  3,  80.00, 60,  'Attiva'),   -- Seduta Psicologica
(5, 9,  3, 110.00, 60,  'Attiva'),   -- Test Psicodiagnostico
-- Romano (Fisioterapista) → Studio Napoli
(6, 10, 4,  70.00, 60,  'Attiva'),   -- Fisioterapia Manuale
(6, 11, 4,  55.00, 30,  'Sospesa'),  -- Tecar (sospesa temporaneamente)
-- Bianchi anche a Firenze
(3, 2,  5,  85.00, 30,  'Attiva');   -- ECG a riposo (studio secondario)

-- ============================================================
-- 16. DISPONIBILITÀ
-- ============================================================
INSERT INTO Disponibilita (Medico_ID, Studio_ID, Data_Ora_Inizio, Data_Ora_Fine, Stato, Timestamp_Blocco, Paziente_ID_Blocco) VALUES
-- Bianchi - Milano
(3, 1, '2025-05-05 09:00:00', '2025-05-05 09:30:00', 'Completata', NULL, NULL),
(3, 1, '2025-05-05 09:30:00', '2025-05-05 10:00:00', 'Completata', NULL, NULL),
(3, 1, '2025-05-05 10:00:00', '2025-05-05 10:30:00', 'Completata', NULL, NULL),
(3, 1, '2025-05-12 09:00:00', '2025-05-12 09:30:00', 'Prenotata', NULL, NULL),
(3, 1, '2025-05-12 09:30:00', '2025-05-12 10:00:00', 'Disponibile', NULL, NULL),
(3, 1, '2025-05-12 10:00:00', '2025-05-12 10:30:00', 'Disponibile', NULL, NULL),
(3, 1, '2025-05-19 09:00:00', '2025-05-19 09:30:00', 'Disponibile', NULL, NULL),
-- Verdi - Roma
(4, 2, '2025-05-06 10:00:00', '2025-05-06 10:30:00', 'Completata', NULL, NULL),
(4, 2, '2025-05-06 10:30:00', '2025-05-06 11:00:00', 'Completata', NULL, NULL),
(4, 2, '2025-05-13 10:00:00', '2025-05-13 10:30:00', 'Prenotata', NULL, NULL),
(4, 2, '2025-05-13 10:30:00', '2025-05-13 11:00:00', 'Disponibile', NULL, NULL),
(4, 2, '2025-05-20 10:00:00', '2025-05-20 10:30:00', 'Disponibile', NULL, NULL),
-- Esposito - Torino
(5, 3, '2025-05-07 14:00:00', '2025-05-07 15:00:00', 'Completata', NULL, NULL),
(5, 3, '2025-05-07 15:00:00', '2025-05-07 16:00:00', 'Completata', NULL, NULL),
(5, 3, '2025-05-14 14:00:00', '2025-05-14 15:00:00', 'Prenotata', NULL, NULL),
(5, 3, '2025-05-14 15:00:00', '2025-05-14 16:00:00', 'Disponibile', NULL, NULL),
-- Romano - Napoli
(6, 4, '2025-05-08 09:00:00', '2025-05-08 10:00:00', 'Completata', NULL, NULL),
(6, 4, '2025-05-15 09:00:00', '2025-05-15 10:00:00', 'Prenotata', NULL, NULL),
(6, 4, '2025-05-22 09:00:00', '2025-05-22 10:00:00', 'Disponibile', NULL, NULL);

-- ============================================================
-- 17. CERTIFICATI MEDICI
-- ============================================================
INSERT INTO Certificato (Medico_ID, TipoCertificato_ID, Nome_File, Stato, Mime_Type, Data_Caricamento, Data_Scadenza, Approved_by) VALUES
(3, 1, 'bianchi_laurea.pdf',        'Approvato',    'application/pdf', '2023-03-15 10:00:00', NULL,                       1),
(3, 2, 'bianchi_abilitazione.pdf',  'Approvato',    'application/pdf', '2023-03-15 10:05:00', NULL,                       1),
(3, 3, 'bianchi_ordine.pdf',        'Approvato',    'application/pdf', '2023-03-15 10:10:00', '2025-12-31 23:59:59',      1),
(3, 5, 'bianchi_rc.pdf',            'Approvato',    'application/pdf', '2023-03-15 10:15:00', '2025-06-30 23:59:59',      1),
(4, 1, 'verdi_laurea.pdf',          'Approvato',    'application/pdf', '2023-04-20 09:00:00', NULL,                       1),
(4, 2, 'verdi_abilitazione.pdf',    'Approvato',    'application/pdf', '2023-04-20 09:05:00', NULL,                       1),
(4, 3, 'verdi_ordine.pdf',          'Approvato',    'application/pdf', '2023-04-20 09:10:00', '2025-12-31 23:59:59',      1),
(4, 5, 'verdi_rc.pdf',              'Approvato',    'application/pdf', '2023-04-20 09:15:00', '2024-12-31 23:59:59',      1),
(5, 1, 'esposito_laurea.pdf',       'Approvato',    'application/pdf', '2023-05-05 11:00:00', NULL,                       2),
(5, 3, 'esposito_ordine.pdf',       'Approvato',    'application/pdf', '2023-05-05 11:10:00', '2025-12-31 23:59:59',      2),
(5, 5, 'esposito_rc.pdf',           'Approvato',    'application/pdf', '2023-05-05 11:15:00', '2025-09-30 23:59:59',      2),
(6, 1, 'romano_laurea.pdf',         'In revisione', 'application/pdf', '2023-06-01 08:00:00', NULL,                       NULL),
(6, 2, 'romano_abilitazione.pdf',   'In revisione', 'application/pdf', '2023-06-01 08:05:00', NULL,                       NULL),
(7, 1, 'conti_laurea.pdf',          'Rifiutato',    'application/pdf', '2023-07-12 14:00:00', NULL,                       1),
(7, 3, 'conti_ordine_scaduto.pdf',  'Scaduto',      'application/pdf', '2023-07-12 14:05:00', '2023-12-31 23:59:59',      1);

-- ============================================================
-- 18. PRENOTAZIONI
-- ============================================================
INSERT INTO Prenotazione (
    Paziente_ID, Disponibilita_ID, ErogazionePrestazione_ID, CodiceSconto_ID,
    Stato, Metodo_Pagamento, ID_Transazione_Esterno,
    Importo_Pagato, Ricavo_Netto_Medico_Euro, Trattenuta_Piattaforma_Euro, Tasse_Stimate_Euro,
    Data_Pagamento
) VALUES
-- Prenotazioni completate (slot 1-3, 8-9, 13-14, 17)
(8,  1, 1, NULL,  'Completata', 'Carta di credito', 'TXN-001-2025', 80.00, 60.00, 12.00,  8.00, '2025-05-05 09:00:00'),
(9,  2, 3, 1,     'Completata', 'PayPal',           'TXN-002-2025', 54.00, 40.50, 10.80,  2.70, '2025-05-05 09:30:00'), -- sconto 10% BENVENUTO10
(10, 3, 2, NULL,  'Completata', 'Carta di credito', 'TXN-003-2025',150.00,112.50, 22.50, 15.00, '2025-05-05 10:00:00'),
(11, 8, 4, NULL,  'Completata', 'Bonifico',         'TXN-004-2025', 90.00, 67.50, 13.50,  9.00, '2025-05-06 10:00:00'),
(12, 9, 5, 3,     'Completata', 'Carta di credito', 'TXN-005-2025',102.00, 76.50, 15.30, 10.20, '2025-05-06 10:30:00'), -- sconto 15% FEDELTA15
(8,  13,6, NULL,  'Completata', 'Carta di credito', 'TXN-006-2025', 80.00, 60.00, 12.00,  8.00, '2025-05-07 14:00:00'),
(13, 14,7, NULL,  'Completata', 'PayPal',           'TXN-007-2025',110.00, 82.50, 16.50, 11.00, '2025-05-07 15:00:00'),
(14, 17,8, NULL,  'Completata', 'Contanti',         NULL,           70.00, 52.50, 10.50,  7.00, '2025-05-08 09:00:00'),
-- Prenotazioni confermate (slot 4, 10, 15, 18)
(9,  4,  1, NULL, 'Confermata', 'Carta di credito', 'TXN-008-2025', 80.00, 60.00, 12.00,  8.00, '2025-05-10 08:00:00'),
(10, 10, 4, NULL, 'Confermata', 'PayPal',           'TXN-009-2025', 90.00, 67.50, 13.50,  9.00, '2025-05-11 09:00:00'),
(11, 15, 6, 5,    'Confermata', 'Carta di credito', 'TXN-010-2025', 60.00, 45.00,  9.00,  6.00, '2025-05-12 10:00:00'), -- sconto 25% VIP25
(12, 18, 8, NULL, 'Confermata', 'Bonifico',         'TXN-011-2025', 70.00, 52.50, 10.50,  7.00, '2025-05-13 11:00:00'),
-- Prenotazione cancellata e rimborsata
(13, 1,  1, NULL, 'Cancellata', NULL,                NULL,            0.00,  0.00,  0.00,  0.00, NULL),
(14, 9,  5, NULL, 'Rimborsata', 'Carta di credito', 'TXN-012-2025',120.00, 90.00, 18.00, 12.00, '2025-05-03 12:00:00');

-- ============================================================
-- 19. RECENSIONI  (solo per prenotazioni Completate)
-- ============================================================
INSERT INTO Recensione (Prenotazione_ID, Voto, Commento, Is_visible, Data_Pubblicazione) VALUES
(1, 5, 'Dottore molto professionale, spiegazioni chiare e precise. Consiglio vivamente.',        TRUE,  '2025-05-06 10:00:00'),
(2, 4, 'Visita veloce e precisa. Studio un po\' piccolo ma medico competente.',                  TRUE,  '2025-05-06 11:00:00'),
(3, 5, 'Ecocardiogramma eseguito benissimo, referto dettagliato. Tornerò sicuramente.',          TRUE,  '2025-05-07 09:00:00'),
(4, 4, 'Dermatologa preparata. Ha individuato subito il problema. Solo l\'attesa un po\' lunga.', TRUE, '2025-05-07 12:00:00'),
(5, 3, 'Visita nella norma, nulla di eccezionale. Prezzi nella media.',                          TRUE,  '2025-05-07 14:00:00'),
(6, 5, 'Psicologo eccellente, mi ha messo subito a mio agio. Studio accogliente.',              TRUE,  '2025-05-08 08:00:00'),
(7, 5, 'Seduta molto utile. Approccio empatico e professionale.',                               FALSE, '2025-05-08 09:00:00'), -- nascosta (moderazione)
(8, 4, 'Fisioterapista brava, ho già sentito miglioramenti dopo la prima seduta.',              TRUE,  '2025-05-09 10:00:00');


