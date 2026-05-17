<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div class="dati-prenotazione">
	    <h3>Recensione per la visita del ${prenotazione.disponibilita.dataOraInizio}</h3>
	    <p>Medico: Dott. ${prenotazione.erogazionePrestazione.medico.cognome}</p>
	    <p>Prestazione: ${prenotazione.erogazionePrestazione.catalogoPrestazioni.nome}</p>
	</div>
	
	<c:if test="${not empty recensione}">
	    <div class="alert alert-success">
	        <h4>Hai già lasciato una recensione!</h4>
	        <p>Voto: ${recensione.voto} / 5</p>
	        <p>Commento: ${recensione.commento}</p>
	    </div>
	    <a href="${pageContext.request.contextPath}/paziente/prenotazioni">Torna alle prenotazioni</a>
	</c:if>
	
	<c:if test="${empty recensione}">
	    <form action="${pageContext.request.contextPath}/paziente/recensione" method="post">
	        <input type="hidden" name="prenotazioneId" value="${prenotazione.id}">
	        <textarea name="commento" rows="5" cols="50"></textarea>
	        <input type="number" name="voto" min="1" max="5">
	        <button type="submit">Salva</button>
	    </form>
	</c:if>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>