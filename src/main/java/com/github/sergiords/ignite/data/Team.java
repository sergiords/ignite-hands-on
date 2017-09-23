package com.github.sergiords.ignite.data;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

public class Team {

    private final Integer id;

    @QuerySqlField
    @QueryTextField
    private final String name;

    private final String country;

    public Team(Integer id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
