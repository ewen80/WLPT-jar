import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by wenliang on 17-1-24.
 */
@SpringBootApplication(scanBasePackages="pw.ewen.WLPT")
@EnableJpaRepositories(basePackages = "pw.ewen.WLPT.core.repositories")
@EntityScan("pw.ewen.WLPT.core.domains.entities")
public class Application {
    public static void main(String[] args) throws ClassNotFoundException {
        SpringApplication.run(Application.class, args);
    }
}
