package com.github.sergiords.ignite.data;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Data {

    private static final List<String> COUNTRIES = Stream
        .of(Locale.FRANCE, Locale.CHINA, Locale.UK, Locale.CANADA, Locale.US, Locale.GERMANY, Locale.ITALY,
            Locale.JAPAN, Locale.KOREA, Locale.forLanguageTag("pt-PT"))
        .map(locale -> locale.getDisplayCountry(Locale.ENGLISH))
        .collect(Collectors.toList());

    public static List<Team> teams() {
        return IntStream.range(0, 1000).boxed()
            .map(id -> new Team(id, "Team" + id, COUNTRIES.get(id % COUNTRIES.size())))
            .collect(Collectors.toList());
    }

    public static List<User> users(Team team) {
        return IntStream.range(0, 1000).boxed()
            .map(id -> new User(team, id, "User" + id, (id * id) % 1234))
            .collect(Collectors.toList());
    }
}
