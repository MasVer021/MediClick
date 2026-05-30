<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main class="mc-container">
	<div class="mc-card mc-card--generic mc-card--medium">
	
		<div class="mc-flex-col mc-align-center mc-mb-xl">
		    <c:choose>
		        <c:when test="${not empty profiloMedico.medico.fotoprofilo}">
		            <img class="medico-avatar" src="data:image/jpeg;base64,${profiloMedico.medico.fotoprofiloBase64}" alt="Foto Dr. ${profiloMedico.medico.nome} ${profiloMedico.medico.cognome}" />
		        </c:when>
		        <c:otherwise>
		            <div class="medico-avatar-placeholder">
		                <span>${profiloMedico.medico.nome.substring(0,1)}${profiloMedico.medico.cognome.substring(0,1)}</span>
		            </div>
		        </c:otherwise>
		    </c:choose>
		    <h2 class="mc-card__title mc-mt-md">DR. ${profiloMedico.medico.cognome} ${profiloMedico.medico.nome}</h2>
		</div>
		
		<form method="get" action="${pageContext.request.contextPath}/paziente/prenotazione">
		
			<div class="mc-form-group">
			    <label class="mc-label">Studio Medico</label>
			    <select name="studio" class="mc-select">
			        <option value="" disabled selected>Seleziona lo Studio</option>
			        <c:forEach items="${profiloMedico.studi}" var="studio">
			            <option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
			        </c:forEach>
			    </select>
			</div>
			
			<div class="mc-form-group">
			    <label class="mc-label">Prestazione Richiesta</label>
			    <select name="prestazione" class="mc-select">
			        <option value="" disabled selected>Seleziona la Prestazione</option>
			        <c:forEach items="${profiloMedico.prestazioni}" var="prestazione">
			            <option value="${prestazione.id}">${prestazione.catalogoPrestazioni.nome}</option>
			        </c:forEach>
			    </select>
			</div>
			
			<div class="mc-mt-lg">
			    <h3 class="mc-card__title mc-mb-md">Scegli Data e Ora</h3>
			    <c:forEach items="${profiloMedico.disponibilitaRaggruppate}" var="entry">
			        <div class="blocco-giorno mc-mb-md">
			            <h4 class="mc-text-muted mc-mb-sm">${entry.key}</h4> 
			            <div class="pulsantiera-orari mc-flex-row mc-gap-sm">
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
		    
		    <button class = "mc-btn mc-btn--outline mc-btn--lg mc-mt-lg mc-btn--block " type="submit">Conferma Prenotazione</button>
		</form>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>