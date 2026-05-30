<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main class="mc-container mc-container-ver mc-mb-xl">
	<div class="mc-card mc-card--generic">
			<div class="mc-card__header ">
		        <h2 class="mc-card__title">Dati Personali e Fiscali</h2>
		    </div>
			<div class="mc-card__body">
				<form action="${pageContext.request.contextPath}/medico/profilo" method="POST">
		            <input type="hidden" name="action" value="salva-dati">
		            
		            <div class="mc-form-group">
						<label class="mc-label" for="nome">Nome</label>
						<input class="mc-input" type="text" id="nome" name="nome" value="${medico.nome}" required>
					</div>
					<div class="mc-form-group">
						<label class="mc-label" for="cognome">Cognome</label>
						<input class="mc-input"  type="text" id="cognome" name="cognome" value="${medico.cognome}" required>
					</div>
					<div class="mc-form-group">
						<label class="mc-label" for="pIva">Partita IVA</label>
						<input class="mc-input" type="text" id="pIva" name="pIva" value="${medico.pIva}" required maxlength="11">
					</div>
					<div class="mc-form-group">
						<label class="mc-label" for="regimeFiscaleId">Regime Fiscale</label>
						<select class="mc-select" id="regimeFiscaleId" name="regimeFiscaleId" required>
		                    <option value="">-- Seleziona Regime --</option>
		                    <c:forEach var="regime" items="${regimi}">
		                        <option value="${regime.id}" ${regime.id == medico.regimeFiscaleId ? 'selected' : ''}>
		                            ${regime.nome} (${regime.aliquotaDefault}%)
		                        </option>
		                    </c:forEach>
		                </select>
					</div>
					<div class="mc-form-group">
						<label class="mc-label" for="bio">Biografia Professionale</label>
						<textarea class="mc-textarea" id="bio" name="bio" rows="4" cols="50">${medico.bio}</textarea>
					</div>
					
		            <button class="mc-btn mc-btn--outline mc-btn--block" type="submit">Salva Modifiche</button>
					<div>
			            <c:choose>
							<c:when test="${not empty medico.fotoprofilo}">
								<img class="medico-avatar" src="data:image/jpeg;base64,${medico.fotoprofiloBase64}" alt="Foto Dr. ${medico.nome} ${medico.cognome}" />
							</c:when>
							<c:otherwise>
								<div class="medico-avatar-placeholder">
									<span>${medico.nome.substring(0,1)}${medico.cognome.substring(0,1)}</span>
								</div>
							</c:otherwise>
						</c:choose>
			        </div>
			        </form>
			        <form action="${pageContext.request.contextPath}/medico/profilo" method="POST" enctype="multipart/form-data">
			            <input type="hidden" name="action" value="carica-foto">
			           <div class="mc-form-group">
		                    <label class="mc-label">Foto Profilo</label>
		                    <div class="mc-photo-upload">
		                        <img id="preview-foto" class="mc-image-preview" src="#" alt="Anteprima foto" Style="display: none">
		
		                        <label for="fotoprofilo" class="mc-btn-upload">Scegli Foto Profilo</label>
		                        <input type="file" id="fotoprofilo" name="fotoprofilo" class="mc-file-input" accept="image/*">
		                    </div>
	                	</div>
			            <button class="mc-btn mc-btn--outline mc-btn--block" type="submit">Carica Foto</button>
			        </form>      
		</div>
	</div>
	<div class="mc-card mc-card--generic">
		<div class="mc-card__header ">
	        <h2 class="mc-card__title">Studi e Prestazioni erogate</h2>
	    </div>
		<div class="mc-card__body  mc-table-container">
			<table class="mc-table mc-table--zebra">
            <thead>
                <tr>
                    <th>Studio (Sede)</th>
                    <th>Prestazione</th>
                    <th>Prezzo di Listino</th>
                    <th>Stato</th>
                    <th>Azione</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="erogazione" items="${miePrestazioni}">
                    <tr>
                        <td>${erogazione.studio.nomeSede} - ${erogazione.studio.indirizzoMaps}</td>
                        <td>${erogazione.catalogoPrestazioni.nome}</td>
                        <td><fmt:formatNumber value="${erogazione.prezzoLordoListino}" type="currency" currencySymbol="&euro;"/></td>
                        <td>${erogazione.stato.label}</td>
                        <td>
                            <form action="${pageContext.request.contextPath}/medico/profilo" method="POST" style="display:inline;">
                               
                                <input type="hidden" name="erogazioneId" value="${erogazione.id}">
                                <c:if test="${erogazione.stato.label == 'Attiva'}">
                                 	<input type="hidden" name="action" value="rimuovi-prestazione">
				                    <button class="mc-btn mc-btn--danger" type="submit">Sospendi</button>
				                </c:if>
				                <c:if test="${erogazione.stato.label == 'Sospesa'}">
				                	<input type="hidden" name="action" value="attiva-prestazione">
				                    <button class="mc-btn mc-btn--success" type="submit">Attiva</button>
				                </c:if>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty miePrestazioni}">
                    <tr>
                        <td colspan="5" align="center">Non hai ancora associato nessuna prestazione.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>      
		</div>
		<div class="mc-card__footer mc-flex-col">
		
	        <h2 >Aggiungi una nuova prestazione in uno studio</h2>
	    
			<form action="${pageContext.request.contextPath}/medico/profilo" method="POST">
	            <input type="hidden" name="action" value="associa-prestazione">
	            
	            <div class="mc-form-group">
						<label class="mc-label" for="studioId">Seleziona lo Studio (Sede)</label>
						<select class="mc-select" id="studioId" name="studioId" required>
		                    <option value="">-- Seleziona Studio --</option>
		                    <c:forEach var="studio" items="${studi}">
		                        <option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
		                    </c:forEach>
		                </select>
				</div>
				
				<div class="mc-form-group">
						<label class="mc-label" for="catalogoId">Seleziona la Prestazione</label>
						<select class="mc-select" id="catalogoId" name="catalogoId" required>
		                    <option value="">-- Seleziona Prestazione --</option>
		                    <c:forEach var="cp" items="${prestazioniCatalogo}">
		                        <option value="${cp.id}">${cp.nome}</option>
		                    </c:forEach>
		                </select>
				</div>
				
				<div class="mc-form-group">
						<label class="mc-label" for="durata">Durata della visita (minuti)</label>
						<input class="mc-input" type="number" id="durata" name="durata" min="30" step="30" placeholder="30" required>
				</div>
				
				<div class="mc-form-group">
					<label class="mc-label" for="prezzo">Prezzo Lordo (&euro;)</label>
					<input  class="mc-input" type="number" id="prezzo" name="prezzo" step="0.01" min="0" placeholder="Es. 80.00" required>
				</div>
	           
	            <button class="mc-btn mc-btn--outline mc-btn--block" type="submit">Associa Prestazione</button>
	        </form>
		</div>
	</div>  
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>