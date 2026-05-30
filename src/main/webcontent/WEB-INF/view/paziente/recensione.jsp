<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main class="mc-container">
	<div class="mc-card mc-card--generic ">
		<div class="mc-card__header ">
		 	<h3 class="mc-card__title">Recensione per la visita del ${prenotazione.disponibilita.dataOraInizio}</h3>
		</div>
		<div class="mc-card__body mc-flex-col mc-gap-md">
			<div class="mc-flex-col">
	            <span class="mc-text-muted mc-font-xs">Medico</span>
	            <span class="mc-text-bold mc-font-md">Dott. ${prenotazione.erogazionePrestazione.medico.cognome}</span>
	        </div>
	        <div class="mc-flex-col">
	            <span class="mc-text-muted mc-font-xs">Prestazione</span>
	            <span class="mc-text-bold mc-font-md">${prenotazione.erogazionePrestazione.catalogoPrestazioni.nome}</span>
	        </div>
	        
		
		
			<c:if test="${not empty recensione}">	        
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Commento</span>
		            <span class="mc-text-bold mc-font-md">${recensione.commento}</span>
		        </div>
		         <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Voto</span>
		            <span class="mc-text-bold mc-font-md">${recensione.voto}</span>
		        </div>
			  
			    <a class="mc-btn mc-btn--outline mc-btn--sm" href="${pageContext.request.contextPath}/paziente/prenotazioni">Torna alle prenotazioni</a>
			</c:if>
			
			<c:if test="${empty recensione}">
			    <form action="${pageContext.request.contextPath}/paziente/recensione" method="post">
			        <input type="hidden" name="prenotazioneId" value="${prenotazione.id}">
			        
			         <div class="mc-form-group">
	                    <label class="mc-label" for="commento">Commento</label>
	                    <textarea class="mc-textarea" id="commento" name="commento" rows="4" placeholder="Parlaci brevemente della tua esperienza ..."></textarea>
	                </div>
	                
	                <div class="mc-form-group">
	                    <label class="mc-label" for="voto">Voto</label>
	                    <input type="number" class="mc-input" id="voto" name="voto" min="1" max="5"></input>
	                </div>
	                
			        <button class="mc-btn mc-btn--outline mc-btn--sm"type="submit">Salva</button>
		    	</form>
			</c:if>
		</div>
	</div>
	
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>