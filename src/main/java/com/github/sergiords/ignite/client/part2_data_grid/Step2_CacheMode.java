package com.github.sergiords.ignite.client.part2_data_grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.Collection;

public class Step2_CacheMode {

    private final Ignite ignite;

    public Step2_CacheMode(Ignite ignite) {
        this.ignite = ignite;
    }

    public IgniteCache<Integer, String> createPartitionedCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-partitioned-cache"
         * - configure cache mode to PARTITIONED
         * - create and return a cache with this configuration
         */

        return null;
    }

    public IgniteCache<Integer, String> createReplicatedCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-replicated-cache"
         * - configure cache mode to REPLICATED
         * - create and return a cache with this configuration
         */

        return null;
    }

    public IgniteCache<Integer, String> createBackupCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-backup-cache"
         * - configure cache mode to PARTITIONED
         * - configure cache to have 1 backup exactly for each entry
         * - create and return a cache with this configuration
         */

        return null;
    }

    /*
     * After executing tests, look at test names to find how different cache configurations change cache entry sizes.
     */

    public Integer getCacheSize(IgniteCache<Integer, String> cache) {

        /*
         * TODO:
         * - return cache size in cluster (all keys in all nodes)
         * - use cache.size()
         */
        return null;
    }

    public Collection<Integer> getCacheSizePerNode(IgniteCache<Integer, String> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (all keys in each node)
         * - use cache.localSize(...)
         * - use ignite.compute().broadcast() to get cache size for each server node
         */
        return null;
    }

    public Collection<Integer> getCacheSizePerNodeForPrimaryKeys(IgniteCache<Integer, String> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (only primary keys in each node)
         * - use cache.localSize(...)
         * - use ignite.compute().broadcast() to get cache size for each server node
         */
        return null;
    }

    public Collection<Integer> getCacheSizePerNodeForBackupKeys(IgniteCache<Integer, String> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (only backup keys in each node)
         * - use cache.localSize(...)
         * - use ignite.compute().broadcast() to get cache size for each server node
         */
        return null;
    }

}
