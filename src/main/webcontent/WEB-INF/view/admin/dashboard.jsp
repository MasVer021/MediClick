<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="admin-container">
	<h2>Dashboard Amministratore</h2>
	<div class="stats-row">
		<div class="stat-card card-blue">
			<h4>Medici Registrati</h4>
			<p>${stats.totaleMedici}</p>
		</div>

		<div class="stat-card card-yellow">
			<h4>Medici da Approvare</h4>
			<p>${stats.mediciDaApprovare}</p>
		</div>


		<div class="stat-card card-green">
			<h4>Ricavi Totali Piattaforma</h4>
			<p>
				&euro;
				<fmt:formatNumber value="${stats.guadagniPiattaforma}"
					pattern="#,##0.00" />
			</p>
		</div>
	</div>

	<section class="admin-section">
		<h3>Medici in attesa di verifica</h3>

		<c:choose>
			<c:when test="${not empty mediciInAttesa}">

				<div class="table-responsive">
					<table class="admin-table">
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
									<td><span class="status-badge badge-warning">${medico.statoVerifica}</span>
									</td>

									<c:set var="certificatiSingoloMedico"
										value="${certificatiMedico[medico.id]}" />

									<c:forEach items="${tipiCertificato}" var="tc">
										<td><c:set var="trovato" value="false" /> <c:set
												var="certificatoTrovato" value="" /> <c:forEach
												items="${certificatiSingoloMedico}" var="cert">
												<c:if test="${cert.tipoCertificatoId == tc.id}">
													<c:set var="trovato" value="true" />
													<c:set var="certificatoTrovato" value="${cert}" />
												</c:if>
											</c:forEach> <c:choose>
												<c:when test="${trovato}">
													<a
														href="${pageContext.request.contextPath}/admin/downloadCertificato?id=${certificatoTrovato.id}"
														target="_blank" class="cert-link"
														title="Visualizza ${certificatoTrovato.nomeFile}"> <span
														class="status-badge badge-info"><c:out
																value="${certificatoTrovato.nomeFile}" /></span>
													</a>
													<c:choose>
														<c:when
															test="${certificatoTrovato.stato == 'IN_REVISIONE' }">
															<div class="cert-actions">

																<form method="post"
																	action="${pageContext.request.contextPath}/admin/approvaMedico">
																	<input type="hidden" name="certificatoId"
																		value="${certificatoTrovato.id}"> <input
																		type="hidden" name="approvato" value="true">
																	<button type="submit" class="btn-micro btn-success"
																		title="Approva documento">approva</button>
																</form>

																<form method="post"
																	action="${pageContext.request.contextPath}/admin/approvaMedico">
																	<input type="hidden" name="certificatoId"
																		value="${certificatoTrovato.id}"> <input
																		type="hidden" name="approvato" value="false">
																	<button type="submit" class="btn-micro btn-danger"
																		title="Rifiuta documento">rifiuta</button>
																</form>
															</div>
														</c:when>
														<c:otherwise>
															<span>${certificatoTrovato.stato}</span>
														</c:otherwise>
													</c:choose>
												</c:when>
												<c:otherwise>
													<span class="status-badge badge-secondary">Non
														caricato</span>
												</c:otherwise>
											</c:choose></td>
									</c:forEach>

									<td>

										<form method="post"
											action="${pageContext.request.contextPath}/admin/approvaMedico"
											class="action-form">
											<input type="hidden" name="medicoId" value="${medico.id}">
											<input type="hidden" name="approvato" value="true">
											<button type="submit" class="btn btn-success">Approva</button>
										</form>

										<form method="post"
											action="${pageContext.request.contextPath}/admin/approvaMedico"
											class="action-form">
											<input type="hidden" name="medicoId" value="${medico.id}">
											<input type="hidden" name="approvato" value="false">
											<button type="submit" class="btn btn-danger">Rifiuta</button>
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</c:when>
			<c:otherwise>
				<p class="empty-message">Non ci sono medici in attesa di verifica al momento.</p>
			</c:otherwise>
		</c:choose>
	</section>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />