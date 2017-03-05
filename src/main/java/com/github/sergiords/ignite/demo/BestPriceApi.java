package com.github.sergiords.ignite.demo;

import com.github.sergiords.ignite.data.Station;
import org.apache.ignite.Ignite;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PreDestroy;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.github.sergiords.ignite.demo.BestPriceFinderService.CACHE_NAME;
import static java.util.stream.Collectors.toList;

@RestController
@SuppressWarnings({"unused"})
public class BestPriceApi {

    private static final String SERVICE_NAME = "best-price-service";

    private final Ignite ignite;

    public BestPriceApi(Ignite ignite, BestPriceFinderService bestPriceFinderService) {

        this.ignite = ignite;

        /*
         * TODO:
         * - deploy an instance of the given service in each server node (Node Singleton Service)
         */
        ignite.services().deployNodeSingleton(SERVICE_NAME, bestPriceFinderService);
    }

    @GetMapping("/{origin}/{destination}/{begin}/{end}")
    public List<BestPrice> bestPricePerDay(
        @PathVariable("origin") Station origin,
        @PathVariable("destination") Station destination,
        @PathVariable("begin") @DateTimeFormat(iso = ISO.DATE) LocalDate begin,
        @PathVariable("end") @DateTimeFormat(iso = ISO.DATE) LocalDate end,
        @RequestParam(value = "seats", defaultValue = "1") Integer seats
    ) {

        return Stream
            .iterate(begin, date -> date.plusDays(1))
            .limit(ChronoUnit.DAYS.between(begin, end) + 1) // all dates in [begin; end]
            .parallel() // important, since all calls must be dispatched to concurrently-running nodes
            .map(date -> {

                String key = BestPriceFinderService.getKey(origin, destination, date);

                /*
                 * TODO:
                 * - return best price for key using an affinity call based on price cache and given key
                 * - affinityCall must rely on BestPriceFinderService locally deployed on each node to compute best price
                 */
                return ignite.compute().affinityCall(CACHE_NAME, key, () -> {

                    // local-node deployed service instance
                    BestPriceFinder bestPriceFinder = ignite.services().service(SERVICE_NAME);

                    return bestPriceFinder.bestPriceFor(origin, destination, date, seats);

                });
            })
            .filter(Optional::isPresent) // do not show dates with no results
            .map(Optional::get)
            .collect(toList());
    }

    @PreDestroy
    public void destroy() {
        ignite.services().cancel(SERVICE_NAME);
        ignite.destroyCache(CACHE_NAME);
    }

}
