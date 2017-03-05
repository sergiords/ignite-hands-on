package com.github.sergiords.ignite.demo;

import com.github.sergiords.ignite.Config;
import com.github.sergiords.ignite.data.TrainServer;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "com.github.sergiords.ignite.demo")
public class BestPriceApp {

    public static void main(String[] args) {
        System.setProperty("server.port", "8081");
        SpringApplication.run(BestPriceApp.class, args);
    }

    @Bean
    public Ignite ignite() {
        IgniteConfiguration igniteConfiguration = Config.igniteConfiguration();
        igniteConfiguration.setClientMode(true);
        return Ignition.start(igniteConfiguration);
    }

    @Bean
    public TrainServer trainServer() {
        return new TrainServer();
    }

    @Bean
    public BestPriceFinderService bestPriceFinderService(Ignite ignite, TrainServer trainServer) {
        return new BestPriceFinderService(ignite, trainServer);
    }

}
