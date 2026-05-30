<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main class="mc-container mc-container-ver mc-mb-xl">
	<div class="mc-kpi-grid">
		<div class="mc-kpi-card">
		    <span class="mc-kpi-card__title">Visite Completate</span>
		    <span class="mc-kpi-card__value">${stats.conteggio}</span>
		</div>
		
		
		<div class="mc-kpi-card ">
		    <span class="mc-kpi-card__title">Guadagno Netto</span>
		    <span class="mc-kpi-card__value"><fmt:formatNumber value="${stats.guadagnoNetto}" type="currency" currencySymbol="&euro;"/></span>
		</div>
		
		
		<div class="mc-kpi-card">
		    <span class="mc-kpi-card__title">Tasse Stimate</span>
		    <span class="mc-kpi-card__value"><fmt:formatNumber value="${stats.tasseStimate}" type="currency" currencySymbol="&euro;"/></span>
		</div>       
	</div>
	<div class="mc-card mc-card--generic">
		 <div class="mc-card__header ">
		 	<h2 class="mc-card__title">DashBoard</h2>
		 </div>
		 <div class="mc-card__body">
			  <form action="${pageContext.request.contextPath}/medico/dashboard" method="GET">
			  	<div class="mc-form-group">
					<label class="mc-label" for="dataInizio">Data inizio</label>
					<input class="mc-input" type="date" id="dataInizio" name="dataInizio" value="${dataInizio}" required>
				</div>
				<div class="mc-form-group">
					<label class="mc-label" for="dataFine">Data fine</label>
					<input class="mc-input" type="date" id="dataFine" name="dataFine" value="${dataFine}" required>
				</div>
	            <button class="mc-btn mc-btn--outline" type="submit">filtra</button>
		    </form>
		 </div> 
	</div>
	 
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>