<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty successo}">
   <div class="mc-message mc-message--success">
 		<p class="mc-message__message">
 		${successo}
 		</p>
 	</div>
 <c:remove var="successo" scope="session" />
</c:if>
<footer class="mc-footer">
	<div class="mc-footer__item">
		<img src="${pageContext.request.contextPath}/img/BiaFullLogo.svg" alt="Logo completo bianco" >
		
		<p>La tua salute in un click.</p>
		
		<div class="mc-footer__social-links">
			<a href="https://www.instagram.com/mediclick" target="_blank"><img src="${pageContext.request.contextPath}/img/instagramBlackLogo.svg" class="social-icon" alt="Logo Instagram nero"></a>
			<a href="https://www.facebook.com/mediclick" target="_blank"><img src="${pageContext.request.contextPath}/img/facebookBlackLogo.svg" class="social-icon" alt="Logo Facebook nero"></a>
			<a href="https://www.x.com/mediclick" target="_blank"><img src="${pageContext.request.contextPath}/img/xBlackLogo.svg" class="social-icon" alt="Logo X nero"></a>	
		</div>
		
	</div>
	<div class="mc-footer__item mc-footer__item-border">
		<a href="${pageContext.request.contextPath}/search">Cerca un medico</a>
		<a href="${pageContext.request.contextPath}/info.jsp#come-funziona">Come funziona</a>
		<a href="${pageContext.request.contextPath}/search">Tutte le specializzazioni</a>	
	</div>
	<div class="mc-footer__item mc-footer__item-border">
		<a href="${pageContext.request.contextPath}/info.jsp#sei-un-medico">Sei un medico?</a>
		<a href="${pageContext.request.contextPath}/info.jsp#accreditamento">Accreditamento</a>
		<a href="${pageContext.request.contextPath}/info.jsp#centro-assistenza">Centro assistenza medici</a>
	</div>
	<div class="mc-footer__item mc-footer__item-border">
		<a href="${pageContext.request.contextPath}/info.jsp#privacy">Privacy Policy &amp; Cookie</a>
		<a href="${pageContext.request.contextPath}/info.jsp#termini">Termini e condizioni</a>
		<a href="${pageContext.request.contextPath}/info.jsp#contattaci">Contattaci : supporto@mediclick.it</a>
	    <p>&copy; 2026 MediClick - Prog ShadowMonarcDev </p>
	</div>
</footer>
</body>
</html>