package eyemazev20.Services;

import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.GlobalMessage;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalMessageService {
    public static void addToDb(final GlobalMessage globalMessage) {
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            UtilVars.session.save(globalMessage);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }
    public static List<GlobalMessage> getMessages() {
        final var hql = "FROM GlobalMessage";
        final var query = UtilVars.session.createQuery(hql);
        return query.list();
    }
}
