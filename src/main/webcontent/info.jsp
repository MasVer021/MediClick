<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main>
	<div class="mc-grid">
		<section id="come-funziona" class="mc-card">
			<h2>Per i Pazienti</h2>
			<h3>Come funziona MediClick?</h3>
			<p>MediClick è la piattaforma ideale per trovare il medico specialista che fa per te e prenotare una visita in pochi click. Cerca la specializzazione, inserisci la città, confronta i profili, le recensioni e prenota l'orario più comodo per le tue esigenze, direttamente online.</p>
			<h3>Cerca un medico o specializzazioni</h3>
			<p>
				Puoi cercare medici per nome, cognome oppure consultare tutte le nostre specializzazioni dalla barra di ricerca cliccando su <a href="<c:url value='/search'/>">Cerca uno Specialista</a>.
			</p>
		</section>
		<section id="sei-un-medico" class="mc-card">
			<h2>Per i Medici</h2>
			<h3>Sei un medico? Scopri i vantaggi</h3>
			<p>Unirti a MediClick ti permette di aumentare la tua visibilità, gestire le tue prenotazioni tramite un'agenda digitale intelligente e migliorare la comunicazione con i tuoi pazienti grazie alle recensioni verificate.</p>
			<h3 id="accreditamento">Come funziona l'Accreditamento?</h3>
			<p>L'accreditamento è semplice e veloce. Clicca sulla voce di menu per la registrazione medici e compila il form. Il nostro team verificherà le tue credenziali e l'iscrizione all'albo prima di attivare il profilo sulla piattaforma.</p>
			<h3 id="centro-assistenza">Centro assistenza medici</h3>
			<p>
				Per qualsiasi problema tecnico o nella gestione delle agende, il nostro team dedicato è a tua disposizione all'indirizzo email <a href="mailto:supporto-medici@mediclick.it">supporto-medici@mediclick.it</a>.
			</p>
		</section>
		<section id="policy" class="mc-card">
			<h2>Privacy, Policy e Condizioni</h2>
			<h3 id="privacy">Privacy Policy & Cookie</h3>
			<p>La tua privacy è fondamentale per noi. Raccogliamo i tuoi dati unicamente per garantirti il servizio di prenotazione e li trattiamo nel massimo rispetto della sicurezza. Usiamo solo cookie tecnici.</p>
			<h3 id="termini">Termini e condizioni</h3>
			<p>L'uso di MediClick implica l'accettazione dei nostri termini di servizio. La piattaforma funge da intermediario tecnologico tra il paziente e il professionista per agevolare la prenotazione. La prestazione medica resta di esclusiva competenza del medico curante.</p>
			<h3 id="contattaci">Contattaci</h3>
			<p>
				Hai ulteriori dubbi o necessiti di supporto generale? Non esitare a scriverci al nostro indirizzo: <a href="mailto:supporto@mediclick.it">supporto@mediclick.it</a>
			</p>
		</section>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />
