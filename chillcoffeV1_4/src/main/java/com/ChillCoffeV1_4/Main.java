package com.ChillCoffeV1_4;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.awt.Desktop;
import java.net.URI;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        try {
            Desktop.getDesktop().browse(new URI("http://localhost:8080/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
