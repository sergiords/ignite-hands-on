package com.github.sergiords.ignite.server;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;

import java.util.concurrent.atomic.AtomicReference;

public class ServerApp {

    public static void main(String[] args) {

        /*
         * TODO:
         * - start an ignite instance with configuration from Config#igniteConfiguration
         */
        Ignite ignite = Ignition.start(Config.igniteConfiguration());

        // Instance to check what is happening on server nodes during tests
        new ServerAppInfo(ignite);
    }

    private static final AtomicReference<String> callReference = new AtomicReference<>();

    public static void reset() {
        callReference.set(null);
    }

    public static void print(String message) {
        callReference.set(message);
    }

    public static String watch() {
        return callReference.get();
    }

    public static String getName() {
        String nodeId = System.getProperty("node.id");
        callReference.set(nodeId);
        return nodeId;
    }

    public static String getThreadName() {
        String name = Thread.currentThread().getName();
        callReference.set(name);
        return name;
    }

}
