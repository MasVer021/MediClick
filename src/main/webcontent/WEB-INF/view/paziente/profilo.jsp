<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div>
		<p>${paziente.nome}</p>
		<p>${paziente.cognome}</p>
		<p>${paziente.utente.email}</p>
		
		<c:if test="${not modificaTelefono}">
	    	<p>Telefono: ${paziente.telefono} 
	       <a href="?edit=telefono">[Modifica]</a>
	    </p>
		</c:if>
	
		<c:if test="${modificaTelefono}">
	    	<form action="${pageContext.request.contextPath}/paziente/profilo?edit=telefono" method="post">
	        	Telefono: <input type="text" name="nuovoTelefono" value="${paziente.telefono}">
	        	<button type="submit">Salva</button>
	        	<a href="?">Annulla</a>
	    	</form>
		</c:if>
		
		<c:if test="${not modificaPassword}">
	    	<p>Password:  
	       		<a href="?edit=password">[Modifica]</a>
	    	</p>
		</c:if>
		<c:if test="${modificaPassword}">
	    	<form action="${pageContext.request.contextPath}/paziente/profilo?edit=password" method="post">
	        	Vecchia password: <input type="password" name="attualePassword">
	        	Nuova password: <input type="password" name="nuovaPassword">
	        	Conferma nuova password: <input type="password" name="confermaPassword">
	        	<button type="submit">Salva</button>
	        	<a href="?">Annulla</a>
	    	</form>
		</c:if>
		
		<p>${paziente.codiceFiscale}</p>
		<p>${paziente.dataNascita}</p>
		
		<c:if test="${msg != null}">
	    	<p>${msg}</p>
		</c:if>
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>