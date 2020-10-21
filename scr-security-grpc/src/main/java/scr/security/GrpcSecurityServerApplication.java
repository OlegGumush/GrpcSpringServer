package scr.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class GrpcSecurityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GrpcSecurityServerApplication.class, args);		
	}
}
