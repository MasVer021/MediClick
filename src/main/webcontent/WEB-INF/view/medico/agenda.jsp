<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<main class="mc-container mc-container-ver mc-mb-xl">
	<div class="mc-card mc-card--generic mc-card--large">
		<div class="mc-card__header ">
			<h2 class="mc-card__title">La tua Agenda</h2>
			<form action="${pageContext.request.contextPath}/medico/agenda" method="GET">
				<div class="mc-form-group">
					<label class="mc-label" for="data">Seleziona Data</label> <input class="mc-input" type="date" id="data" name="data" value="${dataMostrata}" required>
				</div>
				<button class="mc-btn mc-btn--primary" type="submit">Filtra</button>
				<a class="mc-btn mc-btn--outline" href="${pageContext.request.contextPath}/medico/agenda">Vai a Oggi</a>
			</form>
		</div>
		<div class="mc-card__body">
			<c:choose>
				<c:when test="${empty agenda}">
					<p>Nessun appuntamento o disponibilità per questa data.</p>
				</c:when>
				<c:otherwise>
					<div class="mc-grid mc-grid-3">
						<c:forEach var="slot" items="${agenda}">
							<div class="mc-card mc-card--generic ${slot.statoSlot == 'Disponibile' ? 'mc-card--libera' : ''}">
								<div class="mc-card__header">
									<h2 class="mc-card__title mc-mb-none" style="display: inline">
										<fmt:parseDate value="${slot.dataOraInizio}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedInizio" type="both" />
										<fmt:formatDate value="${parsedInizio}" pattern="HH:mm" />
										-
										<fmt:parseDate value="${slot.dataOraFine}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedFine" type="both" />
										<fmt:formatDate value="${parsedFine}" pattern="HH:mm" />
									</h2>
									<c:set var="badgeClass" value="mc-badge--secondary" />
									<c:if test="${slot.statoSlot == 'Prenotata'}">
										<c:set var="badgeClass" value="mc-badge--info" />
									</c:if>
									<c:if test="${slot.statoSlot == 'Completata'}">
										<c:set var="badgeClass" value="mc-badge--success" />
									</c:if>
									<span class="mc-badge ${badgeClass}">${slot.statoSlot}</span>
								</div>
								<div class="mc-card__body">
									<c:choose>
										<c:when test="${slot.statoSlot == 'Prenotata' || slot.statoSlot == 'Completata'}">
											<div class="mc-flex-col mc-mg-sm">
												<span class="mc-text-muted mc-font-xs">Paziente</span> <span class="mc-text-bold mc-font-md">${slot.nomePaziente} ${slot.cognomePaziente}</span>
											</div>
											<div class="mc-flex-col">
												<span class="mc-text-muted mc-font-xs">Prestazione</span> <span class="mc-text-bold mc-font-md">${slot.nomePrestazione}</span>
											</div>
											<div class="mc-flex-col">
												<span class="mc-text-muted mc-font-xs">Telefono</span> <span class="mc-text-bold mc-font-md">${slot.telefonoPaziente}</span>
											</div>
										</c:when>
										<c:otherwise>
											<p class="mc-text-muted">Slot Libero</p>
										</c:otherwise>
									</c:choose>
								</div>
								<div class="mc-card__footer">
									<c:choose>
										<c:when test="${slot.statoSlot == 'Prenotata'}">
											<form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
												<input type="hidden" name="action" value="completa"> <input type="hidden" name="prenotazioneId" value="${slot.prenotazioneId}"> <input type="hidden" name="data" value="${dataMostrata}">
												<button class="mc-btn mc-btn--success" type="submit">Concludi Visita</button>
											</form>
											<form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
												<input type="hidden" name="action" value="annulla"> <input type="hidden" name="prenotazioneId" value="${slot.prenotazioneId}"> <input type="hidden" name="data" value="${dataMostrata}">
												<button class="mc-btn mc-btn--danger" type="submit">Annulla</button>
											</form>
										</c:when>
										<c:when test="${slot.statoSlot == 'Completata'}">
											<p class="mc-text-muted">Visita Conclusa</p>
										</c:when>
										<c:otherwise>
											<form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
												<input type="hidden" name="action" value="rimuovi"> <input type="hidden" name="disponibilitaId" value="${slot.disponibilitaId}"> <input type="hidden" name="data" value="${dataMostrata}">
												<button class="mc-btn mc-btn--danger" type="submit">Rimuovi Slot</button>
											</form>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
						</c:forEach>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />