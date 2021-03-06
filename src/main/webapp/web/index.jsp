<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>

    <c:import url="./genericHeader.jsp"/>

    <link rel="stylesheet" href="./web/css/escaparate.css">
    <script src="./web/js/index.js"></script>

    <title>Musica para DAA</title>
</head>
<body>

<form id="form" action="./index.jsp" method="POST">
    <div class="demo-card-wide mdl-card mdl-shadow--2dp">
        <div class="mdl-card__title" id="cabeceraTarxetaInicio">
            <h2 class="mdl-card__title-text">Welcome</h2>
        </div>

        <div class="mdl-card__actions mdl-card--border">

                <div class="mdl-textfield mdl-js-textfield mdl-textfield--floating-label">
                    <input class="mdl-textfield__input" type="text" id="searchFilter" name="searchFilter">
                    <label class="mdl-textfield__label" for="searchFilter">Buscar...</label>
                </div>
                <input type="button" value="Buscar" id="searchButton"
                       class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect right">

            <div class="mdl-layout-spacer"></div>
            <input type="button" value="Ver Carrito" id="verCarrito"
                   class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect right">
        </div>

        <div class="mdl-layout mdl-js-layout mdl-color--grey-100">
            <main class="mdl-layout__content">


                <!-- area de busqueda -->



                <div class="mdl-grid">
                    <!--contido tarxeta-->


                    <c:forEach var="user" items="${requestScope.cds}">

                        <div class="item mdl-card mdl-cell mdl-cell--6-col mdl-cell--4-col-tablet mdl-shadow--2dp">
                            <figure class="mdl-card__media">
                                <img src="${ user.value.getImage() }"
                                     alt=""/>
                            </figure>
                            <div class="mdl-card__title">
                                <h1 class="mdl-card__title-text titulo_disco">${user.value.getTitle()} </h1>
                                <h1 class="mdl-card__subtitle-text">${user.value.getUnitaryPrice()}€</h1>
                            </div>
                            <div class="mdl-card__supporting-text ellipsis">
                                <p>${user.value.getDescription()}</p>
                            </div>
                            <div class="mdl-card__actions mdl-card--border">
                                <a id="a${user.value.getId()}" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect boton-info">Información</a>
                                <div class="mdl-layout-spacer"></div>
                                <button type="button"
                                        class="mdl-button mdl-button--icon mdl-button--colored boton-compra"
                                        id="${user.value.getId()}"><i class="material-icons">add_shopping_cart</i>
                                </button>
                            </div>
                        </div>

                        <%--<option value="${producto.value.getId()} | ${producto.value.getTitle()} | ${producto.value.getAuthor()} | ${producto.value.getCountry()} | ${producto.value.getUnitaryPrice()}"><c:out value="${producto.value.getTitle()} | ${producto.value.getAuthor()} | ${producto.value.getCountry()} | ${producto.value.getUnitaryPrice()}€" /> </option>--%>
                    </c:forEach>


                </div>
            </main>
        </div>


        <input type="hidden" name="product" id="product" value="-1">
        <input type="hidden" name="quantity" id="quantity" value="1">
        <input type="hidden" name="action" id="action" value="buyItem">

        <c:import url="./menu.jsp"/>




    </div>
</form>
</body>
</html>