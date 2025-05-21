package com.project.SH;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Base64;

@SpringBootApplication
public class TheStoryHollowApplication {

	public static void main(String[] args) {
		SpringApplication.run(TheStoryHollowApplication.class, args);
	}

}
