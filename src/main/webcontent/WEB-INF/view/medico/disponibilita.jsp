<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
  <h2>Inserisci nuove Disponibilità</h2>
    <p><a href="${pageContext.request.contextPath}/medico/agenda">Torna all'Agenda</a></p>
    <!-- Blocco per mostrare eventuali errori (es. date invertite o doppioni) -->
    <c:if test="${not empty errore}">
        <div>
            <strong>Errore:</strong> ${errore.messaggio}
        </div>
    </c:if>
    <!-- Form di inserimento -->
    <form action="${pageContext.request.contextPath}/medico/disponibilita" method="POST">
        
        <fieldset>
            <legend>Dettagli Orario</legend>
            
         
            <p>
                <label for="dataGiornata">Data:</label><br>
                <input type="date" id="dataGiornata" name="dataGiornata" required>
            </p>
     
            <p>
                <label for="oraInizio">Dalle ore:</label><br>
                <input type="time" id="oraInizio" name="oraInizio" step="1800" required>
            </p>
         
            <p>
                <label for="oraFine">Alle ore:</label><br>
                <input type="time" id="oraFine" name="oraFine" step="1800" required>
            </p>
    
            <p>
                <label for="studioId">Seleziona lo Studio:</label><br>
                <select id="studioId" name="studioId" required>
                    <option value="">-- Scegli uno studio --</option>
                    
                   
                    <c:forEach var="studio" items="${studi}">
                       <option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
                    </c:forEach>
                </select>
            </p>
            
        </fieldset>
        <br>
        <button type="submit">Salva Disponibilità</button>
        
    </form>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>