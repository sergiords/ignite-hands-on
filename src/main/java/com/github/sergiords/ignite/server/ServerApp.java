package com.github.sergiords.ignite.server;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

public class ServerApp {

    public static void main(String[] args) {

        /*
         * TODO:
         * - start an ignite instance with configuration from Config#igniteConfiguration
         */
        IgniteConfiguration igniteConfiguration = Config.igniteConfiguration();
        Ignition.start(igniteConfiguration);
    }

}
