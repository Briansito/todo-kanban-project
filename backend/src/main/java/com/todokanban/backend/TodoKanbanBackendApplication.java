package com.todokanban.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.todokanban")
public class TodoKanbanBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoKanbanBackendApplication.class, args);
    }

}
