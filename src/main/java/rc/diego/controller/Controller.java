package rc.diego.controller;

import rc.diego.model.VO.VOCd;
import rc.diego.model.VO.VOComment;
import rc.diego.model.VO.VOShoppingCart;
import rc.diego.model.VO.VOUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by entakitos on 19/02/16.
 */
public class Controller extends CustomHttpServlet {

    private final String COMMENTS = "comments";
    private final String PARAMETER_ACTION = "action";
    private final String PARAMETER_PRODUCT = "product";
    private final String PARAMETER_QUANTITY = "quantity";
    private final String PARAMETER_ERROR = "error";

    private final String ACTION_SHOW_INDEX = "index";
    private final String ACTION_SHOW_PRODUCT_INFO = "productInfo";
    private final String ACTION_SHOW_SHOPPING_CART = "shoppingCart";
    private final String ACTION_SHOW_SIGN_IN = "signIn";
    private final String ACTION_SHOW_SIGN_UP = "signUp";
    private final String ACTION_BUY_ITEM = "buyItem";
    private final String ACTION_ERASE_ITEM = "eraseItem";
    private final String ACTION_UPDATE_ITEM = "updateItem";
    private final String ACTION_CHECKOUT = "checkout";
    private final String ACTION_SIGN_IN = "signInUser";
    private final String ACTION_SIGN_UP = "signUpUser";
    private final String ACTION_CONFIRM_PAYMENT = "confirmPayment";
    private final String ACTION_RESET = "reset";

    private final String ADMIN_ACTION_SHOW_STOCK = "admin/stock";
    private final String ADMIN_ACTION_EDIT_ITEM = "admin/edit";
    private final String ADMIN_ACTION_DELETE = "admin/delete";
    private final String ADMIN_ACTION_SAVE = "admin/save";

    private final String ADD_COMMENT = "addComment";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        VOShoppingCart shoppingCart;
        VOUser user;
        VOCd cd;
        int id;

        HttpSession session = req.getSession(false);

        if (session == null) { //crease unha sesion si non existe
            session = req.getSession(true);
            getTaskMapper().initializeSession(session);
        }

        try {
            System.out.println("El valor es " + req.getParameter(PARAMETER_ACTION));
            switch ((String) req.getParameter(PARAMETER_ACTION)) {
                case ACTION_SHOW_INDEX:
                    shoppingCart = getTaskMapper().getAllCds();
                    req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                    getViewManager().showIndex();
                    break;
                case ACTION_SHOW_PRODUCT_INFO:
                    ArrayList<VOComment> comments;

                    cd = new VOCd();
                    cd.setId(Integer.parseInt(req.getParameter(PARAMETER_PRODUCT)));

                    //Adding comments
                    comments = getTaskMapper().getAllComments(cd);
                    req.setAttribute(VOComment.COMMENTS,comments);

                    if (getTaskMapper().getCd(cd)) {

                        shoppingCart = new VOShoppingCart();

                        getTaskMapper().addToShoppingCart(
                                shoppingCart,
                                cd
                        );

                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                        getViewManager().showProductInfo();
                    } else {
                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

                        getViewManager().showIndex();
                    }

                    break;
                case ACTION_SHOW_SHOPPING_CART:
                    getViewManager().showShoppingCart();
                    break;
                case ADD_COMMENT:
                    System.out.println("------------_> Ejecutando");
                    VOComment comment = new VOComment();

                    String title=  req.getParameter("title");
                    System.out.println(title);
                    String content = req.getParameter("content");
                    System.out.println(content);
                    String idProduct = req.getParameter("idProducto1");
                    System.out.println(idProduct);
                    String DNI = req.getParameter("DNI");
                    System.out.println(DNI);
                    System.out.println("------------_> Ejecutando 2");
                    int idProductComment = Integer.parseInt(idProduct);
                    String idParentComment = req.getParameter("idCommentParent");
                    System.out.println(idParentComment);
                    int idParentCommentInt;
                    System.out.println("------------_> Ejecutando 3");
                    if(!idParentComment.equals("null")){
                        System.out.println("No deberia entrar aqui");
                        idParentCommentInt = Integer.parseInt(idParentComment);
                        comment.setIdCommentParent(idParentCommentInt);
                    }


                    VOUser ruser = ((VOUser) req.getSession().getAttribute(VOUser.SESSION_ATTRIBUTE_USER));


                    comment.setTitle(ruser.getFirstName() + ruser.getLastName());
                    comment.setContent(content);
                    comment.setIdProduct(idProductComment);
                    comment.setDNI(ruser.getDNI());

                    getTaskMapper().addComment(comment);
                    break;

                case ACTION_SHOW_SIGN_IN:
                    getViewManager().showSignIn();
                    break;
                case ACTION_SHOW_SIGN_UP:
                    getViewManager().showSignUp();
                    break;
                case ACTION_CHECKOUT:
                    getViewManager().showPaymentData();
                    break;
                case ACTION_CONFIRM_PAYMENT:

                    user = user = ((VOUser) session.getAttribute(VOUser.SESSION_ATTRIBUTE_USER));

                    if (user.getFirstName() != null && user.getFirstName().length() > 0) {
                        if (getTaskMapper().insertOrder(
                                (VOUser) session.getAttribute(VOUser.SESSION_ATTRIBUTE_USER),
                                (VOShoppingCart) session.getAttribute(VOShoppingCart.SESSION_ATTRIBUTE_SHOPPING_CART)
                        )) {

                            getTaskMapper().sendConfirmPaymentMail(
                                    (VOUser) session.getAttribute(VOUser.SESSION_ATTRIBUTE_USER),
                                    (VOShoppingCart) session.getAttribute(VOShoppingCart.SESSION_ATTRIBUTE_SHOPPING_CART)
                            );

                            getViewManager().showPayment();

                        } else {
                            req.setAttribute(PARAMETER_ERROR, "Se ha producido un error. No se dispone de suficiente sotck para algunos de los productos seleccionados");
                            getViewManager().showError();
                        }

                    } else {
                        getViewManager().showSignIn();
                    }

                    break;
                case ACTION_BUY_ITEM:
                    cd = new VOCd();
                    cd.setId(Integer.parseInt(req.getParameter(PARAMETER_PRODUCT)));

                    if (getTaskMapper().getCd(cd)) {

                        cd.setQuantity(Integer.parseInt(req.getParameter(PARAMETER_QUANTITY)));


                        shoppingCart = (VOShoppingCart) session.getAttribute(VOShoppingCart.SESSION_ATTRIBUTE_SHOPPING_CART);

                        getTaskMapper().addToShoppingCart(
                                shoppingCart,
                                cd
                        );

                        getViewManager().showShoppingCart();
                    } else {
                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

                        getViewManager().showIndex();
                    }


                    break;
                case ACTION_ERASE_ITEM:

                    Enumeration enumeration = req.getParameterNames();

                    while (enumeration.hasMoreElements()) {
                        String element = (String) enumeration.nextElement();

                        if (element.contains("checkbox-")) {

                            VOCd VOCd = new VOCd();
                            VOCd.setId(Integer.parseInt(element.replace("checkbox-", "").trim()));

                            try {
                                getTaskMapper().removeFromShoppingCart(
                                        (VOShoppingCart) session.getAttribute(VOShoppingCart.SESSION_ATTRIBUTE_SHOPPING_CART),
                                        VOCd
                                );

                                /*
                                    Execútase dentro de un bloque try catch por si se dese a
                                    posibilidade de que se manipulara o html
                                    e se intentara borrar un elemento que non existe no carrito
                                 */
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    getViewManager().showShoppingCart();
                    break;
                case ACTION_UPDATE_ITEM:
                    //TODO:FALTA REALIZAR ACCION DE ACTUALIZAR UN ITEM
                    getViewManager().showShoppingCart();
                    break;
                case ACTION_RESET:

                    session.setAttribute(VOUser.SESSION_ATTRIBUTE_USER, new VOUser());

                    getTaskMapper().initializeShoppingCart(
                            (VOShoppingCart) session.getAttribute(VOShoppingCart.SESSION_ATTRIBUTE_SHOPPING_CART)
                    );

                    shoppingCart = getTaskMapper().getAllCds();
                    req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

                    getViewManager().showIndex();

                    break;
                case ACTION_SIGN_IN:
                    user = ((VOUser) session.getAttribute(VOUser.SESSION_ATTRIBUTE_USER));

                    getTaskMapper().setUserData(
                            req.getParameter(VOUser.PARAMETER_DNI),
                            null,
                            null,
                            null,
                            req.getParameter(VOUser.PARAMETER_PASSWORD),
                            user
                    );

                    if (getTaskMapper().signInUser(user)) {  //usuario logueado correctamente
                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

                        getViewManager().showIndex();
                    } else {
                        req.setAttribute(PARAMETER_ERROR, "Los datos de usuario no son correctos o el usuario no existe. Por favor, inténtelo de nuevo.");

                        getViewManager().showError();
                    }

                    break;
                case ACTION_SIGN_UP:


                    //TODO:mensaxe de error ao rexistrar o usuario
                    user = ((VOUser) session.getAttribute(VOUser.SESSION_ATTRIBUTE_USER));

                    getTaskMapper().setUserData(
                            req.getParameter(VOUser.PARAMETER_DNI),
                            req.getParameter(VOUser.PARAMETER_FIRST_NAME),
                            req.getParameter(VOUser.PARAMETER_LAST_NAME),
                            req.getParameter(VOUser.PARAMETER_MAIL),
                            req.getParameter(VOUser.PARAMETER_PASSWORD),
                            user
                    );

                    //TODO: Intentar registraro ususario na base de datos
                    if (getTaskMapper().signUpUser(user)) {
                        //TODO: por ahora unha vez se registra un usuario, se mostraa páxina inicial
                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

                        getViewManager().showIndex();
                    } else {
                        req.setAttribute(PARAMETER_ERROR, "Se ha producido un error. No se puede registrar un usuario con ese DNI");

                        getViewManager().showError();
                    }

                    break;

                // Admin things
                case ADMIN_ACTION_EDIT_ITEM:
                    try {
                        id = Integer.parseInt(req.getParameter("item"));
                    } catch (NumberFormatException e) {
                        id = 0;
                    }
                    cd = new VOCd();
                    cd.setId(id);

                    if (id < 0) {
                        req.setAttribute(PARAMETER_ERROR, "Se ha producido un error. No se puede encontrar un cd con ese identificador");
                        getViewManager().showError();
                    } else {
                        if (getTaskMapper().getCd(cd)) {
                            System.err.println("-- Edit item " + id);
                            req.setAttribute(VOShoppingCart.SESSION_ITEM, cd);
                            getViewManager().showEditProduct();
                        } else {
                            System.err.println("-- Edit item " + 0);
                            cd.setId(0);
                            req.setAttribute(VOShoppingCart.SESSION_ITEM, cd);
                            getViewManager().showEditProduct();
                        }
                    }
                    break;

                case ADMIN_ACTION_SHOW_STOCK:
                    System.err.println("-- Show Stocks");
                    shoppingCart = getTaskMapper().getAllCds();
                    req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                    getViewManager().showStocks();
                    break;
                case ADMIN_ACTION_DELETE:
                    VOCd cd3 = new VOCd();
                    try {
                        cd3.setId(Integer.parseInt(req.getParameter("item")));
                        System.err.println("-- Delete item " + cd3.getId());

                        getTaskMapper().deleteCd(cd3);

                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                        getViewManager().showStocks();

                    } catch (NumberFormatException e) {
                        req.setAttribute(PARAMETER_ERROR, "Not a number");
                        getViewManager().showError();
                    }
                    break;
                case ADMIN_ACTION_SAVE:
                    System.err.println("-- Save item");
                    VOCd cd2 = new VOCd();
                    try {
                        cd2.setId(Integer.parseInt(req.getParameter("id")));
                        cd2.setTitle(req.getParameter("name"));
                        cd2.setImage(req.getParameter("imagen"));
                        cd2.setAuthor(req.getParameter("author"));
                        cd2.setQuantity(Integer.parseInt(req.getParameter("quantity")));
                        cd2.setUnitaryPrice(Float.parseFloat(req.getParameter("price")));
                        cd2.setDescription(req.getParameter("description"));
                        cd2.setCountry(req.getParameter("country"));

                        System.out.println(cd2.toString());

                        if (cd2.getId() != 0) {
                            getTaskMapper().updateCd(cd2);
                        } else {
                            getTaskMapper().createCd(cd2);
                        }

                        shoppingCart = getTaskMapper().getAllCds();
                        req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                        getViewManager().showStocks();
                    } catch (NumberFormatException e) {
                        System.err.println("[ERR] Not a Number");
                        req.setAttribute(PARAMETER_ERROR, "Not a number");
                        getViewManager().showError();
                    }
                    break;

                default:
                    shoppingCart = getTaskMapper().getAllCds();
                    req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);
                    getViewManager().showIndex();
            }
        } catch (NullPointerException e) {
            shoppingCart = getTaskMapper().getAllCds();
            req.setAttribute(VOShoppingCart.SESSION_ATTRIBUTE_CDS, shoppingCart);

            getViewManager().showIndex();
        }

    }
}

