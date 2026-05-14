<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div class = "nome-immagine">
		<p>DR .${profiloMedico.medico.cognome} ${profiloMedico.medico.nome}</p>
		<c:choose>
			<c:when test="${not empty profiloMedico.medico.fotoprofilo}">
				<img class="medico-avatar" src="${profiloMedico.medico.fotoprofilo}" alt="Foto Dr. ${profiloMedico.medico.nome} ${profiloMedico.medico.cognome}" />
			</c:when>
			<c:otherwise>
				<div class="medico-avatar-placeholder">
					<span>${profiloMedico.medico.nome.substring(0,1)}${profiloMedico.medico.cognome.substring(0,1)}</span>
				</div>
			</c:otherwise>
		</c:choose>
	</div>
	<form method="get" action='<%= response.encodeUrl(request.getContextPath()+"/paziente/prenotazione")%>'>
		<div class ="studio">
			<select name="studio">
			    <option value="" disabled selected>Studio</option>
			    <c:forEach items="${profiloMedico.studi}" var="studio">
			        <option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
			    </c:forEach>
			</select>
		</div>
		<div class ="prestazione">
			<select name="prestazione">
			    <option value="" disabled selected>Prestazione</option>
			    <c:forEach items="${profiloMedico.prestazioni}" var="prestazione">
			        <option value="${prestazione.id}">${prestazione.catalogoPrestazioni.nome}</option>
			    </c:forEach>
			</select>
		</div>
		<div class="disponibilita">
	        <h3>Scegli Data e Ora</h3>
	        
	        <c:forEach items="${profiloMedico.disponibilitaRaggruppate}" var="entry">
	            <div class="blocco-giorno">
	                <h4>${entry.key}</h4> 
	                <div class="pulsantiera-orari">
	                    <c:forEach items="${entry.value}" var="slot">
	                        <div class="opzione-orario">
	                            <input type="radio" id="slot_${slot.id}" name="disponibilitaId" value="${slot.id}" required>
	                            <label for="slot_${slot.id}">${slot.dataOraInizio.toLocalTime()}</label>
	                        </div>
	                        
	                    </c:forEach>
	                </div>
	            </div>
	        </c:forEach>
	    </div>
	    <button type="submit">Conferma Prenotazione</button>
	</form>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>