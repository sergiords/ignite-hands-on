package com.github.sergiords.ignite.client.part5_messaging;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.UUID;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
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
        this.cache = null;

        /*
         * TODO:
         * - register an event listener for remote events
         * - the listener should simply print the event it receives
         * - ensure events are filtered remotely (on nodes where they are generated) using remoteFilter
         * - listen for EventType.EVT_CACHE_OBJECT_PUT and EventType.EVT_JOB_FINISHED only (enabled for the hands on)
         * - use IgniteEvents#remoteListen
         */
        this.listenerUUID = null;
    }

    @Override
    public void run() {

        /*
         * TODO:
         * - run a job on a remote node to trigger the event listener when job finishes
         */

        /*
         * TODO:
         * - put a value in the cache to trigger the event listener when value is cached
         */
    }

    @Override
    public void close() {
        ignite.events().stopRemoteListen(listenerUUID);
        ignite.destroyCache(CACHE_NAME);
    }

}
