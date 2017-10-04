package com.github.sergiords.ignite.client.part5_messaging;

import com.github.sergiords.ignite.server.ServerApp;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.events.EventType;

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
        this.cache = ignite.getOrCreateCache("my-cache");

        /*
         * TODO:
         * - register a local event listener for remote events
         * - the listener should simply send event.message() to ServerApp.send(...) and return true, to keep on listening to events
         * - set remote filter to listen to EventType.EVT_CACHE_OBJECT_PUT types only
         * - use ignite.events().remoteListen(...)
         * TIP:
         * - event types are explicitly enabled in Config class (performance issue #1)
         * - remote filter ensures events are filtered remotely (on nodes where they are generated) and not locally (performance issue #2)
         */
        this.listenerUUID = ignite.events().remoteListen((uuid, event) -> {
            ServerApp.send(event.shortDisplay());
            return true;
        }, event -> event.type() == EventType.EVT_CACHE_OBJECT_PUT);
    }

    public void cacheValue() {

        /*
         * TODO:
         * - put a "hello" => "Bob" entry in cache to trigger the previous event listener when value is cached
         */
        ignite.compute().run(() -> cache.put("hello", "Bob"));
    }

    @Override
    public void close() {
        Optional.ofNullable(listenerUUID)
            .ifPresent(ignite.events()::stopRemoteListen);
    }

}
