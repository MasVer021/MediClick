<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="mc-container">
	<div class="mc-card mc-card--generic mc-card--medium">
		<div class="mc-flex-col mc-align-center mc-mb-xl">
			<c:choose>
				<c:when test="${not empty profiloMedico.medico.fotoprofilo}">
					<img class="medico-avatar" src="data:image/jpeg;base64,${profiloMedico.medico.fotoprofiloBase64}" alt="Foto Dr. ${profiloMedico.medico.nome} ${profiloMedico.medico.cognome}" />
				</c:when>
				<c:otherwise>
					<div class="medico-avatar-placeholder">
						<span>${profiloMedico.medico.nome.substring(0,1)}${profiloMedico.medico.cognome.substring(0,1)}</span>
					</div>
				</c:otherwise>
			</c:choose>
			<h2 class="mc-card__title mc-mt-md">DR. ${profiloMedico.medico.cognome} ${profiloMedico.medico.nome}</h2>
		</div>
		<form method="get" action="<c:url value='/paziente/prenotazione'/>" id="Form-prenotazione">
			<div class="mc-form-group">
				<label class="mc-label">Studio Medico</label> <select name="studio" id="studioSelect" class="mc-select" data-medico-id="${profiloMedico.medico.id}">
					<option value="" disabled selected>Seleziona lo Studio</option>
					<c:forEach items="${profiloMedico.studi}" var="studio">
						<option value="${studio.id}">${studio.nomeSede}- ${studio.indirizzoMaps}</option>
					</c:forEach>
				</select>
			</div>
			<div class="mc-form-group">
				<label class="mc-label">Prestazione Richiesta</label> <select name="prestazione" id="prestazioneSelect" class="mc-select">
					<option value="" disabled selected>Seleziona prima uno Studio</option>
				</select>
			</div>
			<div class="mc-mt-lg">
				<h3 class="mc-card__title mc-mb-md">Scegli Data e Ora</h3>
				<div id="slotsContainer" class="mc-flex-col mc-gap-md">
					<p class="mc-text-muted">Seleziona uno studio per vedere gli orari disponibili.</p>
				</div>
			</div>
			<button id="btnConferma" class="mc-btn mc-btn--outline mc-btn--lg mc-mt-lg mc-btn--block" type="submit" disabled>Conferma Prenotazione</button>
		</form>
	</div>
</main>
<script src="${pageContext.request.contextPath}/js/validation-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/prenotazione.js"></script>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />