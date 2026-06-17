<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="mc-container mc-container-ver mc-mb-xl">
	<div class="mc-mb-md">
		<h1 class="mc-font-xl mc-text-bold">Prenotazioni Piattaforma</h1>
		<p class="mc-text-muted">Visualizza e filtra lo storico di tutte le visite mediche prenotate.</p>
	</div>
	<div class="mc-card mc-card--generic mc-mb-lg">
		<div class="mc-card__header">
			<h3 class="mc-card__title">Filtra Risultati</h3>
		</div>
		<div class="mc-card__body">
			<form method="get" action="${pageContext.request.contextPath}/admin/prenotazioni" class="mc-flex-row mc-align-center mc-gap-md" style="flex-wrap: wrap;">
				<div class="mc-form-group" style="flex: 1; min-width: 200px; margin-bottom: 0;">
					<label class="mc-label" for="codiceFiscale">Codice Fiscale Cliente</label> <input class="mc-input" type="text" id="codiceFiscale" name="codiceFiscale" value="<c:out value=" ${codiceFiscaleFiltro}" />" placeholder="Codice Fiscale">
				</div>
				<div class="mc-form-group" style="flex: 1; min-width: 150px; margin-bottom: 0;">
					<label class="mc-label" for="dataInizio">Dal (Data)</label> <input class="mc-input" type="date" id="dataInizio" name="dataInizio" value="<c:out value="
							   ${dataInizioFiltro}" />">
				</div>
				<div class="mc-form-group" style="flex: 1; min-width: 150px; margin-bottom: 0;">
					<label class="mc-label" for="dataFine">Al (Data)</label> <input class="mc-input" type="date" id="dataFine" name="dataFine" value="<c:out value="
							   ${dataFineFiltro}" />">
				</div>
				<div class="mc-flex-row mc-gap-sm mc-mt-md" style="align-self: flex-end; margin-top: 0;">
					<button class="mc-btn mc-btn--secondary" type="submit">Filtra</button>
					<a href="${pageContext.request.contextPath}/admin/prenotazioni" class="mc-btn mc-btn--outline">Azzera</a>
				</div>
			</form>
		</div>
	</div>
	<div class="mc-card mc-card--generic">
		<div class="mc-card__header">
			<h3 class="mc-card__title">Elenco Visite Prenotate</h3>
		</div>
		<div class="mc-card__body mc-table-container">
			<table class="mc-table mc-table--zebra">
				<thead>
					<tr>
						<th>Codice Transazione</th>
						<th>Data Visita</th>
						<th>Paziente</th>
						<th>Medico</th>
						<th>Prestazione</th>
						<th>Importo</th>
						<th>Metodo Pagamento</th>
						<th>Stato</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${prenotazioni}" var="p">
						<tr>
							<td><span class="mc-text-bold"> <c:out value="${p.idTransazioneEsterno}" />
							</span></td>
							<td><c:out value="${p.disponibilita.dataOraInizio}" /></td>
							<td><c:out value="${p.paziente.cognome} ${p.paziente.nome}" /> (CF: <c:out value="${p.paziente.codiceFiscale}" />)</td>
							<td>Dott. <c:out value="${p.erogazionePrestazione.medico.cognome} ${p.erogazionePrestazione.medico.nome}" />
							</td>
							<td><c:out value="${p.erogazionePrestazione.catalogoPrestazioni.nome}" /></td>
							<td>&euro; <c:out value="${p.importoPagato}" />
							</td>
							<td><c:out value="${p.metodoPagamento}" /></td>
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
						</tr>
					</c:forEach>
					<c:if test="${empty prenotazioni}">
						<tr>
							<td colspan="8" class="mc-text-center mc-text-muted mc-p-lg">Nessuna prenotazione trovata per i filtri selezionati.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />