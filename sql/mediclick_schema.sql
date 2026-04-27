-- ============================================================
-- MediClick - Schema SQL
-- Generato dall'analisi del diagramma ER
-- ============================================================

-- ============================================================
-- TABELLE BASE (senza dipendenze esterne)
-- ============================================================
Drop DATABASE MediClick;
Create DAtabase MediClick;

USE MediClick;

CREATE TABLE Permesso (
    ID          INT             NOT NULL AUTO_INCREMENT,
    Codice      VARCHAR(50)     NOT NULL UNIQUE,
    Descrizione VARCHAR(255),

    PRIMARY KEY (ID)
);

CREATE TABLE Ruolo (
    ID          INT             NOT NULL AUTO_INCREMENT,
    Codice      VARCHAR(50)     NOT NULL UNIQUE,
    Descrizione VARCHAR(255),

    PRIMARY KEY (ID)
);

-- Relazione N:N tra Ruolo e Permesso
CREATE TABLE Caratterizzato (
    Ruolo_ID    INT NOT NULL,
    Permesso_ID INT NOT NULL,

    PRIMARY KEY (Ruolo_ID, Permesso_ID),
    CONSTRAINT fk_carat_ruolo    FOREIGN KEY (Ruolo_ID)    REFERENCES Ruolo(ID)    ON DELETE CASCADE,
    CONSTRAINT fk_carat_permesso FOREIGN KEY (Permesso_ID) REFERENCES Permesso(ID) ON DELETE CASCADE
);

CREATE TABLE Utente (
    ID              INT             NOT NULL AUTO_INCREMENT,
    Email           VARCHAR(150)    NOT NULL UNIQUE,
    Password        VARCHAR(255)    NOT NULL,
    Data_Iscrizione DATE,
    Account_attivo  BOOLEAN         NOT NULL DEFAULT TRUE,
    Ruolo_ID        INT,


    PRIMARY KEY (ID),
    CONSTRAINT fk_utente_ruolo FOREIGN KEY (Ruolo_ID) REFERENCES Ruolo(ID) ON DELETE SET NULL
);

CREATE TABLE Dipartimento (
    ID     INT          NOT NULL AUTO_INCREMENT,
    Nome   VARCHAR(100) NOT NULL,

    PRIMARY KEY (ID)
);



CREATE TABLE TipoCertificato (
    ID           INT          NOT NULL AUTO_INCREMENT,
    Nome         VARCHAR(100) NOT NULL,
    Obbligatorio BOOLEAN      NOT NULL DEFAULT FALSE,

    PRIMARY KEY (ID)
);

CREATE TABLE Categoria (
    ID   INT          NOT NULL AUTO_INCREMENT,
    Nome VARCHAR(100) NOT NULL,

    PRIMARY KEY (ID)
);

CREATE TABLE CatalogoPrestazioni (
    ID          INT          NOT NULL AUTO_INCREMENT,
    Nome        VARCHAR(150) NOT NULL,
    Stato       ENUM('Attiva', 'Disattiva')  NOT NULL DEFAULT 'Attiva',
    Descrizione TEXT,
    Categoria_ID INT,

    PRIMARY KEY (ID),
    CONSTRAINT fk_catalogo_categoria FOREIGN KEY (Categoria_ID) REFERENCES Categoria(ID) ON DELETE SET NULL
);

CREATE TABLE Regime_Fiscale (
    ID              INT             NOT NULL AUTO_INCREMENT,
    Nome            VARCHAR(100)    NOT NULL,
    Aliquota_Default DECIMAL(5,2),
    Descrizione     TEXT,

    PRIMARY KEY (ID)
);

-- ============================================================
-- UTENTI SPECIALIZZATI
-- ============================================================

CREATE TABLE Amministratore (
    ID           INT NOT NULL,
    Dipartimento_ID INT,

    PRIMARY KEY (ID),
    CONSTRAINT fk_amm_utente      FOREIGN KEY (ID)       REFERENCES Utente(ID)      ON DELETE RESTRICT,
    CONSTRAINT fk_amm_dipartimento FOREIGN KEY (Dipartimento_ID) REFERENCES Dipartimento(ID) ON DELETE SET NULL
);

CREATE TABLE Medico (
    ID          INT             NOT NULL,
    Cognome     VARCHAR(100)    NOT NULL,
    Nome        VARCHAR(100)    NOT NULL,
    Bio         TEXT,
    P_Iva       VARCHAR(20),
    Stato_verifica ENUM('In attesa', 'Approvato', 'Rifiutato', 'Sospeso') NOT NULL DEFAULT 'In attesa',
    Regime_fiscale int,

    PRIMARY KEY (ID),
    CONSTRAINT fk_medico_utente FOREIGN KEY (ID) REFERENCES Utente(ID) ON DELETE RESTRICT,
    CONSTRAINT fk_medico_regime_fiscale FOREIGN KEY (Regime_fiscale) REFERENCES Regime_Fiscale(ID) ON DELETE RESTRICT
);

CREATE TABLE Paziente (
    ID              INT             NOT NULL,
    Cognome         VARCHAR(100)    NOT NULL,
    Nome            VARCHAR(100)    NOT NULL,
    Codice_Fiscale  VARCHAR(16)     UNIQUE,
    Telefono        VARCHAR(20),
    Data_Nascita    DATE,

    PRIMARY KEY (ID),
    CONSTRAINT fk_paziente_utente        FOREIGN KEY (ID)        REFERENCES Utente(ID)       ON DELETE RESTRICT

);


-- ============================================================
-- STUDIO, DISPONIBILITÀ E PRESTAZIONI
-- ============================================================

CREATE TABLE Studio (
    ID              INT             NOT NULL AUTO_INCREMENT,
    Place_ID        VARCHAR(100),
    Nome_Sede       VARCHAR(150),
    Indirizzo_Maps  VARCHAR(255),
    Citta           VARCHAR(100),
    Lat             DECIMAL(10,7),
    Lng             DECIMAL(10,7),

    PRIMARY KEY (ID)
);


CREATE TABLE Disponibilita (
    ID               INT             NOT NULL AUTO_INCREMENT,
    Medico_ID        INT             NOT NULL,
    Studio_ID        INT,
    Data_Ora_Inizio  DATETIME        NOT NULL,
    Data_Ora_Fine    DATETIME        NOT NULL,
    Stato            ENUM('Disponibile', 'Prenotata', 'Bloccata', 'Completata', 'Cancellata') NOT NULL DEFAULT 'Disponibile',
    Timestamp_Blocco DATETIME,
    Paziente_ID_Blocco INT,

    PRIMARY KEY (ID),
    CONSTRAINT fk_disp_medico FOREIGN KEY (Medico_ID) REFERENCES Medico(ID)  ON DELETE CASCADE,
    CONSTRAINT fk_disp_studio FOREIGN KEY (Studio_ID) REFERENCES Studio(ID)  ON DELETE CASCADE,
    CONSTRAINT fk_disp_paziente_blocco FOREIGN KEY (Paziente_ID_Blocco) REFERENCES Paziente(ID) ON DELETE SET NULL
);

CREATE TABLE ErogazionePrestazione (
    ID                  INT             NOT NULL AUTO_INCREMENT,
    Medico_ID           INT             NOT NULL,
    CatalogoPrestazioni_ID INT          NOT NULL,
    Studio_ID           INT,
    Prezzo_Lordo_Listino DECIMAL(10,2),
    Durata              INT             COMMENT 'Durata in minuti approssimato alla mezzo ora  successiva es 31 min 60minm',
    Stato               ENUM('Attiva', 'Sospesa') NOT NULL DEFAULT 'Attiva',
    PRIMARY KEY (ID),
    CONSTRAINT fk_erp_medico    FOREIGN KEY (Medico_ID)             REFERENCES Medico(ID)             ON DELETE CASCADE,
    CONSTRAINT fk_erp_catalogo  FOREIGN KEY (CatalogoPrestazioni_ID) REFERENCES CatalogoPrestazioni(ID) ON DELETE CASCADE,
    CONSTRAINT fk_erp_studio    FOREIGN KEY (Studio_ID)             REFERENCES Studio(ID)             ON DELETE SET NULL
);

-- ============================================================
-- CERTIFICATI
-- ============================================================
CREATE TABLE ImpostazioniSistema (
    ID           INT          NOT NULL AUTO_INCREMENT,
    Chiave       VARCHAR(100) NOT NULL UNIQUE,
    Valore       VARCHAR(100),
    Data_Inizio  DATE,
    Data_Fine    DATE,
    Updated_by   INT        NOT NULL,
    PRIMARY KEY (ID),
    CONSTRAINT fk_imp_sis_amm FOREIGN KEY (Updated_by) REFERENCES Amministratore(ID) ON DELETE RESTRICT
);


CREATE TABLE Certificato (
    ID               INT          NOT NULL AUTO_INCREMENT,
    Medico_ID        INT          NOT NULL,
    TipoCertificato_ID INT        NOT NULL,
    Nome_File        VARCHAR(255),
    Dati_Documento   BLOB,
    Stato            ENUM('In revisione', 'Approvato', 'Rifiutato', 'Scaduto') NOT NULL DEFAULT 'In revisione',
    Mime_Type        VARCHAR(100),
    Data_Caricamento DATETIME     DEFAULT CURRENT_TIMESTAMP,
    Data_Scadenza   DATETIME,
    Approved_by      INT,


    PRIMARY KEY (ID),
    CONSTRAINT fk_cert_medico FOREIGN KEY (Medico_ID)         REFERENCES Medico(ID)          ON DELETE CASCADE,
    CONSTRAINT fk_cert_tipo   FOREIGN KEY (TipoCertificato_ID) REFERENCES TipoCertificato(ID) ON DELETE RESTRICT,
    CONSTRAINT fk_cert_amm   FOREIGN KEY (Approved_by) REFERENCES Amministratore(ID) ON DELETE RESTRICT
);

-- ============================================================
-- PRENOTAZIONI E PAGAMENTI
-- ============================================================

CREATE TABLE CodiceSconto (
    ID               INT             NOT NULL AUTO_INCREMENT,
    Codice           VARCHAR(50)     NOT NULL UNIQUE,
    Valore_Percentuale DECIMAL(5,2)  NOT NULL,
    Data_Scadenza    DATE,
    Attivo           BOOLEAN         NOT NULL DEFAULT TRUE,

    PRIMARY KEY (ID)
);

CREATE TABLE Prenotazione (
    ID                      INT             NOT NULL AUTO_INCREMENT,
    Paziente_ID             INT             NOT NULL,
    Disponibilita_ID        INT             NOT NULL,
    ErogazionePrestazione_ID INT,
    CodiceSconto_ID         INT,
    Stato                   ENUM('Confermata', 'Completata', 'Cancellata', 'Rimborsata') NOT NULL DEFAULT 'Confermata',
    Metodo_Pagamento        VARCHAR(255),
    ID_Transazione_Esterno  VARCHAR(255),
    Importo_Pagato          DECIMAL(10,2),
    Ricavo_Netto_Medico_Euro DECIMAL(10,2),
    Trattenuta_Piattaforma_Euro DECIMAL(10,2),
    Tasse_Stimate_Euro      DECIMAL(10,2),
    Data_Pagamento          DATETIME   DEFAULT CURRENT_TIMESTAMP,


    PRIMARY KEY (ID),
    CONSTRAINT fk_pren_paziente   FOREIGN KEY (Paziente_ID)             REFERENCES Paziente(ID)             ON DELETE CASCADE,
    CONSTRAINT fk_pren_disp       FOREIGN KEY (Disponibilita_ID)        REFERENCES Disponibilita(ID)        ON DELETE RESTRICT,
    CONSTRAINT fk_pren_erp        FOREIGN KEY (ErogazionePrestazione_ID) REFERENCES ErogazionePrestazione(ID) ON DELETE SET NULL,
    CONSTRAINT fk_pren_sconto     FOREIGN KEY (CodiceSconto_ID)         REFERENCES CodiceSconto(ID)         ON DELETE SET NULL
);

-- ============================================================
-- RECENSIONI
-- ============================================================

CREATE TABLE Recensione (
    ID                  INT             NOT NULL AUTO_INCREMENT,
    Prenotazione_ID     INT             NOT NULL UNIQUE,
    Voto                TINYINT         NOT NULL CHECK (Voto BETWEEN 1 AND 5),
    Commento            TEXT,
    is_visible               BOOLEAN     NOT NULL DEFAULT TRUE,
    Data_Pubblicazione  DATETIME        DEFAULT CURRENT_TIMESTAMP,


    PRIMARY KEY (ID),
    CONSTRAINT fk_recen_prenotazione FOREIGN KEY (Prenotazione_ID) REFERENCES Prenotazione(ID) ON DELETE CASCADE
);

-- ============================================================
-- INDICI UTILI PER LE QUERY PIÙ COMUNI
-- ============================================================

CREATE INDEX idx_utente_email         ON Utente(Email);
CREATE INDEX idx_medico_stato_verifica ON Medico(Stato_verifica);
CREATE INDEX idx_paziente_cf           ON Paziente(Codice_Fiscale);
CREATE INDEX idx_disp_medico_data      ON Disponibilita(Medico_ID, Data_Ora_Inizio);
CREATE INDEX idx_disp_stato            ON Disponibilita(Stato);
CREATE INDEX idx_pren_paziente         ON Prenotazione(Paziente_ID);
CREATE INDEX idx_pren_stato            ON Prenotazione(Stato);
CREATE INDEX idx_erp_medico            ON ErogazionePrestazione(Medico_ID);
CREATE INDEX idx_erp_catalogo          ON ErogazionePrestazione(CatalogoPrestazioni_ID);
CREATE INDEX idx_recen_prenotazione    ON Recensione(Prenotazione_ID);
CREATE INDEX idx_catalogo_categoria    ON CatalogoPrestazioni(Categoria_ID);
CREATE INDEX idx_cert_medico           ON Certificato(Medico_ID);
