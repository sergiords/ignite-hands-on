package com.github.sergiords.ignite.keynote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.github.sergiords.ignite.keynote")
public class KeynoteApp {

    public static void main(String[] args) {
        SpringApplication.run(KeynoteApp.class, args);
    }

}
