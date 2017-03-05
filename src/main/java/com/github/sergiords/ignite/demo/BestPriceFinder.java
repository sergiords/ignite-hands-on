package com.github.sergiords.ignite.demo;

import com.github.sergiords.ignite.data.Station;

import java.time.LocalDate;
import java.util.Optional;

public interface BestPriceFinder {

    Optional<BestPrice> bestPriceFor(Station origin, Station destination, LocalDate date, Integer seats);

}
