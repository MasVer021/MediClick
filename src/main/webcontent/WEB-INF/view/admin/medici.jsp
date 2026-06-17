<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />

	<main class="mc-container mc-container-ver mc-mb-xl">
		<div class="mc-mb-md">
			<h1 class="mc-font-xl mc-text-bold">Elenco Medici Registrati</h1>
			<p class="mc-text-muted">Visualizza e gestisci lo stato di attivazione degli account dei medici sulla
				piattaforma.</p>
		</div>

		<div class="mc-card mc-card--generic" style="width: 100%;">
			<div class="mc-card__header">
				<h3 class="mc-card__title">Lista Medici</h3>
			</div>
			<div class="mc-card__body mc-table-container">
				<table class="mc-table mc-table--zebra">
					<thead>
						<tr>
							<th>ID</th>
							<th>Nome e Cognome</th>
							<th>P.IVA</th>
							<th>Stato Verifica</th>
							<th>Stato Account</th>
							<th class="mc-text-right">Azione</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${medici}" var="m">
							<tr>
								<td>
									<c:out value="${m.id}" />
								</td>
								<td><span class="mc-text-bold">
										<c:out value="${m.nomeCompleto}" />
									</span></td>
								<td>
									<c:out value="${m.pIva}" />
								</td>
								<td>
									<c:choose>
										<c:when
												test="${m.statoVerifica == 'APPROVATO' || m.statoVerifica.label == 'Approvato' || m.statoVerifica.label == 'Verificato'}">
											<span class="mc-badge mc-badge--success">
												<c:out value="${m.statoVerifica.label}" />
											</span>
										</c:when>
										<c:when
												test="${m.statoVerifica == 'IN_ATTESA' || m.statoVerifica.label == 'In revisione' || m.statoVerifica.label == 'In Attesa'}">
											<span class="mc-badge mc-badge--warning">
												<c:out value="${m.statoVerifica.label}" />
											</span>
										</c:when>
										<c:otherwise>
											<span class="mc-badge mc-badge--secondary">
												<c:out value="${m.statoVerifica.label}" />
											</span>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${m.utente.accountAttivo}">
											<span class="mc-badge mc-badge--info">Attivo</span>
										</c:when>
										<c:otherwise>
											<span class="mc-badge mc-badge--danger">Bloccato</span>
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<div class="mc-table-action-cell">
										<form action="${pageContext.request.contextPath}/admin/medici" method="post">
											<input type="hidden" name="medicoId" value="${m.id}">
											<c:choose>
												<c:when test="${m.utente.accountAttivo}">
													<input type="hidden" name="blocca" value="true">
													<button class="mc-btn mc-btn--danger mc-btn--sm"
															type="submit">Blocca Account</button>
												</c:when>
												<c:otherwise>
													<input type="hidden" name="blocca" value="false">
													<button class="mc-btn mc-btn--success mc-btn--sm"
															type="submit">Sblocca Account</button>
												</c:otherwise>
											</c:choose>
										</form>
									</div>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty medici}">
							<tr>
								<td colspan="6" class="mc-text-center mc-text-muted mc-p-lg">Nessun medico registrato
									nel sistema.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</main>
	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />