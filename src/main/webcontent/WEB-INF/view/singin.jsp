<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<form method="post" action="${pageContext.request.contextPath}/singin?medico=${isMedico}" enctype="multipart/form-data">
	
    	<input type="email" name="email" placeholder="Email" required>
        <input type="password" name="password" placeholder="Password" required>
        <input type="password" name="passwordRipetuta" placeholder="Ripeti Password" required>
        <input type="text" name="nome" placeholder="Nome" required>
        <input type="text" name="cognome" placeholder="Cognome" required>
        <c:if test="${isMedico}">
	        <textarea id="Bio" name="Bio" rows="4" cols="50" placeholder="Biografia"></textarea>
	        <input type="text" name="PIva" placeholder="Partita Iva">
			<select id="RegimeFiscale" name="RegimeFiscale" class="form-select">
				 <option value="" disabled selected>Regime Fiscale</option>
				<c:forEach items="${regimiFiscali}"  var="regimeFiscale">
				 	<option  value="${regimeFiscale.id}">${regimeFiscale.nome}</option>
				</c:forEach>
			</select>
			<label for="fotoprofilo">Foto Profilo (opzionale):</label>
    		<input type="file" id="fotoprofilo" name="fotoprofilo" accept="image/*">
    		<img id="preview-foto" src="#" alt="Preview foto" style="display: none; max-width: 200px; margin-top: 10px; border-radius: 8px;">
		</c:if>
		<c:if test="${!isMedico}">
	        <input type="text" name="CF" placeholder="Inserisci Codice Fiscale">
	        <input type="tel" name="telefono" placeholder="Numero di telefono">
	        <input type="date" name="DataNascita" placeholder="DataNascita">
        </c:if>
        <button type="submit">Registrati</button>
        <c:if test="${!isMedico}">
        	<a href="${pageContext.request.contextPath}/singin?medico=true">Sei un professionista?</a>
        </c:if>
        <c:if test="${isMedico}">
    		<a href="${pageContext.request.contextPath}/singin">Sei un paziente?</a>
		</c:if>
    </form>
</main>
<script src="${pageContext.request.contextPath}/js/preview-foto.js"></script>  
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>