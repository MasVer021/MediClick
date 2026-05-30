<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main  class="mc-container mc-container-ver">

	 <div class="mc-message mc-message--error mc-mb-lg">
		<span class="mc-message__message">
			Completa il pagamento entro (da inserire un timer ) per confermare la tua prenotazione
		</span>
	</div>
	
	 <div class="mc-grid mc-grid-2">
	 
		<div class="mc-card mc-card--generic">
		
		    <div class="mc-card__header">
		        <h2 class="mc-card__title">Riepilogo Prenotazione</h2>
		    </div>
		    
		    <div class="mc-card__body mc-flex-col mc-gap-md">
		    
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Medico Specialistico</span>
		            <span class="mc-text-bold mc-font-md">DR. ${riepilogo.medico.cognome} ${riepilogo.medico.nome}</span>
		        </div>
		        
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Prestazione</span>
		            <span>${riepilogo.catalogoPrestazioni.nome}</span>
		        </div>
		        
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Data e Ora</span>
		            <span>${riepilogo.disponibilita.dataOraInizio}</span>
		        </div>
		        
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Prezzo</span>
		            <span class="mc-text-bold mc-font-md">${riepilogo.prestazione.prezzoLordoListino} &euro;</span>
		        </div>
		        
		        <div class="mc-flex-col">
		            <span class="mc-text-muted mc-font-xs">Sede dello Studio</span>
		            <span class="mc-text-muted">${riepilogo.studio.indirizzoMaps}</span>
		        </div>
		        
		    </div>
		    
		</div>
		
		<div class="mc-card mc-card--generic">
		
	    <div class="mc-card__header">
	        <h2 class="mc-card__title">Pagamento</h2>
	    </div>
	    
	    <div class="mc-card__body">
	        <form method="post" action='<%=response.encodeUrl(request.getContextPath() +"/paziente/prenotazione")%>' class="mc-flex-col mc-gap-md">
	            
	            <c:if test="${not empty Errore}">
	                <div class="mc-message mc-message--error">
	                    <span class="mc-message__message">${Errore}</span>
	                </div>
	            </c:if>
	            
	            <div class="mc-flex-col mc-gap-sm">
	                <label class="mc-label">Metodo di Pagamento</label>
	                
	                <label class="mc-flex-row mc-align-center mc-gap-sm">
	                    <input type="radio" name="metodoPagamento" value="carta" required>
	                    <span>Carta di Credito o Debito</span>
	                </label>
	                
	                <div class="mc-form-group">
	                    <input type="text" class="mc-input" name="ncarta" placeholder="Numero della carta (16 cifre)">
	                </div>
	                
	                <label class="mc-flex-row mc-align-center mc-gap-sm">
	                    <input type="radio" name="metodoPagamento" value="paypal" required>
	                    <span>PayPal</span>
	                </label>
	                
	                <label class="mc-flex-row mc-align-center mc-gap-sm">
	                    <input type="radio" name="metodoPagamento" value="apple" required>
	                    <span>Apple Pay</span>
	                </label>
	                
	                <label class="mc-flex-row mc-align-center mc-gap-sm">
	                    <input type="radio" name="metodoPagamento" value="google" required>
	                    <span>Google Pay</span>
	                </label>
	            </div>
	            
	            <div class="mc-form-group">
	                <label for="codiceSconto" class="mc-label">Codice Sconto</label>
	                <div class="mc-flex-row mc-gap-sm">
	                    <input type="text" class="mc-input" name="codiceSconto" id="codiceSconto" placeholder="Es. CLICK10" style="flex: 1;">
	                    <button type="button" class="mc-btn mc-btn--outline mc-btn--sm">Applica</button>
	                </div>
	            </div>
	            
	            <button type="submit" class="mc-btn mc-btn--primary mc-btn--block mc-mt-md">Procedi al Pagamento</button>
	            <a href="${pageContext.request.contextPath}/paziente/prenotazione?action=annulla" class="mc-btn mc-btn--outline mc-btn--danger mc-btn--block">Annulla Prenotazione</a>
	        </form>
	    </div>
</div>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>