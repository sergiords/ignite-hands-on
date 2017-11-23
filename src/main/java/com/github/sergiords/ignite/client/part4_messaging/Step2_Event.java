package com.github.sergiords.ignite.client.part4_messaging;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.Optional;
import java.util.UUID;

public class Step2_Event implements AutoCloseable {

    private final Ignite ignite;

    private final UUID listenerUUID;

    private final IgniteCache<String, String> cache;

    public Step2_Event(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a cache named "my-cache"
         */
        this.cache = null;

        /*
         * TODO:
         * - register an event listener for remote events
         * - the listener should simply send event.shortDisplay() to ServerApp.send(...) and return true, to keep on listening to events
         * - set remote filter to listen to EventType.EVT_CACHE_OBJECT_PUT event types only
         * - use ignite.events().remoteListen(...)
         * TIP:
         * - event types are explicitly enabled in Config class (performance issue #1)
         * - remote filter ensures events are filtered remotely (on nodes where they are generated) and not locally (performance issue #2)
         */
        this.listenerUUID = null;
    }

    public void cacheValue() {

        /*
         * TODO:
         * - put a "hello" => "Bob" entry in cache to trigger the previous event listener when value is cached
         */
    }

    @Override
    public void close() {
        Optional.ofNullable(listenerUUID)
            .ifPresent(ignite.events()::stopRemoteListen);
    }

}
