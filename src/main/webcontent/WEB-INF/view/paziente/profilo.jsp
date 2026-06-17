<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main class="mc-container mc-container-ver">
	<div class="mc-card mc-card--generic mc-mt-lg">
		<div class="mc-card__header">
			<h2 class="mc-card__title">Profilo Utente</h2>
		</div>
		<div class="mc-card__body mc-flex-col mc-gap-md">
			<div class="mc-flex-col">
				<span class="mc-text-muted mc-font-xs">Nome Paziente</span> <span class="mc-text-bold mc-font-md">${paziente.nome}</span>
			</div>
			<div class="mc-flex-col">
				<span class="mc-text-muted mc-font-xs">Cognome Paziente</span> <span class="mc-text-bold mc-font-md">${paziente.cognome}</span>
			</div>
			<div class="mc-flex-col">
				<span class="mc-text-muted mc-font-xs">Indirizzo email</span> <span class="mc-text-bold mc-font-md">${paziente.utente.email}</span>
			</div>
			<c:if test="${not modificaTelefono}">
				<div class="mc-flex-col">
					<span class="mc-text-muted mc-font-xs">Numero di telefono</span> <span class="mc-text-bold mc-font-md">${paziente.telefono}</span>
				</div>
				<a class="mc-btn mc-btn--outline mc-btn--sm" href="?edit=telefono">Modifica</a>
			</c:if>
			<c:if test="${modificaTelefono}">
				<form action="<c:url value='/paziente/profilo?edit=telefono'/>" method="post" id="form-telefono-paziente">
					<div class="mc-form-group">
						<label class="mc-text-muted mc-font-xs" for="nuovoTelefono">Numero di telefono</label> <input class="mc-input" type="text" name="nuovoTelefono" id="nuovoTelefono" value="${paziente.telefono}">
					</div>
					<button class="mc-btn mc-btn--success mc-btn--sm" type="submit">Salva</button>
					<a class="mc-btn mc-btn--danger mc-btn--sm" href="?">Annulla</a>
				</form>
			</c:if>
			<c:if test="${not modificaPassword}">
				<div class="mc-flex-col">
					<span class="mc-text-muted mc-font-xs">Password</span> <a class="mc-btn mc-btn--outline mc-btn--sm" href="?edit=password">Modifica</a>
				</div>
			</c:if>
			<c:if test="${modificaPassword}">
				<form action="<c:url value='/paziente/profilo?edit=password'/>" method="post" id="form-password-paziente">
					<div class="mc-form-group">
						<label class="mc-text-muted mc-font-xs" for="attualePassword">Password attuale</label> <input class="mc-input" type="password" name="attualePassword" id="attualePassword" required>
					</div>
					<div class="mc-form-group">
						<label class="mc-text-muted mc-font-xs" for="nuovaPassword">Nuova password</label> <input class="mc-input" type="password" name="nuovaPassword" id="nuovaPassword" required>
					</div>
					<div class="mc-form-group">
						<label class="mc-text-muted password" for="confermaPassword">Conferma nuova password</label> <input class="mc-input" type="password" name="confermaPassword" id="confermaPassword" required>
					</div>
					<button class="mc-btn mc-btn--success mc-btn--sm" type="submit">Salva</button>
					<a class="mc-btn mc-btn--danger mc-btn--sm" href="?">Annulla</a>
				</form>
			</c:if>
			<div class="mc-flex-col">
				<span class="mc-text-muted mc-font-xs">Codice fiscale</span> <span class="mc-text-bold mc-font-md">${paziente.codiceFiscale}</span>
			</div>
			<div class="mc-flex-col">
				<span class="mc-text-muted mc-font-xs">Data di nascita</span> <span class="mc-text-bold mc-font-md">${paziente.dataNascita}</span>
			</div>
			<c:if test="${msg != null}">
				<p>${msg}</p>
			</c:if>
		</div>
	</div>
</main>
<script src="${pageContext.request.contextPath}/js/validation-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/profilo-paziente.js"></script>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />