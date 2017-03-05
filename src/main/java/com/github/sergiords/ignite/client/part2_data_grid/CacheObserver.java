package com.github.sergiords.ignite.client.part2_data_grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.Map;

@SuppressWarnings({"unused", "FieldCanBeLocal", "InfiniteLoopStatement"})
public class CacheObserver implements Runnable {

    private final Ignite ignite;

    private final IgniteCache<String, String> cache;

    CacheObserver(Ignite ignite, IgniteCache<String, String> cache) {
        this.ignite = ignite;
        this.cache = cache;
    }

    @Override
    public void run() {

        if (cache.size() == 0) {
            System.out.println("Cache is empty, populating cache with 1000 values...");
            write1000CacheData();
        }

        System.out.printf("===== Cache Info for: %s =====%n", cache.getName());
        System.out.println("10 cache values: " + read10CacheData());
        System.out.println("Cache size: " + getCacheSize());
        System.out.println("Cache size per node: " + getCacheSizePerNode());
        System.out.println("Primary cache size per node: " + getPrimaryCacheSizePerNode());
        System.out.println("Backup cache size per node: " + getBackupCacheSizePerNode());
    }

    private void write1000CacheData() {

        /*
         * TODO:
         * - put 1000 entries in the cache in the following format: "Key1"=>"Value1", "Key2"=>"Value2", ...
         */
    }

    private Map<String, String> read10CacheData() {

        /*
         * TODO:
         * - get and return the 10 values associated to keys [Key50;Key59] in the cache in one call
         */
        return null;
    }

    private Integer getCacheSize() {

        /*
         * TODO:
         * - return cluster-wide cache size
         * - see IgniteCache#size
         */
        return null;
    }

    private Map<String, Integer> getCacheSizePerNode() {

        /*
         * TODO:
         * - return a map containing cache size for all entries in each node (NodeId => localCacheSize)
         * - see IgniteCache#localSize
         * - retrieve NodeId using System.getProperty("node.id")
         */
        return null;
    }

    private Map<String, Integer> getPrimaryCacheSizePerNode() {

        /*
         * TODO:
         * - return a map containing cache size for primary entries in each node (NodeId => localCacheSize)
         * - see IgniteCache#localSize
         * - retrieve NodeId using System.getProperty("node.id")
         */
        return null;
    }

    private Map<String, Integer> getBackupCacheSizePerNode() {

        /*
         * TODO:
         * - return a map containing cache size for backup entries in each node (NodeId => localCacheSize)
         * - see IgniteCache#localSize
         * - retrieve NodeId using System.getProperty("node.id")
         */
        return null;
    }

}
