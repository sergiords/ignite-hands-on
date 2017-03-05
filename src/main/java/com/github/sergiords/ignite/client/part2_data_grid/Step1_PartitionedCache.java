package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

@SuppressWarnings({"unused", "ConstantConditions", "Duplicates", "FieldCanBeLocal"})
public class Step1_PartitionedCache implements ClientStep {

    private static final String CACHE_NAME = "my-partitioned-cache";

    private final Ignite ignite;

    private final IgniteCache<String, String> cache;

    public Step1_PartitionedCache(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a CacheConfiguration named "my-partitioned-cache"
         * - set the cache mode to PARTITIONED in this configuration
         * - create a cache using this configuration
         */
        this.cache = null;
    }

    @Override
    public void run() {

        CacheObserver cacheObserver = new CacheObserver(ignite, cache);

        cacheObserver.run();

    }

    @Override
    public void close() {
        ignite.destroyCache(CACHE_NAME);
    }

}
