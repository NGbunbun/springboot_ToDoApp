package com.example.todoapp;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoAppApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TodoAppApplication.class);
		app.setBannerMode(Mode.OFF);
		app.run(args);
	}

}
