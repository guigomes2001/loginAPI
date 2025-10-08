package com.loginAPI.loginAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {
	    "controller",
	    "dao",
	    "domain",
	    "dto",
	    "exception",
	    "repository",
	    "securityAPI",
	    "bo",
	    "util"
	})
@SpringBootApplication
public class LoginApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginApiApplication.class, args);
	}

}
