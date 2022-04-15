package com.roadToMaster.UniversityManagerApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class UniversityManagerApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(UniversityManagerApiApplication.class, args);
  }

}
