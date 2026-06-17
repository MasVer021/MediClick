<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />
	<main class="mc-container mc-container-ver mc-mb-xl">

		<div class="mc-card mc-card--generic">
			<div class="mc-card__header ">
				<h2 class="mc-card__title">Inserisci nuove Disponibilità</h2>
			</div>
			<div class="mc-card__body">
				<form action="${pageContext.request.contextPath}/medico/disponibilita" method="POST"
					  id="disponibilita-medico">

					<div class="mc-form-group">
						<label class="mc-label" for="dataGiornata">Data:</label>
						<input class="mc-input" type="date" id="dataGiornata" name="dataGiornata" required>
					</div>

					<div class="mc-form-group">
						<label class="mc-label" for="oraInizio">Dalle ore:</label>
						<input class="mc-input" type="time" id="oraInizio" name="oraInizio" step="1800" required>
					</div>

					<div class="mc-form-group">
						<label class="mc-label" for="oraFine">Alle ore:</label>
						<input class="mc-input" type="time" id="oraFine" name="oraFine" step="1800" required>
					</div>

					<div class="mc-form-group">
						<label class="mc-label" for="studioId">Seleziona lo Studio:</label>
						<select class="mc-select" id="studioId" name="studioId" required>
							<option value="">-- Scegli uno studio --</option>
							<c:forEach var="studio" items="${studi}">
								<option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
							</c:forEach>
						</select>
					</div>

					<button class="mc-btn mc-btn--outline" type="submit">Salva Disponibilità</button>
				</form>
			</div>
		</div>
	</main>
	<script src="${pageContext.request.contextPath}/js/validation-utils.js"></script>
	<script src="${pageContext.request.contextPath}/js/disponibilita-medico.js"></script>
	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />