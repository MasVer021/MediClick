<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="mc-container mc-container-ver">
	<div class="mc-kpi-grid">
		<div class="mc-kpi-card">
			<span class="mc-kpi-card__title">Prenotazioni Valide</span> <span class="mc-kpi-card__value">${numeroPrenotazioni}</span>
		</div>
		<div class="mc-kpi-card ">
			<span class="mc-kpi-card__title">Spesa Totale</span> <span class="mc-kpi-card__value">&euro; ${spesaTotale}</span>
		</div>
		<div class="mc-kpi-card">
			<span class="mc-kpi-card__title">Visite da effettuare</span> <span class="mc-kpi-card__value">${visiteDaEffettuare}</span>
		</div>
	</div>
	<div class="mc-card mc-card--generic mc-mt-lg">
		<div class="mc-card__header">
			<h2 class="mc-card__title">Prenotazioni</h2>
		</div>
		<div class="mc-card__body">
			<c:if test="${empty prenotazioni}">
				<div class="mc-text-center mc-p-xl">
					<p class="mc-text-muted mc-mb-lg">Non hai ancora effettuato nessuna prenotazione.</p>
					<a href="<c:url value='/search'/>" class="mc-btn mc-btn--primary">Cerca un Medico</a>
				</div>
			</c:if>
			<c:if test="${not empty prenotazioni}">
				<div class="mc-table-container">
					<table class="mc-table mc-table--zebra">
						<thead>
							<tr>
								<th>Codice</th>
								<th>Data e Ora</th>
								<th>Prestazione</th>
								<th>Stato</th>
								<th>Importo</th>
								<th class="mc-text-right">Azioni</th>
								<th>Fattura</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach var="p" items="${prenotazioni}">
								<tr>
									<td>${p.idTransazioneEsterno}</td>
									<fmt:parseDate value="${p.disponibilita.dataOraInizio}" var="dataOraForm" pattern="yyyy-MM-dd'T'HH:mm" type="both" />
									<td><fmt:formatDate value="${dataOraForm}" pattern="dd/MM/yyyy HH:mm" /></td>
									<td>${p.erogazionePrestazione.catalogoPrestazioni.nome}</td>
									<td><c:choose>
											<c:when test="${p.stato.label == 'Confermata'}">
												<span class="mc-badge mc-badge--success">${p.stato.label}</span>
											</c:when>
											<c:when test="${p.stato.label == 'Disdetta'}">
												<span class="mc-badge mc-badge--danger">${p.stato.label}</span>
											</c:when>
											<c:otherwise>
												<span class="mc-badge mc-badge--info">${p.stato.label}</span>
											</c:otherwise>
										</c:choose></td>
									<td>&euro; ${p.importoPagato}</td>
									<td>
										<div class="mc-table-action-cell">
											<c:if test="${p.stato.label == 'Confermata' && p.futura}">
												<form action="<c:url value='/paziente/prenotazioni'/>" method="post" onsubmit="return confirm('Sei sicuro di voler disdire questa visita?');" style="margin: 0;">
													<input type="hidden" name="action" value="disdici"> <input type="hidden" name="prenotazioneId" value="${p.id}">
													<button type="submit" class="mc-btn mc-btn--danger mc-btn--sm">Disdici</button>
												</form>
											</c:if>
											<c:if test="${(p.stato.label == 'Confermata' || p.stato.label == 'Erogata') && not p.futura}">
												<a href="<c:url value='/paziente/recensione?prenotazioneId=${p.id}'/>" class="mc-btn mc-btn--secondary mc-btn--sm">Recensisci</a>
											</c:if>
										</div>
									</td>
									<td><a href="<c:url value='/paziente/fattura?prenotazioneId=${p.id}'/>" class="mc-btn mc-btn--secondary mc-btn--sm">visualizza</a></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:if>
		</div>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />