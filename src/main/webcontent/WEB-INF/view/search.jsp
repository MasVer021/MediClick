<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<jsp:include page="/WEB-INF/view/layout/header.jsp" />

		<main class="mc-container mc-p-lg">

			<c:choose>
				<c:when test="${not empty medici}">
					<div class="mc-search-layout">
						<aside class="mc-search-layout__sidebar">
							<div class="mc-card mc-card--primary">
								<div class="mc-card__header mc-text-center">
									<h2 class="mc-card__title">La tua salute in un click</h2>
									<p class="mc-card__subtitle">Affina la tua ricerca</p>
								</div>

								<form method="get" action="<c:url value='/search'/>"
									  class="mc-card__body mc-flex-col mc-gap-md">
									<div class="mc-form-group">
										<label class="mc-label" for="specialista">Seleziona la categoria</label>
										<select class="mc-select" id="specialista" name="specialista">
											<option value="" disabled selected>Categoria</option>
											<c:forEach items="${categorie}" var="c">
												<option value="${c.id}"> ${c.nome}</option>
											</c:forEach>
										</select>
									</div>

									<div class="mc-form-group" id="medici-input-search-group">
										<label class="mc-label" for="querySpecialista">Nome Specialista</label>
										<input class="mc-input" type="search" placeholder="Es. Mario Rossi"
											   id="querySpecialista" name="querySpecialista" autocomplete="off">

									</div>

									<div class="mc-form-group" id="citta-input-search-group">
										<label class="mc-label" for="citta">Citt&agrave;</label>
										<input class="mc-input" type="text" id="citta" name="citta" placeholder="Roma"
											   autocomplete="off">
									</div>

									<button class="mc-btn mc-btn--secondary mc-btn--block" type="submit">Aggiorna
										Ricerca</button>
								</form>
							</div>
						</aside>

						<section class="mc-search-layout__results">
							<div class="mc-grid mc-grid-3 mc-gap-md">
								<c:forEach items="${medici}" var="m">

									<div class="mc-card mc-card--primary mc-p-sm mc-justify-between">
										<div class="mc-card__avatar-box">
											<c:choose>
												<c:when test="${not empty m.medico.fotoprofiloBase64}">
													<img class="mc-image-preview"
														 src="data:image/jpeg;base64,${m.medico.fotoprofiloBase64}"
														 alt="Foto Dr. ${m.medico.nome} ${m.medico.cognome}" />
												</c:when>
												<c:otherwise>
													<div class="medico-avatar-placeholder">
														<span>${m.medico.nome.substring(0,1)}${m.medico.cognome.substring(0,1)}</span>
													</div>
												</c:otherwise>
											</c:choose>
										</div>

										<div class="mc-card__body mc-p-md mc-flex-col mc-gap-sm mc-justify-between">
											<div>
												<h2 class="mc-text-bold">Dr. ${m.medico.nome} ${m.medico.cognome}</h2>

												<div class="mc-flex-row mc-justify-between mc-align-center mc-mb-sm">
													<span class="mc-badge mc-badge--info">${m.categoria.nome}</span>
													<span class="mc-text-bold">&#9733; ${m.valoreRecensioni}/5
														(${m.numeroRecensioni})</span>
												</div>

												<p class="mc-text-muted mc-text-sm">${m.indirizzo}</p>
											</div>

											<div class="mc-card__highlight-box">
												<c:choose>
													<c:when test="${not empty m.primaDisponibilita}">
														<p class="mc-text-bold">Prima disponibilit&agrave;:
															${m.primaDisponibilita}</p>
													</c:when>
													<c:otherwise>
														<p class="mc-text-muted">Nessuna disponibilit&agrave; imminente
														</p>
													</c:otherwise>
												</c:choose>
											</div>

											<a href="<c:url value='/profiloMedico?id=${m.medico.id}'/>"
											   class="mc-btn mc-btn--primary mc-btn--block mc-mt-sm">
												Prenota - ${m.costo} &euro;
											</a>
										</div>
									</div>

								</c:forEach>
							</div>
						</section>

					</div>
				</c:when>

				<c:otherwise>
					<div class="mc-card mc-card--small mc-card--primary">
						<div class="mc-card__header mc-text-center">
							<h2 class="mc-card__title">La tua salute in un click</h2>
							<p class="mc-card__subtitle">Cerca lo specialista pi&ugrave; adatto a te</p>
						</div>

						<form method="get" action="<c:url value='/search'/>"
							  class="mc-card__body mc-flex-col mc-gap-md">
							<div class="mc-form-group">
								<label class="mc-label" for="specialista">Seleziona la categoria</label>
								<select class="mc-select" id="specialista" name="specialista">
									<option value="" disabled selected>Categoria</option>
									<c:forEach items="${categorie}" var="c">
										<option value="${c.id}"> ${c.nome}</option>
									</c:forEach>
								</select>
							</div>

							<div class="mc-form-group" id="medici-input-search-group">
								<label class="mc-label" for="querySpecialista">Nome Specialista</label>
								<input class="mc-input" type="search" placeholder="Es. Mario Rossi"
									   id="querySpecialista" name="querySpecialista" autocomplete="off">
							</div>


							<div class="mc-form-group" id="citta-input-search-group">
								<label class="mc-label" for="citta">Citt&agrave;</label>
								<input class="mc-input" type="text" id="citta" name="citta" placeholder="Roma"
									   list="citta-suggest" autocomplete="off">
							</div>

							<button class="mc-btn mc-btn--secondary mc-btn--block" type="submit">Cerca</button>
						</form>
					</div>
				</c:otherwise>

			</c:choose>

		</main>


		<script src="${pageContext.request.contextPath}/js/validation-utils.js"></script>
		<script src="${pageContext.request.contextPath}/js/search.js"></script>
		<jsp:include page="/WEB-INF/view/layout/footer.jsp" />