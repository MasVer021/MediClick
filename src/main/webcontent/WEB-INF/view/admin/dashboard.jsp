<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="mc-container mc-container-ver mc-mb-xl">
	<div class="mc-kpi-grid">
		<div class="mc-kpi-card">
			<span class="mc-kpi-card__title">Medici Registrati</span> <span class="mc-kpi-card__value">${stats.totaleMedici}</span>
		</div>
		<div class="mc-kpi-card ">
			<span class="mc-kpi-card__title">Medici da Approvare</span> <span class="mc-kpi-card__value">${stats.mediciDaApprovare}</span>
		</div>
		<div class="mc-kpi-card">
			<span class="mc-kpi-card__title">Ricavi Totali Piattaforma</span> <span class="mc-kpi-card__value">&euro; <fmt:formatNumber value="${stats.guadagniPiattaforma}" pattern="#,##0.00" />
			</span>
		</div>
	</div>
	<section class="mc-card mc-card--generic">
		<div class="mc-card__header">
			<h3 class="mc-card__title">Medici in attesa di verifica</h3>
		</div>
		<div class="mc-card__body  mc-table-container">
			<c:choose>
				<c:when test="${not empty mediciInAttesa}">
					<table class="mc-table mc-table--zebra">
						<thead>
							<tr>
								<th>ID</th>
								<th>Cognome</th>
								<th>Nome</th>
								<th>P. IVA</th>
								<th>Stato</th>
								<c:forEach items="${tipiCertificato}" var="tc">
									<th>${tc.nome}</th>
								</c:forEach>
								<th>Azioni</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${mediciInAttesa}" var="medico">
								<tr>
									<td>${medico.id}</td>
									<td><c:out value="${medico.cognome}" /></td>
									<td><c:out value="${medico.nome}" /></td>
									<td><c:out value="${medico.pIva}" /></td>
									<td><span class="mc-badge mc-badge--warning">${medico.statoVerifica}</span></td>
									<c:set var="certificatiSingoloMedico" value="${certificatiMedico[medico.id]}" />
									<c:forEach items="${tipiCertificato}" var="tc">
										<td><c:set var="trovato" value="false" /> <c:set var="certificatoTrovato" value="" /> <c:forEach items="${certificatiSingoloMedico}" var="cert">
												<c:if test="${cert.tipoCertificatoId == tc.id}">
													<c:set var="trovato" value="true" />
													<c:set var="certificatoTrovato" value="${cert}" />
												</c:if>
											</c:forEach> <c:choose>
												<c:when test="${trovato}">
													<a href="${pageContext.request.contextPath}/admin/downloadCertificato?id=${certificatoTrovato.id}" target="_blank" class="cert-link" title="Visualizza ${certificatoTrovato.nomeFile}"> <c:if test="${certificatoTrovato.stato == 'RIFIUTATO'}">
															<c:set var="badge" value="danger" />
														</c:if> <c:if test="${certificatoTrovato.stato == 'APPROVATO'}">
															<c:set var="badge" value="success" />
														</c:if> <span class="mc-badge mc-badge--${badge}"> <c:out value="${certificatoTrovato.nomeFile}" />
													</span>
													</a>
													<c:choose>
														<c:when test="${certificatoTrovato.stato == 'IN_REVISIONE' }">
															<div class="mc-table-action-cell mc-mt-sm">
																<form method="post" action="${pageContext.request.contextPath}/admin/approvaMedico">
																	<input type="hidden" name="certificatoId" value="${certificatoTrovato.id}"> <input type="hidden" name="approvato" value="true">
																	<button type="submit" class="mc-btn mc-btn--success mc-btn--sm" title="Approva documento">approva</button>
																</form>
																<form method="post" action="${pageContext.request.contextPath}/admin/approvaMedico">
																	<input type="hidden" name="certificatoId" value="${certificatoTrovato.id}"> <input type="hidden" name="approvato" value="false">
																	<button type="submit" class="mc-btn mc-btn--danger mc-btn--sm" title="Rifiuta documento">rifiuta</button>
																</form>
															</div>
														</c:when>
													</c:choose>
												</c:when>
												<c:otherwise>
													<span class="mc-badge mc-badge--warning">Non caricato</span>
												</c:otherwise>
											</c:choose></td>
									</c:forEach>
									<td>
										<div class="mc-table-action-cell">
											<form method="post" action="${pageContext.request.contextPath}/admin/approvaMedico" class="action-form">
												<input type="hidden" name="medicoId" value="${medico.id}"> <input type="hidden" name="approvato" value="true">
												<button type="submit" class="mc-btn mc-btn--success mc-btn--sm">Approva</button>
											</form>
											<form method="post" action="${pageContext.request.contextPath}/admin/approvaMedico" class="action-form">
												<input type="hidden" name="medicoId" value="${medico.id}"> <input type="hidden" name="approvato" value="false">
												<button type="submit" class="mc-btn mc-btn--danger mc-btn--sm">Rifiuta</button>
											</form>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</c:when>
				<c:otherwise>
					<p class="mc-text-muted">Non ci sono medici in attesa di verifica al momento.</p>
				</c:otherwise>
			</c:choose>
		</div>
	</section>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />