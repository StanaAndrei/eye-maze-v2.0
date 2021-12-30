package eyemazev20.Services;

import eyemazev20.Dtos.UsernamePutReq;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.User;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.List;

public class UserService {
    public static boolean updateUsername(UsernamePutReq usernamePutReq) {
        Transaction transaction = null;
        boolean ok = true;
        try {
            transaction = UtilVars.session.beginTransaction();
            final var user = (User) UtilVars.session.get(User.class, usernamePutReq.getId());
            user.setUsername(usernamePutReq.getNewUsername());
            UtilVars.session.update(user);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
            ok = false;
        }
        return ok;
    }
    public static User getUserData(final String loginUUID) {
        final var qs = "FROM User WHERE " +
                "loginUUID = :loginUUID";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("loginUUID", loginUUID);
        List list =  query.list();
        return (User)list.get(0);
    }
}
