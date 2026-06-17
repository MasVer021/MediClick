<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="/WEB-INF/view/layout/header.jsp" />
<main>
	<section id="hero" class="mc-hero">
		<h1 class="mc-hero__title">Benvenuto in MediClick</h1>
		<p class="mc-hero__subtitle">La tua salute in un click. Trova il medico giusto per te e prenota subito la tua visita.</p>
		<a class="mc-btn mc-btn--primary" href="<c:url value='/search'/>">Cerca uno Specialista</a>
	</section>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp" />
