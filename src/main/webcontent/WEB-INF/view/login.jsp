<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div class="card-login">
		<form  method="post" action='<%=response.encodeUrl(request.getContextPath() +"/login")%>'>
			<input type="email" name="email" placeholder="Email" required>
			<input type="password" name="password" placeholder="Password" required>
			<button type="submit">Accedi</button>
		</form>
		${errore}
	</div>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>
 	
