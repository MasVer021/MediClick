<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<main>
   <h2>Il Mio Profilo Medico</h2>
    <section>
        <h3>Dati Personali e Fiscali</h3>
        <form action="${pageContext.request.contextPath}/medico/profilo" method="POST">
            <input type="hidden" name="action" value="salva-dati">
            <p>
                <label for="nome">Nome:</label><br>
                <input type="text" id="nome" name="nome" value="${medico.nome}" required>
            </p>
            <p>
                <label for="cognome">Cognome:</label><br>
                <input type="text" id="cognome" name="cognome" value="${medico.cognome}" required>
            </p>
            <p>
                <label for="pIva">Partita IVA:</label><br>
                <input type="text" id="pIva" name="pIva" value="${medico.pIva}" required max="11">
            </p>
            <p>
                <label for="regimeFiscaleId">Regime Fiscale:</label><br>
                <select id="regimeFiscaleId" name="regimeFiscaleId" required>
                    <option value="">-- Seleziona Regime --</option>
                    <c:forEach var="regime" items="${regimi}">
                        <option value="${regime.id}" ${regime.id == medico.regimeFiscaleId ? 'selected' : ''}>
                            ${regime.nome} (${regime.aliquotaDefault}%)
                        </option>
                    </c:forEach>
                </select>
            </p>
            <p>
                <label for="bio">Biografia Professionale:</label><br>
                <textarea id="bio" name="bio" rows="4" cols="50">${medico.bio}</textarea>
            </p>
            <button type="submit">Salva Modifiche</button>
        </form>
    </section>
    <section>
        <h3>Foto Profilo</h3>
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
        <form action="${pageContext.request.contextPath}/medico/profilo" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="carica-foto">
            <p>
                <label for="foto">Seleziona una nuova foto (JPG/PNG max 2MB):</label><br>
                <input type="file" id="foto" name="foto" accept="image/jpeg, image/png" required>
            </p>
            <button type="submit">Carica Foto</button>
        </form>
    </section>
    <section>
        <h3>Studi e Prestazioni erogate</h3>
        
        <table border="1" cellpadding="5">
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
                                <input type="hidden" name="action" value="rimuovi-prestazione">
                                <input type="hidden" name="erogazioneId" value="${erogazione.id}">
                                <button type="submit" onclick="return confirm('Sicuro di voler rimuovere questa prestazione?');">Sospendi</button>
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
        <h4>Aggiungi una nuova prestazione in uno studio</h4>
        <form action="${pageContext.request.contextPath}/medico/profilo" method="POST">
            <input type="hidden" name="action" value="associa-prestazione">
            <p>
                <label for="studioId">Seleziona lo Studio (Sede):</label><br>
                <select id="studioId" name="studioId" required>
                    <option value="">-- Seleziona Studio --</option>
                    <c:forEach var="studio" items="${studi}">
                        <option value="${studio.id}">${studio.nomeSede} - ${studio.indirizzoMaps}</option>
                    </c:forEach>
                </select>
            </p>
            <p>
                <label for="catalogoId">Seleziona la Prestazione:</label><br>
                <select id="catalogoId" name="catalogoId" required>
                    <option value="">-- Seleziona Prestazione --</option>
                    <c:forEach var="cp" items="${prestazioniCatalogo}">
                        <option value="${cp.id}">${cp.nome}</option>
                    </c:forEach>
                </select>
            </p>
            <p>
			    <label for="durata">Durata della visita (minuti):</label><br>
			    <input type="number" id="durata" name="durata" min="30" step="30" placeholder="30" required>
			</p>
            <p>
                <label for="prezzo">Prezzo Lordo (&euro;):</label><br>
                <input type="number" id="prezzo" name="prezzo" step="0.01" min="0" placeholder="Es. 80.00" required>
            </p>
            <button type="submit">Associa Prestazione</button>
        </form>
    </section>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>