package vn.com.irtech.eport.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "vn.com.irtech.eport")
@SpringBootApplication
public class EportApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(EportApiApplication.class, args);
	}
}
