<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <title>MediClick</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
<nav id="main-menu">
	<div class="nav-left">
	    <a href="${pageContext.request.contextPath}/"><img src = "${pageContext.request.contextPath}/img/ColFullLogo.svg" alt="Logo completo colorato"></a>
	</div>
	
	<div class="nav-center">
    <!-- Utente NON loggato -->
	    <c:if test="${empty sessionScope.utenteLoggato}">
	        <a href="${pageContext.request.contextPath}/login">Home</a>
	        <a href="${pageContext.request.contextPath}/registrazione">Medico</a>
	    </c:if>
	
	    <!-- PAZIENTE -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'PAZIENTE'}">
	        <a href="${pageContext.request.contextPath}/paziente/profilo">Home</a>
	        <a href="${pageContext.request.contextPath}/ricerca">Prenotazioni</a>
	        <a href="${pageContext.request.contextPath}/ricerca">Sconti</a>
	        <a href="${pageContext.request.contextPath}/logout">Medico</a>
	    </c:if>
	
	    <!-- MEDICO -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'MEDICO'}">
	        <a href="${pageContext.request.contextPath}/medico/agenda">Home</a>
	        <a href="${pageContext.request.contextPath}/medico/disponibilita">Documenti</a>
	        <a href="${pageContext.request.contextPath}/medico/disponibilita">Prenotazioni</a>
	        <a href="${pageContext.request.contextPath}/medico/disponibilita">Disponibilità</a>
	        <a href="${pageContext.request.contextPath}/medico/disponibilita">Contabilità</a>
	    </c:if>
	
	    <!-- ADMIN -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'ADMIN'}">
	        <a href="${pageContext.request.contextPath}/admin/dashboard">Home</a>
	        <a href="${pageContext.request.contextPath}/admin/medici">Contabilità</a>
	        <a href="${pageContext.request.contextPath}/admin/catalogo">Approvazione</a>
	        <a href="${pageContext.request.contextPath}/logout">Impostazioni</a>
	    </c:if>
    </div>
    
    <div class="nav-right">
	    <c:if test="${empty sessionScope.utenteLoggato}">
	        <a href="${pageContext.request.contextPath}/login">Accedi</a>
	        <a href="${pageContext.request.contextPath}/singin">Registrati</a>
	    </c:if>
	
	    <!-- PAZIENTE -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'PAZIENTE'}">
	        <a href="${pageContext.request.contextPath}/paziente/profilo">Il mio profilo</a>
	        <a href="${pageContext.request.contextPath}/logout">Esci</a>
	    </c:if>
	
	    <!-- MEDICO -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'MEDICO'}">
	        <a href="${pageContext.request.contextPath}/paziente/profilo">Il mio profilo</a>
	        <a href="${pageContext.request.contextPath}/logout">Esci</a>
	    </c:if>
	
	    <!-- ADMIN -->
	    <c:if test="${sessionScope.utenteLoggato.ruolo.codice == 'ADMIN'}">
	        <a href="${pageContext.request.contextPath}/paziente/profilo">Il mio profilo</a>
	        <a href="${pageContext.request.contextPath}/logout">Esci</a>
	    </c:if>
    </div>
    
    
    
    
</nav>

<!-- Messaggi di feedback globali -->
<c:if test="${not empty param.msg}">
    <div class="alert">${param.msg}</div>
</c:if>
<c:if test="${not empty requestScope.errore}">
    <div class="alert alert-error">${requestScope.errore}</div>
</c:if>
