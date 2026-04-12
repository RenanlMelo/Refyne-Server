package com.renan.refyne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.security.Keys;
import java.security.Key;

@SpringBootApplication
@RestController
public class RefyneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefyneApplication.class, args);

  Key key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

  System.out.println(key);
	}
}
