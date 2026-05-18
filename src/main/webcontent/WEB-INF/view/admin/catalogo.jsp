<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="/WEB-INF/view/layout/header.jsp"/>

<main>
    <h1>Gestione Catalogo</h1>
    <p>Inserisci e amministra le prestazioni sanitarie e le rispettive categorie mediche.</p>
    <section>
        
        <div>
            <h3>Aggiungi Categoria</h3>
            <form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
                <input type="hidden" name="action" value="aggiungiCategoria">
                
                <div>
                    <label>Nome Categoria *</label>
                    <input type="text" name="nomeCategoria" required placeholder="es. Cardiologia">
                </div>
                
                <button type="submit">Crea Categoria</button>
            </form>
        </div>

       
        <div>
            <h3>Aggiungi Prestazione</h3>
            <form action="${pageContext.request.contextPath}/admin/catalogo" method="post">
                <input type="hidden" name="action" value="aggiungiPrestazione">
                
                <div>
                    <label>Categoria Associata *</label>
                    <select name="categoriaId" required>
                        <option value="">Seleziona una categoria...</option>
                        <c:forEach items="${categorie}" var="cat">
                            <option value="${cat.id}"><c:out value="${cat.nome}"/></option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label>Nome Prestazione *</label>
                    <input type="text" name="nomePrestazione" required placeholder="es. Visita Cardiologica">
                </div>

                <div>
                    <label>Descrizione</label>
                    <textarea name="descrizione" placeholder="Inserisci una descrizione..."></textarea>
                </div>
                
                <button type="submit">Crea Prestazione</button>
            </form>
        </div>
    </section>

   
    <section>
        <div>
            <h3>Prestazioni nel Catalogo</h3>
            <table>
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
                            <td><c:out value="${pr.nome}"/></td>
                            <td><c:out value="${pr.categoria.nome}"/></td>
                            <td><c:out value="${pr.descrizione}"/></td>
                            <td><c:out value="${pr.stato.label}"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty prestazioni}">
                        <tr>
                            <td colspan="4">Nessuna prestazione presente nel catalogo.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

        <div>
            <h3>Categorie Mediche</h3>
            <table>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nome Categoria</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${categorie}" var="c">
                        <tr>
                            <td><c:out value="${c.id}"/></td>
                            <td><c:out value="${c.nome}"/></td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty categorie}">
                        <tr>
                            <td colspan="2">Nessuna categoria presente.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>

    </section>
</main>

<jsp:include page="/WEB-INF/view/layout/footer.jsp"/>
