<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div class="avvisdo-tempo-conferma">
		<!--TODO  inserire un timer   -->
		<p>Completa il pagamento entro (da inserire un timer ) per confermare la tua prenotazione</p>
	</div>
	<div class="riepilogo">
		<p>DR ${riepilogo.medico.cognome} ${riepilogo.medico.nome}</p>
		<p>${riepilogo.catalogoPrestazioni.nome}</p>
		<p>${riepilogo.disponibilita.dataOraInizio}</p>
		<p>${riepilogo.prestazione.prezzoLordoListino}</p>
		<p>${riepilogo.studio.indirizzoMaps}</p>
	</div>
	<div class="metodo-pagamento">
		<form method="post" action='<%=response.encodeUrl(request.getContextPath() +"/paziente/prenotazione")%>'>
			<c:if test="${not empty Errore}">
				<p class="errore-prenotazione">${Errore}</p>
			</c:if>
			
			<div class="opzioni-pagamento">
				<label><input type="radio" name="metodoPagamento" value="carta" required> Carta di credito</label><br>
				<input type="text" name="ncarta" placeholder="inserisci il numero della carta">
				<label><input type="radio" name="metodoPagamento" value="paypal" required> PayPal</label><br>
				<!--  redirect a paypal -->
				<label><input type="radio" name="metodoPagamento" value="apple" required> Apple Pay</label><br>
				<label><input type="radio" name="metodoPagamento" value="google" required> Google Pay</label><br>
				<!--  redirect a google pay o appleapu -->
			</div>
			
			<div class="sezione-sconto">
				<label for="codiceSconto">Inserire codice sconto</label>
				<input type="text" name="codiceSconto" id="codiceSconto">
				<button type="button">Applica</button>
			</div>
			
			<div class="sezione-submit">
				<button type="submit">Paga</button>
			</div>
		</form>
		<!--TODO  inserire un timer   -->
		<p>Completa il pagamento entro (da inserire un timer ) per confermare la tua prenotazione</p>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>