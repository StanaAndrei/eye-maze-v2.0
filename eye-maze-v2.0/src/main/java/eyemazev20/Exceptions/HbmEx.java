package eyemazev20.Exceptions;

import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class HbmEx {

    @ExceptionHandler
    public static void handleHbmErrs(Transaction transaction, HibernateException hibernateException) {
        if (transaction != null) {
            transaction.rollback();
        }
        hibernateException.printStackTrace();
    }
}
