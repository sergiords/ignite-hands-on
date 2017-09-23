package com.github.sergiords.ignite.server;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.events.EventType;

import static org.apache.ignite.cache.CachePeekMode.*;

public class ServerAppInfo {

    public ServerAppInfo(Ignite ignite) {

        ignite.events().localListen(event -> {

            ignite.cacheNames().forEach(cacheName -> {
                IgniteCache cache = ignite.cache(cacheName);
                System.out.printf("Cache: %s, total: %d, primary: %d, backup: %d, all: %d%n", cacheName,
                    cache.size(), cache.localSize(PRIMARY), cache.localSize(BACKUP), cache.localSize(ALL));
            });

            return true;

        }, EventType.EVT_NODE_LEFT, EventType.EVT_CACHE_REBALANCE_STOPPED, EventType.EVT_NODE_FAILED, EventType.EVT_NODE_JOINED);

    }


}
