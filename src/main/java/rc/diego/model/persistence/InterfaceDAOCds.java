package rc.diego.model.persistence;

import rc.diego.model.VO.VOCd;
import rc.diego.model.VO.VOShoppingCart;
import rc.diego.model.persistence.MySQL.DAOCdsMySQL;

/**
 * Created by entakitos on 17/03/16.
 */
public interface InterfaceDAOCds {
    VOShoppingCart getAllCDs();
    boolean getCD(VOCd cd);
    boolean updateCDQuantity(VOCd cd);


    VOShoppingCart getCDsByFilter(String filter);

    boolean updateCD(VOCd cd);
    boolean create(VOCd cd) throws DAOCdsMySQL.CdAlreadyExistsException;

    boolean deleteCD(VOCd cd);

}
