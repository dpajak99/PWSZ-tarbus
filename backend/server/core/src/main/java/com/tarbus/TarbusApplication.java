package com.tarbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.tarbus.repositories.jpa")
//@EnableMongoRepositories(basePackages = "com.tarbus.repositories.mongo")
public class TarbusApplication {

  public static void main(String[] args) {
    SpringApplication app = new SpringApplication(TarbusApplication.class);
    app.run(args);
  }

}
