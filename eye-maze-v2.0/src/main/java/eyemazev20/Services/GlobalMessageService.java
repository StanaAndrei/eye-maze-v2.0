package eyemazev20.Services;

import eyemazev20.Exceptions.HbmEx;
import eyemazev20.Models.orm.GlobalMessage;
import eyemazev20.Utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
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

    @Nullable
    public static ArrayList<Object[]> getMessages() {
        final var hql = "SELECT gm.content, gm.timestp, u.profilePicB64, u.username, u.loginUUID " +
                "FROM GlobalMessage gm JOIN User u ON(gm.senderId = u.id)";
        final var query = UtilVars.session.createQuery(hql);
        List list = query.list();
        Iterator it = list.iterator();
        final var rows = new ArrayList<Object[]>();
        while (it.hasNext()) {
            final var cols = (Object[]) it.next();
            //System.out.println(rows[0] + "|" + rows[1] + "|" + rows[4]);
            rows.add(cols);
        }
        return rows;
    }
}
