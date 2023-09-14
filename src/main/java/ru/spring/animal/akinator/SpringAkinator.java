package ru.spring.animal.akinator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.spring.animal.akinator.service.SpringAkinatorService;

@SpringBootApplication
public class SpringAkinator implements CommandLineRunner {
    private final SpringAkinatorService akinatorService;

    @Autowired
    public SpringAkinator(SpringAkinatorService akinatorService) {
        this.akinatorService = akinatorService;
    }

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(SpringAkinator.class);
        app.run(args);
    }

    @Override
    public void run(String... args) {
        akinatorService.startGame();
    }
}
