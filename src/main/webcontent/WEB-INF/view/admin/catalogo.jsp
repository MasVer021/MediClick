<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />

	<main class="mc-container mc-container-ver mc-mb-xl">
		<div class="mc-mb-md">
			<h1 class="mc-font-xl mc-text-bold">Gestione Catalogo</h1>
			<p class="mc-text-muted">Inserisci e amministra le prestazioni sanitarie e le rispettive categorie mediche.
			</p>
		</div>

		<div class="mc-grid mc-grid-2">
			<div class="mc-card mc-card--generic">
				<div class="mc-card__header">
					<h3 class="mc-card__title">Aggiungi Categoria</h3>
				</div>
				<div class="mc-card__body">
					<form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
						<input type="hidden" name="action" value="aggiungiCategoria">

						<div class="mc-form-group">
							<label class="mc-label" for="nomeCategoria">Nome Categoria *</label>
							<input class="mc-input" type="text" id="nomeCategoria" name="nomeCategoria" required
								   placeholder="es. Cardiologia">
						</div>

						<button class="mc-btn mc-btn--outline mc-btn--block mc-mt-md" type="submit">Crea
							Categoria</button>
					</form>
				</div>
			</div>

			<div class="mc-card mc-card--generic">
				<div class="mc-card__header">
					<h3 class="mc-card__title">Aggiungi Prestazione</h3>
				</div>
				<div class="mc-card__body">
					<form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
						<input type="hidden" name="action" value="aggiungiPrestazione">

						<div class="mc-form-group">
							<label class="mc-label" for="categoriaId">Categoria Associata *</label>
							<select class="mc-select" id="categoriaId" name="categoriaId" required>
								<option value="">Seleziona una categoria...</option>
								<c:forEach items="${categorie}" var="cat">
									<option value="${cat.id}">
										<c:out value="${cat.nome}" />
									</option>
								</c:forEach>
							</select>
						</div>

						<div class="mc-form-group">
							<label class="mc-label" for="nomePrestazione">Nome Prestazione *</label>
							<input class="mc-input" type="text" id="nomePrestazione" name="nomePrestazione" required
								   placeholder="es. Visita Cardiologica">
						</div>

						<div class="mc-form-group">
							<label class="mc-label" for="descrizione">Descrizione</label>
							<textarea class="mc-textarea" id="descrizione" name="descrizione"
									  placeholder="Inserisci una descrizione..."></textarea>
						</div>

						<button class="mc-btn mc-btn--outline mc-btn--block mc-mt-md" type="submit">Crea
							Prestazione</button>
					</form>
				</div>
			</div>
		</div>

		<div class="mc-card mc-card--generic mc-mt-lg">
			<div class="mc-card__header">
				<h3 class="mc-card__title">Prestazioni nel Catalogo</h3>
			</div>
			<div class="mc-card__body mc-table-container">
				<table class="mc-table mc-table--zebra">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Categoria</th>
							<th>Descrizione</th>
							<th>Stato</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${prestazioni}" var="pr">
							<tr>
								<td><span class="mc-text-bold">
										<c:out value="${pr.nome}" />
									</span></td>
								<td>
									<c:out value="${pr.categoria.nome}" />
								</td>
								<td>
									<c:out value="${pr.descrizione}" />
								</td>
								<td>
									<span
										  class="mc-badge ${pr.stato.label == 'Attiva' || pr.stato.label == 'Attivo' ? 'mc-badge--success' : 'mc-badge--secondary'}">
										<c:out value="${pr.stato.label}" />
									</span>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty prestazioni}">
							<tr>
								<td colspan="4" class="mc-text-center mc-text-muted mc-p-lg">Nessuna prestazione
									presente nel catalogo.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>

		<div class="mc-card mc-card--generic mc-mt-lg">
			<div class="mc-card__header">
				<h3 class="mc-card__title">Categorie Mediche</h3>
			</div>
			<div class="mc-card__body mc-table-container">
				<table class="mc-table mc-table--zebra">
					<thead>
						<tr>
							<th>ID Categoria</th>
							<th>Nome Categoria</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${categorie}" var="c">
							<tr>
								<td>
									<c:out value="${c.id}" />
								</td>
								<td><span class="mc-text-bold">
										<c:out value="${c.nome}" />
									</span></td>
							</tr>
						</c:forEach>
						<c:if test="${empty categorie}">
							<tr>
								<td colspan="2" class="mc-text-center mc-text-muted mc-p-lg">Nessuna categoria presente.
								</td>
							</tr>
						</c:if>
					</tbody>
				</table>
			</div>
		</div>
	</main>

	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />