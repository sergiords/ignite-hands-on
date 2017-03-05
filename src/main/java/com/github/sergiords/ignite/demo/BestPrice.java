package com.github.sergiords.ignite.demo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.github.sergiords.ignite.data.Price;
import com.github.sergiords.ignite.data.Train;

import java.time.LocalDate;
import java.time.LocalTime;

@SuppressWarnings({"unused", "WeakerAccess"})
public class BestPrice {

    private final String origin;

    private final String destination;

    private final LocalDate date;

    private final LocalTime time;

    private final Integer price;

    private final Integer seats;

    public BestPrice(Train train, Price price) {
        this.origin = train.getOrigin().getCode();
        this.destination = train.getDestination().getCode();
        this.date = train.getDate();
        this.time = train.getTime();
        this.price = price.getValue();
        this.seats = price.getSeats();
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    public LocalDate getDate() {
        return date;
    }

    @JsonFormat(pattern = "KK:mm")
    public LocalTime getTime() {
        return time;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getSeats() {
        return seats;
    }

}
