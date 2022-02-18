package eyemazev20.Services;

import eyemazev20.Exceptions.HbmEx;
import eyemazev20.Models.orm.MazeOrm;
import eyemazev20.Utils.UtilVars;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.stereotype.Service;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Service
public class MazeServices {

    public static int saveMazeToDb(final MazeOrm mazeOrm) {
        Serializable mazeId = null;
        Transaction transaction = null;
        try {
            transaction = UtilVars.session.beginTransaction();
            mazeId = UtilVars.session.save(mazeOrm);
            transaction.commit();
        } catch (HibernateException hibernateException) {
            HbmEx.handleHbmErrs(transaction, hibernateException);
        }
        return (int) mazeId;
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
        final boolean r = ((List<Boolean>) query.list()).get(0);
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

    public static ArrayList<String> getAllPcGenMazes() {
        final var hql = "SELECT name FROM MazeOrm WHERE mkerId IS NULL";
        final var query = UtilVars.session.createQuery(hql);
        final var list = query.list();
        return (ArrayList<String>) list;
    }
}
