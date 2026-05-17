<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
	<div class="container-statistiche">
	    
	    <h2>DashBoard</h2>
	    
	    <form action="${pageContext.request.contextPath}/medico/dashboard" method="GET">
	        <div >
	            
	            <div>
	                <label for="dataInizio" >Data Inizio:</label>
	                <input type="date" id="dataInizio" name="dataInizio" value="${dataInizio}" required>
	            </div>
	            
	            <div>
	                <label for="dataFine" >Data Fine:</label>
	                <input type="date" id="dataFine" name="dataFine" value="${dataFine}" required>
	            </div>
	            
	            <div>
	                <button type="submit">
	                    Filtra
	                </button>
	            </div>
	            
	        </div>
	    </form>

	    <div class="grid-statistiche">
	        
	       
	        <div class="box-stat" >
	            <h4 >Visite Completate</h4>
	            <p >${stats.conteggio}</p>
	        </div>
	    
	        <div class="box-stat" >
	            <h4 >Guadagno Netto</h4>
	            <p >
	                <fmt:formatNumber value="${stats.guadagnoNetto}" type="currency" currencySymbol="&euro;"/>
	            </p>
	        </div>
	       
	        <div class="box-stat" >
	            <h4 >Tasse Stimate</h4>
	            <p s>
	                <fmt:formatNumber value="${stats.tasseStimate}" type="currency" currencySymbol="&euro;"/>
	            </p>
	        </div>
	    </div>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>