<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>

<main class="mc-container">
	
	<div class="mc-card mc-card--small mc-card--primary mc-card--login">
		<div class="mc-card__header">
			<h2 class="mc-card__title">Accedi a MediClick</h2>
			<p class="mc-card--login__subtitle">Inserisci le tue credenziali per accedere</p>
		</div>
		
		<form method="post" action="${pageContext.request.contextPath}/login" class="mc-card__body">
			
			<div class="mc-form-group">
				<label class="mc-label" for="email">Indirizzo Email</label>
				<input class="mc-input" type="email" id="email" name="email" placeholder="nome@esempio.it" required>
			</div>
			
			
			<div class="mc-form-group">
				<label class="mc-label" for="password">Password</label>
				<input class="mc-input" type="password" id="password" name="password" placeholder="******" required>
			</div>
			
			
			<div class="mc-flex-row mc-align-center" style="margin-bottom: 1.5rem;">
				<input type="checkbox" name="rememberMe" value="true" id="rememberMe" style="cursor: pointer; width: 18px; height: 18px;">
				<label for="rememberMe" style="cursor: pointer; font-size: var(--font-xs); color: var(--testo-principale); font-weight: 500;">
					Ricordami su questo dispositivo
				</label>
			</div>
			
			
			<button class="mc-btn mc-btn--primary mc-btn--block" type="submit">
				Accedi
			</button>
		</form>
	</div>
</main>

<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>