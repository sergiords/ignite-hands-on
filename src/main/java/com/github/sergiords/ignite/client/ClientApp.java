package com.github.sergiords.ignite.client;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ClientApp {

    public static void main(String[] args) {

        System.setProperty("node.id", "Client");

        // Use given configuration to avoid multi-cast collisions and stick to localhost :-)
        IgniteConfiguration configuration = Config.igniteConfiguration();
        configuration.setClientMode(true);

        // Use this client as a playground, when you are ready, launch unit tests

        try (Ignite ignite = Ignition.start(configuration)) {

            ignite.compute().broadcast(() -> System.out.println("\nHello World\n"));

        }

    }

}
