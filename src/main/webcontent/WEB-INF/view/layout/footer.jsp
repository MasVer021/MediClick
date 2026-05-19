<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty successo}">
   <div class="mc-message mc-message--success">
 		<p class="mc-message__message">
 		${successo}
 		</p>
 	</div>
</c:if>
<footer class="mc-footer">
	<div class="mc-footer__item">
		<img src="${pageContext.request.contextPath}/img/BiaFullLogo.svg" alt="Logo completo bianco" >
		
		<p>La tua salute in un click.</p>
		
		<div class="mc-footer__social-links">
			<a href="www.instatgram/mediclick"><img src = "${pageContext.request.contextPath}/img/instagramBlackLogo.svg" class="social-icon" alt="Logo istagram nero"></a>
			<a href="www.facebook/mediclick/"><img src = "${pageContext.request.contextPath}/img/facebookBlackLogo.svg" class="social-icon" alt="Logo facebook nero"></a>
			<a href="www.x/mediclick//"><img src = "${pageContext.request.contextPath}/img/xBlackLogo.svg" class="social-icon" alt="Logo x nero"></a>	
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