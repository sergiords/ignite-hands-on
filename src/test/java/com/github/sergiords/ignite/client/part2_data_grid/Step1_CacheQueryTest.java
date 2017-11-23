package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(ServerAppTest.class)
public class Step1_CacheQueryTest {

    private final Ignite ignite;

    public Step1_CacheQueryTest(Ignite ignite) {
        this.ignite = ignite;
    }

    @BeforeEach
    void setUp() {
        ignite.destroyCaches(ignite.cacheNames());
    }

    @TestFactory
    @DisplayName("cache query step")
    List<DynamicTest> cacheQuery() {

        Step1_CacheQuery step = new Step1_CacheQuery(ignite);

        IgniteCache<Integer, Team> cache = ignite.cache("my-cache");

        return asList(

            dynamicTest("should create a cache named \"my-cache\"", () -> {
                assertThat(cache).isNotNull();
                assertThat(cache.getName()).isEqualTo("my-cache");
            }),

            dynamicTest("should write 1000 values to cache", () -> {
                step.populateCache();
                assertThat(cache).isNotNull();
                assertThat(cache.size()).isEqualTo(1000);
            }),

            dynamicTest("should find team by id", () -> {
                Team result = step.findByKey(42);
                assertThat(result).isNotNull();
                assertThat(result.getId()).isEqualTo(42);
            }),

            dynamicTest("should find and process team by id", () -> {
                String result = step.findByKeyAndProcess(99);
                assertThat(result).isNotNull().isEqualTo("TEAM99");
            }),

            dynamicTest("should find teams with scan query", () -> {
                List<Team> result = step.findByScanQuery("99");
                assertThat(result).hasSize(10).extracting(Team::getId).allMatch(id -> id % 100 == 99);
            }),

            dynamicTest("should find teams with text query", () -> {
                List<Team> result = step.findByTextQuery("Team*99");
                assertThat(result).hasSize(10).extracting(Team::getId).allMatch(id -> id % 100 == 99);
            }),

            dynamicTest("should find teams with sql query", () -> {
                List<Team> result = step.findBySqlQuery("%99");
                assertThat(result).hasSize(10).extracting(Team::getId).allMatch(id -> id % 100 == 99);
            })

        );
    }

}
