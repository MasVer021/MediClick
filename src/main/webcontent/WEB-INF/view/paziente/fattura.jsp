<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<jsp:include page="/WEB-INF/view/layout/header.jsp" />
	<main class="mc-container mc-container-ver mc-justify-between mc-white-bg">
		<c:if test="${not empty prenotazione}">
			<div class="mc-flex-row mc-justify-between">

				<img src="${pageContext.request.contextPath}/img/ColFullLogo.svg" alt="Logo completo colorato">
				<div>
					<h1>Fattura</h1>

					<h2>
						<c:out value="${prenotazione.idTransazioneEsterno}"></c:out>
					</h2>
					<h2>
						<c:out value="${prenotazione.dataPagamentoFormattata('dd/MM/yyyy HH:mm')}"></c:out>
					</h2>
				</div>

			</div>

			<div class="mc-flex-row mc-justify-around">
				<div class="mc-flex-col mc-justify-around mc-gap-md">
					<h3 class="mc-text-bold mc-font-lg">Dati Medico:</h3>
					<p>Dott.
						<c:out
							   value="${prenotazione.erogazionePrestazione.medico.cognome} ${prenotazione.erogazionePrestazione.medico.nome}" />
					</p>
					<p>P. IVA:
						<c:out value="${prenotazione.erogazionePrestazione.medico.pIva}" />
					</p>
					<p>Regime Fiscale:
						<c:out value="${prenotazione.erogazionePrestazione.medico.regimeFiscale.nome}" />
					</p>

				</div>
				<div class="mc-flex-col mc-justify-around mc-gap-md">
					<h3 class="mc-text-bold mc-font-lg">Dati Paziente:</h3>
					<p>
						<c:out value="${prenotazione.paziente.cognome} ${prenotazione.paziente.nome}" />
					</p>
					<p>Codice Fiscale:
						<c:out value="${prenotazione.paziente.codiceFiscale}" />
					</p>
					<p>Telefono:
						<c:out value="${prenotazione.paziente.telefono}" />
					</p>
				</div>
			</div>
			<div class="mc-flex-col mc-justify-around mc-gap-md">
				<h3 class="mc-text-bold mc-font-lg">Dettagli Prestazione:</h3>
				<p>Visita:
					<c:out value="${prenotazione.erogazionePrestazione.catalogoPrestazioni.nome}" />
				</p>
				<p>Presso:
					<c:out
						   value="${prenotazione.erogazionePrestazione.studio.nomeSede} - ${prenotazione.erogazionePrestazione.studio.indirizzoMaps}, ${prenotazione.erogazionePrestazione.studio.citta}" />
				</p>
			</div>
			<div class="mc-flex-col mc-justify-around mc-gap-md">
				<h3 class="mc-text-bold mc-font-lg">Riepilogo Pagamento:</h3>
				<p>Importo Pagato: &euro;
					<c:out value="${prenotazione.importoPagato}" />
				</p>
				<c:if test="${not empty prenotazione.codiceSconto}">
					<p>Codice sconto utilizzato:
						<c:out
							   value="${prenotazione.codiceSconto.codice}(${prenotazione.codiceSconto.valorePercentuale}%)" />
					</p>
				</c:if>
			</div>
			<div class="mc-flex-col mc-justify-around mc-gap-md">
				<p>Prestazione sanitaria esente da IVA ai sensi dell'art. 10, comma 1, n. 18, del D.P.R. n. 633/1972.
				</p>
			</div>
			<button onclick="window.print()" class="no-print mc-btn mc-btn--primary mc-mb-md">Stampa</button>
		</c:if>
	</main>
	<jsp:include page="/WEB-INF/view/layout/footer.jsp" />