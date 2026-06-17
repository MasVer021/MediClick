<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />
	<main class="mc-container">
		<div class="mc-card mc-card--generic mc-card--small mc-text-center mc-p-xl">
			<div class="mc-message mc-message--success mc-mb-lg">
				<span class="mc-message__message" style="margin: 0 auto;">Prenotazione Completata!</span>
			</div>


			<h1 class="mc-card__title mc-font-lg mc-mb-md">Congratulazioni!</h1>

			<p class="mc-text-muted mc-mb-xl">La tua prenotazione è stata registrata ed effettuata correttamente nel
				nostro sistema.</p>

			<a href="${pageContext.request.contextPath}/paziente/prenotazione"
			   class="mc-btn mc-btn--primary mc-btn--block">Vai alle prenotazioni</a>

		</div>
	</main>
	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />