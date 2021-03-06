package com.github.sergiords.ignite.data;

import java.util.Objects;

public class User {

    private final Team team;

    private final Integer id;

    private final String name;

    private final Integer commits;

    public User(Team team, Integer id, String name, Integer commits) {
        this.id = id;
        this.name = name;
        this.team = team;
        this.commits = commits;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCommits() {
        return commits;
    }

    @Override
    public boolean equals(Object that) {
        return that != null && that instanceof User && Objects.equals(id, ((User) that).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
