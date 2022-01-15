package eyemazev20.Services;

import eyemazev20.Dtos.http.UserInfoDto;
import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.User;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.util.List;

public class UserService {
    public static User getUserData(final int id) {
        final var qs = "FROM User WHERE " +
                "id = :id";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("id", id);
        List list = query.list();
        return (User)list.get(0);
    }
    public static User getUserData(final String loginUUID) {
        final var qs = "FROM User WHERE " +
                "loginUUID = :loginUUID";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("loginUUID", loginUUID);
        List list = query.list();
        return (User)list.get(0);
    }
    public static void updateUserInfo(final int id, final UserInfoDto userInfoDto) {
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            final var user = (User) UtilVars.session.get(User.class, id);

            if (userInfoDto.getUsername() != null && !userInfoDto.getUsername().isEmpty()) {
                user.setUsername(userInfoDto.getUsername());
            }
            if (userInfoDto.getEmail() != null && !userInfoDto.getEmail().isEmpty()) {
                user.setEmail(userInfoDto.getEmail());
            }
            if (userInfoDto.getUserpassword() != null && !userInfoDto.getUserpassword().isEmpty()) {
                user.setPassword(userInfoDto.getUserpassword());
            }
            if (userInfoDto.getProfilePicB64() != null && !userInfoDto.getProfilePicB64().isEmpty()) {
                user.setProfilePicB64(userInfoDto.getProfilePicB64());
            }

            UtilVars.session.update(user);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }
}
