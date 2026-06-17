<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />

	<main class="mc-container mc-container-ver mc-mb-xl">
		<div class="mc-mb-md">
			<h1 class="mc-font-xl mc-text-bold">Impostazioni di Sistema</h1>
			<p class="mc-text-muted">Visualizza e modifica i parametri globali di configurazione del sistema.</p>
		</div>

		<div class="mc-card mc-card--generic mc-card--medium">
			<div class="mc-card__header">
				<h3 class="mc-card__title">Modifica o Aggiungi Parametro</h3>
			</div>
			<div class="mc-card__body">
				<form action="${pageContext.request.contextPath}/admin/impostazioni" method="post">
					<div class="mc-form-group">
						<label class="mc-label" for="chiave">Chiave Parametro *</label>
						<input class="mc-input" type="text" id="chiave" name="chiave" required
							   placeholder="es. tasse_piattaforma">
					</div>
					<div class="mc-form-group">
						<label class="mc-label" for="valore">Nuovo Valore *</label>
						<input class="mc-input" type="text" id="valore" name="valore" required placeholder="es. 15">
					</div>
					<button class="mc-btn mc-btn--outline mc-btn--block mc-mt-md" type="submit">Salva
						Impostazione</button>
				</form>
			</div>
		</div>

		<div class="mc-card mc-card--generic">
			<div class="mc-card__header">
				<h3 class="mc-card__title">Parametri di Configurazione Attivi</h3>
			</div>
			<div class="mc-card__body mc-table-container" style="margin-bottom: 0;">
				<table class="mc-table mc-table--zebra">
					<thead>
						<tr>
							<th>Chiave</th>
							<th>Valore Corrente</th>
							<th>Data Ultimo Aggiornamento</th>
							<th>Modificato Da (Admin)</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${impostazioni}" var="imp">
							<tr>
								<td><span class="mc-text-bold">
										<c:out value="${imp.chiave}" />
									</span></td>
								<td>
									<c:out value="${imp.valore}" />
								</td>
								<td>
									<c:out value="${imp.dataInizio}" />
								</td>
								<td>
									<c:out value="${imp.amministratore.utente.email}" />
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty impostazioni}">
							<tr>
								<td colspan="4" class="mc-text-center mc-text-muted mc-p-lg">Nessun parametro impostato
									nel sistema.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</main>

	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />