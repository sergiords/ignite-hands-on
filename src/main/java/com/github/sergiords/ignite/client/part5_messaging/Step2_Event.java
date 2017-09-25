package com.github.sergiords.ignite.client.part5_messaging;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.events.EventType;

import java.util.UUID;

public class Step2_Event implements ClientStep {

    private static final String CACHE_NAME = "my-event-triggering-cache";

    private final Ignite ignite;

    private final UUID listenerUUID;

    private final IgniteCache<String, String> cache;

    public Step2_Event(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a cache named "my-event-triggering-cache"
         */
        this.cache = ignite.getOrCreateCache(CACHE_NAME);

        /*
         * TODO:
         * - register an event listener for remote events
         * - the listener should simply print the event it receives
         * - ensure events are filtered remotely (on nodes where they are generated) using remoteFilter
         * - listen for EventType.EVT_CACHE_OBJECT_PUT and EventType.EVT_JOB_FINISHED only (enabled for the hands on)
         * - use IgniteEvents#remoteListen
         */
        this.listenerUUID = ignite.events().remoteListen(
            (uuid, event) -> {
                System.out.printf("Event %d received on node: %s%n", event.type(), event.node().attribute("node.id"));
                return true;
            },
            event -> event.type() == EventType.EVT_CACHE_OBJECT_PUT || event.type() == EventType.EVT_JOB_FINISHED
        );
    }

    @Override
    public void run() {

        /*
         * TODO:
         * - run a job on a remote node to trigger the event listener when job finishes
         */
        ignite.compute().run(() -> System.out.println("Hello"));

        /*
         * TODO:
         * - put a value in the cache to trigger the event listener when value is cached
         */
        cache.put("hello", "hello");
    }

    @Override
    public void close() {
        ignite.events().stopRemoteListen(listenerUUID);
        ignite.destroyCache(CACHE_NAME);
    }

}
