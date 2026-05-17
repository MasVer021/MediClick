<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
<div>
    
    <div>
        <h2>La tua Agenda</h2>
        
        <form action="${pageContext.request.contextPath}/medico/agenda" method="GET">
            <label for="data">Seleziona Data:</label>
            <input type="date" id="data" name="data" value="${dataMostrata}">
            
            <button type="submit">Filtra</button>
            
            <a href="${pageContext.request.contextPath}/medico/agenda">Vai a Oggi</a>
        </form>
    </div>

    <hr>
    <div>
        <c:choose>
            <c:when test="${empty agenda}">
                <p>Nessun appuntamento o disponibilità per questa data.</p>
            </c:when>
            
            <c:otherwise>
                <c:forEach var="slot" items="${agenda}">
                    
                    <div>
                        <div>
                            <strong>Orario: </strong>
                            <fmt:parseDate value="${slot.dataOraInizio}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedInizio" type="both" />
                            <fmt:formatDate value="${parsedInizio}" pattern="HH:mm"/>
                            -
                            <fmt:parseDate value="${slot.dataOraFine}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedFine" type="both" />
                            <fmt:formatDate value="${parsedFine}" pattern="HH:mm"/>
                            
                            <em>(Stato: ${slot.statoSlot})</em>
                        </div>
                        
                        <div>
                            <c:choose>
                                <c:when test="${slot.statoSlot == 'Prenotata' || slot.statoSlot == 'Completata'}">
                                    <p><strong>Paziente:</strong> ${slot.nomePaziente} ${slot.cognomePaziente}</p>
                                    <p><strong>Prestazione:</strong> ${slot.nomePrestazione}</p>
                                    <p><strong>Telefono:</strong> ${slot.telefonoPaziente}</p>
                                </c:when>
                                <c:otherwise>
                                    <p>Slot Libero</p>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div>
                            <c:choose>
                            
                                <c:when test="${slot.statoSlot == 'Prenotata'}">
                                    <form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
                                        <input type="hidden" name="action" value="completa">
                                        <input type="hidden" name="prenotazioneId" value="${slot.prenotazioneId}">
                                        <input type="hidden" name="data" value="${dataMostrata}">
                                        <button type="submit">Concludi Visita</button>
                                    </form>
                                    <form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
                                    	<input type="hidden" name="action" value="annulla">
                                        <input type="hidden" name="prenotazioneId" value="${slot.prenotazioneId}">
                                        <input type="hidden" name="data" value="${dataMostrata}">
                                        <button type="submit">Annulla</button>
                                    </form>
                                </c:when>
                                
                                <c:when test="${slot.statoSlot == 'Completata'}">
                                    <span>(Visita Conclusa)</span>
                                </c:when>
                                
                                <c:otherwise>
                                    <form action="${pageContext.request.contextPath}/medico/agenda" method="POST">
                                    	<input type="hidden" name="action" value="rimuovi">
                                        <input type="hidden" name="disponibilitaId" value="${slot.disponibilitaId}">
                                        <input type="hidden" name="data" value="${dataMostrata}">
                                        <button type="submit">Rimuovi Slot</button>
                                    </form>
                                </c:otherwise>
                                
                            </c:choose>
                        </div>
                    </div> 
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</div>

</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>