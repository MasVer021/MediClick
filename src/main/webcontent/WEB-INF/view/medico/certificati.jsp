<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
    <h2>I Miei Certificati</h2>
    
    <section>
        <h3>Documenti Caricati</h3>
        <table border="1" cellpadding="5">
            <thead>
                <tr>
                    <th>Nome Documento</th>
                    <th>Tipo Certificato</th>
                    <th>Obbligatorio</th>
                    <th>Data Caricamento</th>
                    <th>Data Scadenza</th>
                    <th>Stato</th>
                    <th>Azione</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach  items="${certificatiCaricati}" var="cert">
                    <tr>
                        <td><strong>${cert.nomeFile}</strong></td>
                        <td>${cert.tipoCertificato.nome}</td>
                        <td>
                            <c:choose>
                                <c:when test="${cert.tipoCertificato.obbligatorio}">
                                    <span>Sì</span>
                                </c:when>
                                <c:otherwise>No</c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <fmt:parseDate value="${cert.dataCaricamento}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedCaricamento" type="both"/>
                            <fmt:formatDate value="${parsedCaricamento}" pattern="dd/MM/yyyy HH:mm"/>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty cert.dataScadenza}">
                                    <fmt:parseDate value="${cert.dataScadenza}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedScadenza" type="both"/>
                                    <fmt:formatDate value="${parsedScadenza}" pattern="dd/MM/yyyy"/>
                                </c:when>
                                <c:otherwise>
                                    <i>Nessuna Scadenza</i>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${cert.stato == 'APPROVATO'}">
                                    <span><strong>Approvato</strong></span>
                                </c:when>
                                <c:when test="${cert.stato == 'RIFIUTATO'}">
                                    <span><strong>Rifiutato</strong></span>
                                </c:when>
                                <c:when test="${cert.stato == 'SCADUTO'}">
                                    <span><strong>Scaduto</strong></span>
                                </c:when>
                                <c:otherwise>
                                    <span ><i>In revisione</i></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <form action="${pageContext.request.contextPath}/medico/certificati" method="POST">
                                <input type="hidden" name="action" value="elimina-certificato">
                                <input type="hidden" name="certificatoId" value="${cert.id}">
                                <button type="submit" onclick="return confirm('Sei sicuro di voler eliminare questo certificato?');">Elimina</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty certificatiCaricati}">
                    <tr>
                        <td colspan="7" align="center">Nessun certificato caricato finora.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </section>

    <hr>
    <section>
        <h3>Carica un nuovo documento</h3>
        <form action="${pageContext.request.contextPath}/medico/certificati" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="carica-certificato">
            <p>
                <label for="tipoCertificatoId">Seleziona il tipo di certificato:</label><br>
                <select id="tipoCertificatoId" name="tipoCertificatoId" required>
                    <option value="">-- Seleziona Tipo --</option>
                    <c:forEach items="${tipiCertificato}"  var="tc" >
                        <option value="${tc.id}">${tc.nome} ${tc.obbligatorio ? '(Obbligatorio)' : ''}</option>
                    </c:forEach>
                </select>
            </p>
            <p>
                <label for="documento">Seleziona il file (PDF max 5MB):</label><br>
                <input type="file" id="documento" name="documento" accept="application/pdf" required>
            </p>

            <p>
                <label for="dataScadenza">Data di Scadenza (se prevista):</label><br>
                <input type="date" id="dataScadenza" name="dataScadenza">
            </p>

            <button type="submit">Carica Documento</button>
        </form>
    </section>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>