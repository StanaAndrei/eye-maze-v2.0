package eyemazev20;

import eyemazev20.config.OrmConfig;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
		var ctx = new AnnotationConfigApplicationContext();
		ctx.register(OrmConfig.class);
		ctx.close();

	}

}
