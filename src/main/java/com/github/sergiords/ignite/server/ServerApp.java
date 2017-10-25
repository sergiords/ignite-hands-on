package com.github.sergiords.ignite.server;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;

import java.io.OutputStream;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.ConsoleHandler;
import java.util.logging.Logger;

import static java.lang.String.format;

public class ServerApp {

    public static void main(String[] args) {

        /*
         * TODO:
         * - start an ignite instance with configuration from Config.igniteConfiguration()
         */
        IgniteConfiguration configuration = Config.igniteConfiguration();

        Ignition.start(configuration);
    }

    /*
     * ==============================================
     * DO NOT EDIT code bellow this comment.
     * ==============================================
     */

    public static final Logger logger;

    static {

        // Set a unique info in each node
        System.setProperty("node.info", UUID.randomUUID().toString());

        // Configure logging
        System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tT] %5$s%n");
        logger = Logger.getLogger("ServerApp");
        logger.addHandler(new ConsoleHandler() {
            @Override
            public void setOutputStream(OutputStream out) {
                // output logs to System.out instead of System.err!
                super.setOutputStream(System.out);
            }
        });
    }

    private static final AtomicReference<String> callReference = new AtomicReference<>();

    public static void reset() {
        callReference.set(null);
    }

    public static void print(String message) {
        callReference.set(message);
        logger.info(message);
    }

    public static String watch() {
        return callReference.get();
    }

    public static String getName() {
        String name = name();
        logger.info(() -> format("Return name: %s", name));
        callReference.set(name);
        return name;
    }

    public static void send(Object event) {
        logger.info(() -> format("Message received: %s", String.valueOf(event)));
        callReference.set(event.toString());
    }

    private static String name() {
        return System.getProperty("node.id");
    }

    public static String getInfo() {
        String info = info();
        logger.info(() -> format("Return info: %s", info));
        callReference.set(info);
        return info;
    }

    private static String info() {
        return System.getProperty("node.info");
    }

}
