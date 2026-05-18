<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>

<main>
    <h1>Elenco Medici Registrati</h1>
    <p>Visualizza e gestisci lo stato di attivazione degli account dei medici sulla piattaforma.</p>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Nome e Cognome</th>
                <th>P.IVA</th>
                <th>Stato Verifica</th>
                <th>Stato Account</th>
                <th>Azione</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${medici}" var="m">
                <tr>
                    <td><c:out value="${m.id}"/></td>
                    <td><c:out value="${m.nomeCompleto}"/></td>
                    <td><c:out value="${m.pIva}"/></td>
                    <td><c:out value="${m.statoVerifica.label}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${m.utente.accountAttivo}">
                                Attivo / Attivo
                            </c:when>
                            <c:otherwise>
                                Bloccato / Non Attivo
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <form action="${pageContext.request.contextPath}/admin/medici" method="post">
                            <input type="hidden" name="medicoId" value="${m.id}">
                            <c:choose>
                                <c:when test="${m.utente.accountAttivo}">
                                    <input type="hidden" name="blocca" value="true">
                                    <button type="submit">Blocca Account</button>
                                </c:when>
                                <c:otherwise>
                                    <input type="hidden" name="blocca" value="false">
                                    <button type="submit">Sblocca Account</button>
                                </c:otherwise>
                            </c:choose>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty medici}">
                <tr>
                    <td colspan="6">Nessun medico registrato nel sistema.</td>
                </tr>
            </c:if>
        </tbody>
    </table>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>
