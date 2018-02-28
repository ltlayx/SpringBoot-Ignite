package com.iss.users;

import org.apache.ignite.springdata.repository.config.EnableIgniteRepositories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
// Ensure @RepositoryConfig can be found by SpringApplication
@EnableIgniteRepositories
//@ComponentScan(basePackages = {"com.iss.users.dao"})
public class UsersApplication {
	public static void main(String[] args) {
		SpringApplication.run(UsersApplication.class, args);
	}
}
