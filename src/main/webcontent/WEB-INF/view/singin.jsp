<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>
<main class="mc-container">
    <div class="mc-card mc-card--small mc-card--primary mc-card--generic">
        <form method="post" id ="registrazioneForm" action="${pageContext.request.contextPath}/singin?medico=${isMedico}" enctype="multipart/form-data" class="mc-card__body">


            <div class="mc-form-group">
                <label class="mc-label" for="email">Indirizzo Email</label>
                <input class="mc-input" type="email" id="email" name="email" placeholder="esempio@email.com" required>
            </div>


            <div class="mc-form-group">
                <label class="mc-label" for="password">Password</label>
                <input class="mc-input" type="password" id="password" name="password" placeholder="Scegli una password sicura" required>
            </div>


            <div class="mc-form-group">
                <label class="mc-label" for="passwordRipetuta">Conferma Password</label>
                <input class="mc-input" type="password" id="passwordRipetuta" name="passwordRipetuta" placeholder="Ripeti la password" required>
            </div>


            <div class="mc-form-group">
                <label class="mc-label" for="nome">Nome</label>
                <input class="mc-input" type="text" id="nome" name="nome" placeholder="Inserisci il tuo nome" required>
            </div>


            <div class="mc-form-group">
                <label class="mc-label" for="cognome">Cognome</label>
                <input class="mc-input" type="text" id="cognome" name="cognome" placeholder="Inserisci il tuo cognome" required>
            </div>


            <c:if test="${isMedico}">

                <div class="mc-form-group">
                    <label class="mc-label" for="Bio">Biografia Professionale</label>
                    <textarea class="mc-textarea" id="Bio" name="Bio" rows="4" placeholder="Parlaci brevemente della tua esperienza medica..."></textarea>
                </div>

                <div class="mc-form-group">
                    <label class="mc-label" for="PIva">Partita IVA</label>
                    <input class="mc-input" type="text" id="PIva" name="PIva" placeholder="11 cifre numeriche">
                </div>

                <div class="mc-form-group">
                    <label class="mc-label" for="RegimeFiscale">Regime Fiscale</label>
                    <select class="mc-select" id="RegimeFiscale" name="RegimeFiscale">
                        <option value="" disabled selected>Seleziona il regime fiscale</option>
                        <c:forEach items="${regimiFiscali}" var="regimeFiscale">
                            <option value="${regimeFiscale.id}">${regimeFiscale.nome}</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="mc-form-group">
                    <label class="mc-label">Foto Profilo (opzionale)</label>
                    <div class="mc-photo-upload">
                        <img id="preview-foto" class="mc-image-preview" src="#" alt="Anteprima foto" Style="display: none">

                        <label for="fotoprofilo" class="mc-btn-upload">Scegli Foto Profilo</label>
                        <input type="file" id="fotoprofilo" name="fotoprofilo" class="mc-file-input" accept="image/*">
                    </div>
                </div>
            </c:if>

            <c:if test="${!isMedico}">
                <div class="mc-form-group">
                    <label class="mc-label" for="CF">Codice Fiscale</label>
                    <input class="mc-input" type="text" id="CF" name="CF" placeholder="Codice Fiscale a 16 caratteri">
                </div>

                <div class="mc-form-group">
                    <label class="mc-label" for="telefono">Numero di Telefono</label>
                    <input class="mc-input" type="tel" id="telefono" name="telefono" placeholder="+39 333 1234567">
                </div>

                <div class="mc-form-group">
                    <label class="mc-label" for="DataNascita">Data di Nascita</label>
                    <input class="mc-input" type="date" id="DataNascita" name="DataNascita">
                </div>
            </c:if>

            <button type="submit" class="mc-btn mc-btn--primary mc-btn--block">
                Registrati
            </button>

            <div class="mc-medico-paziente">
                <c:if test="${!isMedico}">
                    <a href="${pageContext.request.contextPath}/singin?medico=true" class="mc-link">
                        Sei un professionista? Registrati qui
                    </a>
                </c:if>
                <c:if test="${isMedico}">
                    <a href="${pageContext.request.contextPath}/singin" class="mc-link">
                        Sei un paziente? Registrati qui
                    </a>
                </c:if>
            </div>
            
        </form>
    </div>

</main>
<script src="${pageContext.request.contextPath}/js/preview-foto.js"></script> 
<script src="${pageContext.request.contextPath}/js/validation-utils.js"></script>
<script src="${pageContext.request.contextPath}/js/singin.js"></script> 
<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>