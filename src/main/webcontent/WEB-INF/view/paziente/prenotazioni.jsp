<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div>
	    <div>
	        <h3>Prenotazioni Valide</h3>
	        <h2>${numeroPrenotazioni}</h2>
	    </div>
	    <div>
	        <h3>Spesa Totale</h3>
	        <h2>&euro; ${spesaTotale}</h2>
	    </div>
	    <div >
	        <h3>Visite ancora da effetturare</h3>
	        <h2> ${visiteDaEffettuare}</h2>
	    </div>
	</div>

	<div>
		<c:if test="${empty prenotazioni}">
		    <p>Non hai ancora effettuato nessuna prenotazione.</p>
		    <a href="${pageContext.request.contextPath}/search">Cerca un medico</a>
		</c:if>
		<c:if test="${not empty prenotazioni}">
		    <table border="1" cellpadding="10">
		        <thead>
		            <tr>
		                <th>Codice</th>
		                <th>Data e Ora</th>
		                <th>Medico</th>
		                <th>Stato</th>
		                <th>Importo</th>
		               	<th>Azioni</th>
		            </tr>
		        </thead>
		        <tbody>
		            <c:forEach var="p" items="${prenotazioni}">
		                <tr>
		                    <td>${p.idTransazioneEsterno}</td>
		                    <td>${p.disponibilita.dataOraInizio}</td>
		                    <td>${p.erogazionePrestazione.catalogoPrestazioni.nome}</td>
		                    <td><strong>${p.stato.label}</strong></td>
		                    <td>&euro; ${p.importoPagato}</td>
		                    <td>
							    <c:if test="${p.stato.label == 'Confermata' && p.futura}">
								    <form action="${pageContext.request.contextPath}/paziente/prenotazioni" method="post" onsubmit="return confirm('Sei sicuro di voler disdire questa visita?');">
								        <input type="hidden" name="action" value="disdici">
								        <input type="hidden" name="prenotazioneId" value="${p.id}">
								        <button type="submit" class="btn btn-danger btn-sm">Disdici</button>
								    </form>
								</c:if>
								
								<c:if test="${(p.stato.label == 'Confermata' || p.stato.label == 'Erogata') && not p.futura}">
								    <a href="${pageContext.request.contextPath}/paziente/recensione?prenotazioneId=${p.id}" class="btn btn-primary btn-sm">Lascia Recensione</a>
								</c:if>
							</td>
		                </tr>
		            </c:forEach>
		        </tbody>
		    </table>
		</c:if>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>