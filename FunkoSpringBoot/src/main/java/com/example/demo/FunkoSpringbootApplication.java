package com.example.demo;

import com.example.demo.funko.demo.DemoData;
import com.example.demo.funko.repository.FunkoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class FunkoSpringbootApplication implements CommandLineRunner {
	private final Logger log = LoggerFactory.getLogger(FunkoSpringbootApplication.class);
	@Value("#{environment.getProperty('aplication.mode') != null ? environment.getProperty('aplication.mode') : 'development'}")
	String executionMode;
	@Autowired
	private FunkoRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(FunkoSpringbootApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (executionMode.equals("development")) { // Solo en desarrollo
			log.info("Insertando datos de demostración en la base de datos");
			repository.deleteAll();
			repository.saveAll(DemoData.FUNKO_DEMO);
		}
	}
}
