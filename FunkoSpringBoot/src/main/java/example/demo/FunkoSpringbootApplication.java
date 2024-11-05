package example.demo;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FunkoSpringbootApplication {
	public static void main(String[] args) {
		SpringApplication.run(FunkoSpringbootApplication.class, args);

		System.out.println("\n\n🕹️ SERVER IS RUNNING 🕹️\n\n");
	}
}