package rc.diego.view;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by entakitos on 20/02/16.
 */
public class Error extends AbstractView {

    public Error(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        //TODO usar setViewURL(String str) para especificar donde se encontra a páxina
        setViewUrl("/web/error.jsp");
    }
}
