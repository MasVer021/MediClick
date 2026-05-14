<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main>
	<div class="card-login">
		<h1>La tua salute in un click</h1>
		<form  method= "get" action='<%=response.encodeUrl(request.getContextPath() +"/search")%>'>
			<select name="specialista">
			    <option value="" disabled selected>Quale categoria cerchi?</option>
			    <c:forEach items="${categorie}" var="c">
			        <option value="${c.id}">${c.nome}</option>
			    </c:forEach>
			</select>
			<input type="text" placeholder="nome o cognome specialista" name="querySpecialista">
			<input type="text" placeholder="Dove?" name="citta">
			<button type="submit">Cerca</button>
		</form>
	</div>
	
	<c:if test="${not empty medici}">
		<div class = "risultati">
			<c:forEach items="${medici}"  var="m">
				<div class="card-medico">
					<c:choose>
						<c:when test="${not empty m.medico.fotoprofilo}">
							<img class="medico-avatar" src="${m.medico.fotoprofilo}" alt="Foto Dr. ${m.medico.nome} ${m.medico.cognome}" />
						</c:when>
						<c:otherwise>
							<div class="medico-avatar-placeholder">
								<span>${m.medico.nome.substring(0,1)}${m.medico.cognome.substring(0,1)}</span>
							</div>
						</c:otherwise>
					</c:choose>
					<div class="card-body">
						<h2>Dr. ${m.medico.nome} ${m.medico.cognome}</h2>
					<p class="recensioni">${m.valoreRecensioni}/5 (${m.numeroRecensioni} recensioni)</p>
                	<p class="categoria">${m.categoria.nome}</p>
                	<div class="dettagli-prenotazione">
                    	<p>A partire da: <strong>${m.costo} </strong></p>
	                    <c:choose>
	                        <c:when test="${not empty m.primaDisponibilita}">
	                            <p>Prima disponibilita: ${m.primaDisponibilita}</p>
	                        </c:when>
	                        <c:otherwise>
	                            <p>Nessuna disponibilità imminente</p>
	                        </c:otherwise>
	                    </c:choose>
	                </div>
	                <p class="indirizzo">${m.indirizzo}</p>
	                <a href="${pageContext.request.contextPath}/profiloMedico?id=${m.medico.id}" class="btn-prenota">Prenota Visita - ${m.costo} &euro;</a>
					</div>
				</div>
			</c:forEach>
		</div>
	</c:if>
</main>
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>