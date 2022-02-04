package com.roadToMaster.UniversityManagerApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//TODO: remove exclude to enable security configuration
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class UniversityManagerApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversityManagerApiApplication.class, args);
	}

}
