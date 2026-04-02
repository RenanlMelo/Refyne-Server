package com.renan.refyne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RefyneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RefyneApplication.class, args);
	}
}
