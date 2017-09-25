package com.github.sergiords.ignite.data;

import org.apache.ignite.cache.query.annotations.QuerySqlField;
import org.apache.ignite.cache.query.annotations.QueryTextField;

import java.util.Objects;

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

    @Override
    public boolean equals(Object that) {
        return that != null && that instanceof Team && Objects.equals(id, ((Team) that).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
