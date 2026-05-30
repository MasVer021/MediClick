<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1" />    
    <title>MediClick</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
	<nav id="mc-main-menu">
		<div class="mc-main-menu__left">
		    <a href="${pageContext.request.contextPath}/"><img src = "${pageContext.request.contextPath}/img/ColFullLogo.svg" alt="Logo completo colorato"></a>
		</div>
		
		<div class="mc-main-menu__center">
		    <c:if test="${sessionScope.utente.ruolo.codice == 'PAZIENTE'}">
		        <a href="${pageContext.request.contextPath}/">Home</a>
		        <a href="${pageContext.request.contextPath}/paziente/prenotazioni">Prenotazioni</a>
		        <a href="${pageContext.request.contextPath}/search">Medico</a>
		    </c:if>
		
		   
		    <c:if test="${sessionScope.utente.ruolo.codice == 'MEDICO'}">
		        <a href="${pageContext.request.contextPath}/medico/agenda">Home</a>
		        <a href="${pageContext.request.contextPath}/medico/certificati">Documenti</a>
		        <a href="${pageContext.request.contextPath}/medico/disponibilita">Disponibilità</a>
		        <a href="${pageContext.request.contextPath}/medico/dashboard">Contabilità</a>
		    </c:if>
		
		    
		    <c:if test="${sessionScope.utente.ruolo.codice == 'ADMIN'}">
		        <a href="${pageContext.request.contextPath}/admin/dashboard">Home</a>
		        <a href="${pageContext.request.contextPath}/admin/catalogo">Catalogo prestazioni</a>
		        <a href="${pageContext.request.contextPath}/admin/medici">Medici</a>
		        <a href="${pageContext.request.contextPath}/admin/impostazioni">Impostazioni</a>
		    </c:if>
	    </div>
	    
	    <div class="mc-main-menu__right">
		    <c:if test="${empty sessionScope.utente}">
		        <a href="${pageContext.request.contextPath}/login">Accedi</a>
		        <a href="${pageContext.request.contextPath}/singin">Registrati</a>
		    </c:if>
		
		    
		    <c:if test="${sessionScope.utente.ruolo.codice == 'PAZIENTE'}">
		        <a href="${pageContext.request.contextPath}/paziente/profilo">Il mio profilo</a>
		        <a href="${pageContext.request.contextPath}/logout">Esci</a>
		    </c:if>
		
		    
		    <c:if test="${sessionScope.utente.ruolo.codice == 'MEDICO'}">
		        <a href="${pageContext.request.contextPath}/medico/profilo">Il mio profilo</a>
		        <a href="${pageContext.request.contextPath}/logout">Esci</a>
		    </c:if>
		
		   
		    <c:if test="${sessionScope.utente.ruolo.codice == 'ADMIN'}">
		        <a href="${pageContext.request.contextPath}/logout">Esci</a>
		    </c:if>
	    </div>
	</nav>

 	

<c:if test="${not empty errore}">
   <div class="mc-message mc-message--error">
 		<p class="mc-message__code">
 		${errore.codiceErrore}
 		</p>
 		<p class="mc-message__message">
 		${errore.messaggio}
 		</p>
 	</div>
</c:if>
