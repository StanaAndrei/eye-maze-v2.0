package eyemazev20.Services;

import eyemazev20.exceptions.HbmEx;
import eyemazev20.models.orm.MazeOrm;
import eyemazev20.utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class MazeServices {

    public static void saveMazeToDb(final MazeOrm mazeOrm) {
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            UtilVars.session.save(mazeOrm);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }

    public static void removeMazeFromDb(final int id) {
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            final var maze = (MazeOrm) UtilVars.session.get(MazeOrm.class, id);
            UtilVars.session.delete(maze);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
    }

    public static List<String> getAllMazesName(final int mkerId) {
        final var qs = "SELECT name FROM MazeOrm WHERE mkerId = :mkerId";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("mkerId", mkerId);
        List<String> r = (List<String>) query.list();
        return r;
    }

    public static MazeOrm getDataByName(final String mzName) {
        final var qs = "FROM MazeOrm WHERE mzName = :mzName";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("mzName", mzName);
        return ((List<MazeOrm>) query.list()).get(0);
    }

    public static boolean canAccessMz(final String mzName) {
        final var qs = "SELECT COUNT(*)=1 FROM MazeOrm WHERE mzName = :mzName";
        final var query = UtilVars.session.createQuery(qs);
        query.setParameter("mzName", mzName);
        boolean r = ((List<Boolean>) query.list()).get(0);
        return r;
    }

    public static void updateMzForm(final String mzName) {

    }

    @Nullable
    public static ArrayList<Object[]> getAllMazes() {
        final var hql = "SELECT mz.id, mz.name, u.username " +
                "FROM MazeOrm AS mz JOIN User AS u ON(mz.mkerId = u.id) " +
                "GROUP BY mz.id, u.username";
        final var query = UtilVars.session.createQuery(hql);
        final var list = query.list();
        final var it = list.iterator();
        final var rows = new ArrayList<Object[]>();
        while (it.hasNext()) {
            final var cols = (Object[]) it.next();
            rows.add(cols);
        }
        return rows;
    }
}
