package com.github.sergiords.ignite.data;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CacheData {

    static {
        Locale.setDefault(Locale.ENGLISH);
    }

    private static final Locale[] LOCALES = {
        Locale.FRANCE, Locale.CHINA, Locale.UK, Locale.CANADA, Locale.US,
        Locale.GERMANY, Locale.ITALY, Locale.JAPAN, Locale.KOREA, Locale.forLanguageTag("pt-PT")
    };

    public static List<Team> teams() {
        return IntStream.range(0, 1000).boxed().map(id -> new Team(id, "Team" + id)).collect(Collectors.toList());
    }

    public static List<String> getCountries() {
        return Stream.of(LOCALES).map(Locale::getDisplayCountry).collect(Collectors.toList());
    }

}
