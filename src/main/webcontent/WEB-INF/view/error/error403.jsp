<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>

<main class="mc-container">
    <div class="mc-card mc-card--generic mc-card--medium mc-text-center mc-p-xl">
        <h1 class="mc-text-danger mc-font-lg mc-mb-md">Errore 403</h1>
        <h2 class="mc-text-bold mc-mb-sm">Accesso Vietato</h2>
        <p class="mc-text-muted mc-mb-lg">Non hai i permessi necessari per accedere a questa pagina.</p>
        <a href="${pageContext.request.contextPath}/" class="mc-btn mc-btn--primary">Torna alla Home</a>
    </div>
</main>

<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>