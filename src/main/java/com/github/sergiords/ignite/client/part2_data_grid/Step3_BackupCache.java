package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Step3_BackupCache implements ClientStep {

    private static final String CACHE_NAME = "my-backup-cache";

    private final Ignite ignite;

    private IgniteCache<String, String> cache;

    public Step3_BackupCache(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a CacheConfiguration named "my-backup-cache"
         * - set the cache mode to PARTITIONED
         * - set the number of backups to 1 (i.e. 1 primary node and 1 backup node)
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
