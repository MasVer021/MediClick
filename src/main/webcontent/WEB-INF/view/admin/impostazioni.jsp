<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>

<main>
    <h1>Impostazioni di Sistema</h1>
    <p>Visualizza e modifica i parametri globali di configurazione del sistema.</p>

    <div>
        <h3>Modifica o Aggiungi Parametro</h3>
        <form action="${pageContext.request.contextPath}/admin/impostazioni" method="post">
            <div>
                <label>Chiave Parametro *</label>
                <input type="text" name="chiave" required placeholder="es. tasse_piattaforma">
            </div>
            <div>
                <label>Nuovo Valore *</label>
                <input type="text" name="valore" required placeholder="es. 15">
            </div>
            <button type="submit">Salva Impostazione</button>
        </form>
    </div>

    <div>
        <h3>Parametri di Configurazione Attivi</h3>
        <table>
            <thead>
                <tr>
                    <th>Chiave</th>
                    <th>Valore Corrente</th>
                    <th>Data Ultimo Aggiornamento</th>
                    <th>Modificato Da (Admin)</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${impostazioni}" var="imp">
                    <tr>
                        <td><strong><c:out value="${imp.chiave}"/></strong></td>
                        <td><c:out value="${imp.valore}"/></td>
                        <td><c:out value="${imp.dataInizio}"/></td>
                        <td><c:out value="${imp.amministratore.utente.email}"/></td>
                    </tr>
                </c:forEach>
                <c:if test="${empty impostazioni}">
                    <tr>
                        <td colspan="4">Nessun parametro impostato nel sistema.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</main>

<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>
