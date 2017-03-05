package com.github.sergiords.ignite.demo;

import com.github.sergiords.ignite.data.Station;
import com.github.sergiords.ignite.data.TrainServer;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class BestPriceFinderService implements BestPriceFinder, Service {

    static final String CACHE_NAME = "price-cache";

    private final Ignite ignite;

    private final TrainServer trainServer;

    private final IgniteCache<String, List<BestPrice>> priceCache;

    BestPriceFinderService(Ignite ignite, TrainServer trainServer) {

        this.ignite = ignite;
        this.trainServer = trainServer;

        /*
         * TODO:
         * - create a partitioned cache named "price-cache"
         */
        CacheConfiguration<String, List<BestPrice>> configuration = new CacheConfiguration<>(CACHE_NAME);
        configuration.setCacheMode(CacheMode.PARTITIONED);
        this.priceCache = ignite.getOrCreateCache(configuration);
    }

    @Override
    public void init(ServiceContext ctx) throws Exception {

        ClusterNode localNode = ignite.cluster().localNode();
        String nodeId = localNode.attribute("node.id");

        AtomicInteger loadedEntries = new AtomicInteger();
        AtomicInteger skippedEntries = new AtomicInteger();

        System.out.printf("cache loading in %s...%n", nodeId);

        trainServer.stations()
            .forEach(origin -> trainServer.stations().stream()
                .filter(destination -> !origin.equals(destination))
                .forEach(destination -> trainServer.dates()
                    .forEach(date -> {

                        String key = getKey(origin, destination, date);

                        if (ignite.affinity(CACHE_NAME).isPrimary(localNode, key)) {

                            /*
                             * TODO:
                             * - load cache data since this node is primary for this key
                             * - compute and store all BestPriceProposals for this origin, destination and date
                             * - increment loadedEntries counter
                             */

                            List<BestPrice> bestPrices = trainServer.trains(origin, destination, date).stream()
                                .flatMap(train -> trainServer.prices(train).stream()
                                    .map(price -> new BestPrice(train, price)))
                                .collect(Collectors.toList());

                            priceCache.put(key, bestPrices);

                            loadedEntries.incrementAndGet();

                        } else {

                            /*
                             * TODO:
                             * - do not load cache data since this node is not primary for this key
                             * - increment skippedEntries counter
                             */

                            skippedEntries.incrementAndGet();
                        }
                    })));

        System.out.printf("cache loaded in %s, loaded keys: %d, skipped keys: %d%n", nodeId, loadedEntries.get(), skippedEntries.get());
    }

    @Override
    public void execute(ServiceContext ctx) throws Exception {
        // nothing special here
    }

    static String getKey(Station origin, Station destination, LocalDate date) {

        /*
         * TODO:
         * - return a key containing origin, destination and formatted date
         * - this key will be used to store prices in cache
         */
        return format("%s-%s-%s", origin, destination, date.format(ISO_LOCAL_DATE));
    }

    @Override
    public Optional<BestPrice> bestPriceFor(Station origin, Station destination, LocalDate date, Integer seats) {

        String key = getKey(origin, destination, date);

        System.out.printf("Finding best price for %s%n", key);

        /*
         * TODO:
         * - get prices associated to key
         * - filter prices with enough seats
         * - return best price (min) amongst filtered prices
         */
        return Optional
            .ofNullable(priceCache.get(key))
            .map(Collection::stream)
            .orElseGet(Stream::empty)
            .filter(bestPrice -> bestPrice.getSeats() >= seats)
            .min(Comparator.comparingInt(BestPrice::getPrice));
    }

    @Override
    public void cancel(ServiceContext ctx) {

    }

}
