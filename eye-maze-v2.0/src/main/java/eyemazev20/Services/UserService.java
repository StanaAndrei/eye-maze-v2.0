package eyemazev20.Services;

import eyemazev20.Dtos.UsernamePutReq;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.User;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

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
}
