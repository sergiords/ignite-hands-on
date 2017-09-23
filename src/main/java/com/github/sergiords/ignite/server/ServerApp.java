package com.github.sergiords.ignite.server;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;

import java.util.concurrent.atomic.AtomicReference;

import static org.apache.ignite.cache.CachePeekMode.*;

public class ServerApp {

    public static void main(String[] args) {

        /*
         * TODO:
         * - start an ignite instance with configuration from Config#igniteConfiguration
         */
        IgniteConfiguration configuration = Config.igniteConfiguration();

        Ignite ignite = Ignition.start(configuration);

        // Instance to check what is happening on server nodes during tests
        new ServerApp(ignite);
    }

    /*
     * ==============================================
     * DO NOT EDIT configuration bellow this comment.
     * ==============================================
     */

    public ServerApp(Ignite ignite) {

        ignite.events().localListen(event -> {

            ignite.cacheNames().forEach(cacheName -> {
                IgniteCache cache = ignite.cache(cacheName);
                System.out.printf("Cache: %s, total: %d, primary: %d, backup: %d, all: %d%n", cacheName,
                    cache.size(), cache.localSize(PRIMARY), cache.localSize(BACKUP), cache.localSize(ALL));
            });

            return true;

        }, EventType.EVT_NODE_LEFT, EventType.EVT_CACHE_REBALANCE_STOPPED, EventType.EVT_NODE_FAILED, EventType.EVT_NODE_JOINED);

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
