<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main class="mc-container mc-container-ver mc-mb-xl">

	<div class="mc-card mc-card--generic mc-card--xlarge">
		 <div class="mc-card__header ">
		 	<h2 class="mc-card__title">Documenti Caricati</h2>
		 </div>
		 
	    <section class=" mc-card__body mc-table-container">
	        <table class="mc-table mc-table--zebra">
	            <thead>
	                <tr>
	                    <th>Nome Documento</th>
	                    <th>Tipo Certificato</th>
	                    <th>Obbligatorio</th>
	                    <th>Data Caricamento</th>
	                    <th>Data Scadenza</th>
	                    <th>Stato</th>
	                    <th class="mc-text-right">Azione</th>
	                </tr>
	            </thead>
	            <tbody>
	                <c:forEach items="${certificatiCaricati}" var="cert">
	                    <tr>
	                        <td><span class="mc-text-bold">${cert.nomeFile}</span></td>
	                        <td>${cert.tipoCertificato.nome}</td>
	                        <td>
	                            <c:choose>
	                                <c:when test="${cert.tipoCertificato.obbligatorio}">
	                                    <span class="mc-badge mc-badge--danger">Sì</span>
	                                </c:when>
	                                <c:otherwise>
	                                    <span class="mc-badge mc-badge--secondary">No</span>
	                                </c:otherwise>
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
	                                    <span class="mc-text-muted"><i>Nessuna Scadenza</i></span>
	                                </c:otherwise>
	                            </c:choose>
	                        </td>
	                        <td>
	                            <c:choose>
	                                <c:when test="${cert.stato == 'APPROVATO'}">
	                                    <span class="mc-badge mc-badge--success">Approvato</span>
	                                </c:when>
	                                <c:when test="${cert.stato == 'RIFIUTATO'}">
	                                    <span class="mc-badge mc-badge--danger">Rifiutato</span>
	                                </c:when>
	                                <c:when test="${cert.stato == 'SCADUTO'}">
	                                    <span class="mc-badge mc-badge--warning">Scaduto</span>
	                                </c:when>
	                                <c:otherwise>
	                                    <span class="mc-badge mc-badge--secondary">In revisione</span>
	                                </c:otherwise>
	                            </c:choose>
	                        </td>
	                        <td>
	                            <div class="mc-table-action-cell">
	                                <form action="${pageContext.request.contextPath}/medico/certificati" method="POST">
	                                    <input type="hidden" name="action" value="elimina-certificato">
	                                    <input type="hidden" name="certificatoId" value="${cert.id}">
	                                    <button class="mc-btn mc-btn--danger mc-btn--sm" type="submit" onclick="return confirm('Sei sicuro di voler eliminare questo certificato?');">
	                                        Elimina
	                                    </button>
	                                </form>
	                            </div>
	                        </td>
	                    </tr>
	                </c:forEach>
	                <c:if test="${empty certificatiCaricati}">
	                    <tr>
	                        <td colspan="7" class="mc-text-center mc-text-muted mc-p-lg">
	                            Nessun certificato caricato finora.
	                        </td>
	                    </tr>
	                </c:if>
	            </tbody>
	        </table>
	    </section>
	</div>
	
	<div class="mc-card mc-card--generic mc-card--xlarge">
		 <div class="mc-card__header ">
		 	<h2 class="mc-card__title">Carica documento</h2>
		 </div>
		<div class="mc-card__body">
			 <section>
		        <form action="${pageContext.request.contextPath}/medico/certificati" method="POST" enctype="multipart/form-data">
		            <input type="hidden" name="action" value="carica-certificato">
		            
		            <div class="mc-form-group">
	                    <label class="mc-label" for="tipoCertificatoId">Seleziona il tipo di certificato:</label>
	                     <select class="mc-select" id="tipoCertificatoId" name="tipoCertificatoId" required>
		                    <option value="">-- Seleziona Tipo --</option>
		                    <c:forEach items="${tipiCertificato}"  var="tc" >
		                        <option value="${tc.id}">${tc.nome} ${tc.obbligatorio ? '(Obbligatorio)' : ''}</option>
		                    </c:forEach>
		                </select>
	                </div>
	                
	                <div class="mc-form-group">
					    <label class="mc-label">Seleziona il file (PDF max 5MB)</label>
					    <div class="mc-photo-upload" style="margin: 0.5rem 0; align-items: flex-start; gap: 0.5rem;">
					        <label for="documento" class="mc-btn-upload">Scegli File PDF</label>
					       
					        <input type="file" id="documento" name="documento" class="mc-file-input" accept="application/pdf" required>
					     
					    </div>
					</div>
	                
	                <div class="mc-form-group">
	                    <label class="mc-label" for="dataScadenza">Data di Scadenza (se prevista)</label>
	                    <input class="mc-input" type="date" id="dataScadenza" name="dataScadenza">
	                </div>
		            
		            <button class = "mc-btn mc-btn--outline" type="submit">Carica Documento</button>
		        </form>
	    	</section>
		</div>
	</div>

    
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>