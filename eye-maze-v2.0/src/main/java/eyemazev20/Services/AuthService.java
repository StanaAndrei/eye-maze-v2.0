package eyemazev20.Services;

import eyemazev20.Dtos.http.AuthReq;
import eyemazev20.Dtos.http.RegReq;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.User;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.List;

@Service
public class AuthService {

    public static UUID getLoginUUID(final AuthReq authReq) {
        final var qs = "SELECT loginUUID FROM User WHERE " +
                "email = :email AND " +
                "userPassword = :userPassword";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("email", authReq.getEmail());
        query.setParameter("userPassword", authReq.getPassword());
        final var loginUUID = ((List<String>) query.list()).get(0);
        return UUID.fromString(loginUUID);
    }

    public static int confirmReg(final RegReq regReq) {
        Transaction transaction = null;
        Integer userId = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            final var user = new User(
                    regReq.getUsername(),
                    regReq.getPassword(),
                    regReq.getEmail(),
                    UUID.randomUUID().toString()
            );
            userId = (int) UtilVars.session.save(user);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
        return userId;
    }

    public static boolean isAuth(HttpSession httpSession) {
        return httpSession.getAttribute("loginUUID") != null;
    }
}
