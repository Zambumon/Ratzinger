package rc.diego.model.task;

import rc.diego.model.VO.VOCd;
import rc.diego.model.VO.VOComment;
import rc.diego.model.VO.VOShoppingCart;
import rc.diego.model.persistence.AbstractFactoryMySQL;
import rc.diego.model.persistence.InterfaceDAOFactory;

import java.util.ArrayList;

/**
 * Created by entakitos on 17/03/16.
 */
public class getAllCommentsFromDataBase implements InterfaceTask{

    private InterfaceDAOFactory daoFactory;

    private VOCd cd;

    public getAllCommentsFromDataBase(VOCd cd) {
        this.cd = cd;
    }

    public ArrayList<VOComment> comments;

    public ArrayList<VOComment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<VOComment> comments) {
        this.comments = comments;
    }



    @Override
    public void run() {
        daoFactory = new AbstractFactoryMySQL();
        try {
            comments = daoFactory.getDAOComments().getCommentsByProduct(cd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}