<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>

      <c:import url="./genericHeader.jsp" />
      <script src="./web/js/checkout.js"></script>

      <title>Musica para DAA</title>
    </head>
    <body>

      <form action="./index.jsp" method="POST" id="form" >
        <div class="demo-card-wide mdl-card mdl-shadow--2dp">
          <div class="mdl-card__title" id="cabeceraTarxetaInicio">
            <h2 class="mdl-card__title-text">Confirmación de Compra</h2>
          </div>

          <!--contido tarxeta-->

          <table class="mdl-data-table mdl-js-data-table">
            <thead>
              <tr>
                <th class="mdl-data-table__cell--non-numeric">CD</th>
                <th>Cantidad</th>
                <th>Precio Unitario</th>
                <th>Precio Total</th>
              </tr>
            </thead>
            <tbody>
                    <fmt:setLocale value="es_ES"/>
                    <c:forEach var="user" items="${sessionScope.cart}">

                        <tr>
                            <td class="text-align-left">${user.value.getTitle()}</td>
                            <td>${user.value.getQuantity()}</td>
                            <td>${user.value.getUnitaryPrice()}&euro;</td>
                            <td><fmt:formatNumber value="${user.value.getUnitaryPrice() * user.value.getQuantity()}" type="currency"/></td>
                                <c:set var="total" value="${total + user.value.getUnitaryPrice() * user.value.getQuantity()}"></c:set>
                        </tr>

                    </c:forEach>

            </tbody>
            <tfoot>

                <tr>
                  <th class="mdl-data-table__cell--non-numeric">Precio Sin Iva:</th>
                  <th></th>
                  <th></th>
                  <th><fmt:formatNumber value="${total}" type="currency"/></th>

                </tr>
                <c:choose>
                    <c:when test="${sessionScope.usuario.isVip()}">
                        <tr>
                            <th class="mdl-data-table__cell--non-numeric">Descuento VIP(20%):</th>
                            <th></th>
                            <th></th>
                            <th><fmt:formatNumber value="${total * 0.8}" type="currency"/></th>
                            <th></th>
                        </tr>
                        <tr>
                            <th class="mdl-data-table__cell--non-numeric">IVA(21%):</th>
                            <th></th>
                            <th></th>
                            <th><fmt:formatNumber value="${total * 21 / 100}" type="currency"/></th>
                            <th></th>
                        </tr>
                        <tr>
                            <th class="mdl-data-table__cell--non-numeric">Precio Total:</th>
                            <th></th>
                            <th></th>
                            <th><fmt:formatNumber value="${total * 0.8 + total * 21 / 100}" type="currency"/></th>
                            <th></th>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <th class="mdl-data-table__cell--non-numeric">IVA(21%):</th>
                            <th></th>
                            <th></th>
                            <th><fmt:formatNumber value="${total * 21 / 100}" type="currency"/></th>
                            <th></th>
                        </tr>
                        <tr>
                            <th class="mdl-data-table__cell--non-numeric">Precio Total:</th>
                            <th></th>
                            <th></th>
                            <th><fmt:formatNumber value="${total + total * 21 / 100}" type="currency"/></th>
                            <th></th>
                        </tr>
                    </c:otherwise>
                </c:choose>


                </tr>

            </tfoot>
          </table>



               <input type="hidden" name="action" id="action" value="confirmPayment">
            <!--fin contido tarxeta-->

          <div class="mdl-card__actions mdl-card--border">
            <input type="button" value="Volver" id="volver" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect">
            <input type="submit" value="Continuar" class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" style="float:right;">
          </div>
          <c:import url="./menu.jsp" />
        </div>
      </form>
    </body>
</html>