package eyemazev20.Services;

import eyemazev20.Dtos.http.UserInfoUpDto;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.User;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.List;

public class UserService {
    public static User getUserData(final String loginUUID) {
        final var qs = "FROM User WHERE " +
                "loginUUID = :loginUUID";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("loginUUID", loginUUID);
        List list = query.list();
        return (User)list.get(0);
    }
    public static void updateUserInfo(final int id, final UserInfoUpDto userInfoUpDto) {
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            final var user = (User) UtilVars.session.get(User.class, id);

            if (userInfoUpDto.getUsername() != null && !userInfoUpDto.getUsername().isEmpty()) {
                user.setUsername(userInfoUpDto.getUsername());
            }
            if (userInfoUpDto.getEmail() != null && !userInfoUpDto.getEmail().isEmpty()) {
                user.setEmail(userInfoUpDto.getEmail());
            }
            if (userInfoUpDto.getUserpassword() != null && !userInfoUpDto.getUserpassword().isEmpty()) {
                user.setPassword(userInfoUpDto.getUserpassword());
            }
            if (userInfoUpDto.getProfilePicB64() != null && !userInfoUpDto.getProfilePicB64().isEmpty()) {
                user.setProfilePicB64(userInfoUpDto.getProfilePicB64());
            }

            UtilVars.session.update(user);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }
}
