package eyemazev20.config;

import eyemazev20.utils.UtilVars;
import org.springframework.context.annotation.Bean;
import org.hibernate.Session;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrmConfig {
    @Bean
    public void config() {
        var configuration = new org.hibernate.cfg.Configuration();
        try {
            Session sessionFactory = configuration.configure().buildSessionFactory().openSession();
            UtilVars.session = sessionFactory;
        } catch (Exception e) {
            System.out.println("mess:" + e.getMessage());
        }
    }
}
