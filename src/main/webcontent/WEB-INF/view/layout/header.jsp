<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="it">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>MediClick</title>
<link rel="stylesheet" href="<c:url value="/css/style.css" />">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
	<nav id="mc-main-menu">
		<div class="mc-main-menu__left">
			<a href="<c:url value="/" />"><img src="<c:url value="/img/ColFullLogo.svg" />" alt="Logo completo colorato"></a>
		</div>
		<button id="hamburger-menu" class="mc-menu-toggle" aria-label="Apri menu">
			<span></span> 
			<span></span>
			 <span></span>
		</button>
		<div class="mc-main-menu__links" id="menuLinks">
			<div class="mc-main-menu__center">
				<c:if test="${sessionScope.utente.ruolo.codice == 'PAZIENTE'}">
					<a href="<c:url value='/' />">Home</a>
					<a href="<c:url value='/paziente/prenotazioni' />">Prenotazioni</a>
					<a href="<c:url value='/search' />">Medico</a>
				</c:if>
				<c:if test="${sessionScope.utente.ruolo.codice == 'MEDICO'}">
					<a href="<c:url value='/medico/agenda' />">Home</a>
					<a href="<c:url value='/medico/certificati' />">Documenti</a>
					<a href="<c:url value='/medico/disponibilita' />">Disponibilità</a>
					<a href="<c:url value='/medico/dashboard' />">Contabilità</a>
				</c:if>
				<c:if test="${sessionScope.utente.ruolo.codice == 'ADMIN'}">
					<a href="<c:url value='/admin/dashboard' />">Home</a>
					<a href="<c:url value='/admin/catalogo' />">Catalogo prestazioni</a>
					<a href="<c:url value='/admin/prenotazioni' />">Prenotazioni</a>
					<a href="<c:url value='/admin/medici' />">Medici</a>
					<a href="<c:url value='/admin/impostazioni'/>">Impostazioni</a>
				</c:if>
			</div>
			<div class="mc-main-menu__right">
				<c:if test="${empty sessionScope.utente}">
					<a href="<c:url value='/login' />">Accedi</a>
					<a href="<c:url value='/singin' />">Registrati</a>
				</c:if>
				<c:if test="${sessionScope.utente.ruolo.codice == 'PAZIENTE'}">
					<a href="<c:url value='/paziente/profilo' />">Il mio profilo</a>
					<a href="<c:url value='/logout' />">Esci</a>
				</c:if>
				<c:if test="${sessionScope.utente.ruolo.codice == 'MEDICO'}">
					<a href="<c:url value='/medico/profilo' />">Il mio profilo</a>
					<a href="<c:url value='/logout' />">Esci</a>
				</c:if>
				<c:if test="${sessionScope.utente.ruolo.codice == 'ADMIN'}">
					<a href="<c:url value='/logout' />">Esci</a>
				</c:if>
			</div>
		</div>
	</nav>
	<c:if test="${not empty errore}">
		<div class="mc-message mc-message--error">
			<p class="mc-message__code">${errore.codiceErrore}</p>
			<p class="mc-message__message">${errore.messaggio}</p>
		</div>
		<c:remove var="errore" scope="session" />
	</c:if>
	<script src="<c:url value='/js/toggle-menu.js'/>" defer></script>