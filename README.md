# MediClick 🩺

**MediClick** è una piattaforma web innovativa progettata per semplificare l'incontro tra medici specialisti e pazienti. Con il motto *"La tua salute in un click"*, l'applicazione permette agli utenti di cercare professionisti, confrontare le recensioni e prenotare visite mediche in modo rapido e sicuro.

## 🌟 Funzionalità Principali

### Per i Pazienti 🧑‍⚕️
- **Ricerca Intelligente**: Trova lo specialista perfetto filtrando per categoria medica, nome o città.
- **Consultazione Profili**: Visualizza informazioni dettagliate sui medici, costi delle prestazioni e orari delle prime disponibilità.
- **Sistema di Recensioni**: Leggi le valutazioni degli altri pazienti per fare una scelta informata.
- **Prenotazione Rapida**: Prenota la tua visita direttamente online con pochissimi click.

### Per i Medici 👨‍⚕️
- **Profilo Professionale**: Crea e gestisci il tuo profilo con foto personalizzata e biografia.
- **Gestione Agenda**: Ottimizza il tuo tempo gestendo gli appuntamenti in modo digitale.
- **Accreditamento Sicuro**: Processo di verifica dedicato per garantire ai pazienti solo professionisti certificati e iscritti all'albo.

## 🛠️ Tecnologie Utilizzate

Il progetto è sviluppato seguendo un'architettura **MVC (Model-View-Controller)** robusta e strutturata a livelli.

*   **Frontend**: HTML5, CSS3 vanilla, JSP (JavaServer Pages), JSTL. Design responsivo e UI moderna.
*   **Backend**: Java (Servlets). Architettura a strati: Controller (Servlet), Business Logic (Service Layer) e Data Access Object (DAO Layer).
*   **Database**: MySQL.

## 📂 Struttura del Progetto

```text
MediClick/
├── doc/                        # Documentazione del progetto (Proposal, File di Architettura)
├── sql/                        # Script Database
│   ├── mediclick_schema.sql    # DDL per la creazione delle tabelle
│   └── mediclick_seed.sql      # DML per il popolamento iniziale del DB (Mock data)
└── src/
    └── main/
        ├── java/it/mediclick/  # Codice sorgente Java (Bean, DAO, Service, utilità)
        └── webcontent/         # File statici e viste JSP
            ├── css/            # Fogli di stile (style.css)
            ├── img/            # Risorse grafiche
            ├── WEB-INF/view/   # Layout protetti (Header, Footer)
            └── *.jsp           # Pagine pubbliche
```

## 🚀 Setup e Installazione

1. **Clona il repository**:
   ```bash
   git clone https://github.com/TuoUsername/MediClick.git
   ```

2. **Configura il Database**:
   - Assicurati di avere un server MySQL locale in esecuzione.
   - Crea un database dedicato.
   - Importa prima lo schema eseguendo lo script `sql/mediclick_schema.sql`.
   - Inserisci i dati di test con lo script `sql/mediclick_seed.sql`.
   - Verifica la classe di connessione (es. `Contex.java`) per assicurarti che username e password del DB combacino con le tue credenziali locali.

3. **Esecuzione dell'Applicazione**:
   - Importa il progetto nel tuo IDE preferito (es. Eclipse IDE for Enterprise Java Developers).
   - Configura un Application Server come **Apache Tomcat**.
   - Avvia il server sul progetto.
   - Accedi all'applicazione dal browser (solitamente `http://localhost:8080/MediClick`).

## 🤝 Contributi
Questo progetto è stato sviluppato in ambito accademico (Università degli Studi di Salerno). Eventuali pull request, suggerimenti e segnalazioni di bug sono benvenuti!

## 📄 Licenza
Distribuito sotto licenza MIT.
