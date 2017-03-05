package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import javax.cache.configuration.Factory;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.IntStream.range;

@SuppressWarnings({"unused", "ConstantConditions", "UnusedAssignment"})
public class Step4_StoredCache implements ClientStep {

    private static final String CACHE_NAME = "my-stored-cache";

    private final Ignite ignite;

    private final IgniteCache<String, String> cache;

    public Step4_StoredCache(Ignite ignite) {

        this.ignite = ignite;

        final String basePath;
        try {
            basePath = Files.createDirectories(Paths.get("build").resolve("stored-cache-test")).toAbsolutePath().toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        /*
         * TODO:
         * - complete MyCacheStore class: this store read, write and delete cache values from files
         * - create a of Factory for MyCacheStore: this factory creates CacheStore instances across cluster nodes
         */
        Factory<MyCacheStore> myCacheStoreFactory = null;

        /*
         * TODO:
         * - create a CacheConfiguration named "my-stored-cache"
         * - set the cache mode to PARTITIONED
         * - set the cache store factory
         * - enable read-through and write-through
         * - create a cache using this configuration
         */
        this.cache = null;
    }

    @Override
    public void run() {

        if (cache.size() > 0 && cache.size() < 1000) {
            ignite.compute().run(() -> range(0, 1000).mapToObj(n -> "Key" + n).forEach(cache::get));
        }

        CacheObserver cacheObserver = new CacheObserver(ignite, cache);

        cacheObserver.run();

    }

    private static class MyCacheStore extends CacheStoreAdapter<String, String> {

        private final String basePath;

        private MyCacheStore(String basePath) {
            this.basePath = basePath;
        }

        @Override
        public String load(String key) throws CacheLoaderException {

            /*
             * TODO:
             * - read value from $basePath/$key file
             * - use: new String(Files.readAllBytes(...)))
             */
            try {
                throw new IOException("To be implemented");
            } catch (IOException e) {
                throw new CacheLoaderException(e);
            }
        }

        @Override
        public void write(Cache.Entry<? extends String, ? extends String> entry) throws CacheWriterException {

            /*
             * TODO:
             * - write value to $basePath/$key file
             * - use: Files.write(..., value.getBytes()))
             */
            try {
                throw new IOException("To be implemented");
            } catch (IOException e) {
                throw new CacheWriterException(e);
            }
        }

        @Override
        public void delete(Object key) throws CacheWriterException {

            /*
             * TODO:
             * - delete $basePath/$key file
             * - use: Files.delete(...)
             */
            try {
                throw new IOException("To be implemented");
            } catch (IOException e) {
                throw new CacheWriterException(e);
            }
        }
    }

    @Override
    public void close() {
        ignite.destroyCache(CACHE_NAME);
    }

}
