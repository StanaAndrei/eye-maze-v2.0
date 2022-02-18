package eyemazev20;

import eyemazev20.Configs.MatchMakerConfig;
import eyemazev20.Configs.OrmConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Properties;

@SpringBootApplication
public class Application {
	private static void testing() {
		System.err.println("WORKS");
		//GlobalMessageService.getMessages();
	}

	public static void main(String[] args) {
		final var application = new SpringApplication(Application.class);
		final var properties = new Properties();
		properties.setProperty("spring.cache.type", "none");
		application.setDefaultProperties(properties);
		application.run(args);
		var ctx = new AnnotationConfigApplicationContext();
		ctx.register(OrmConfig.class);
		ctx.register(MatchMakerConfig.class);
		ctx.close();
		testing();
	}
}
