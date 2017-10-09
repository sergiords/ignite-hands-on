package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(ServerAppTest.class)
class Step4_DataAffinityTest {

    private final Ignite ignite;

    private final Step4_DataAffinity step;

    public Step4_DataAffinityTest(Ignite ignite) {
        this.ignite = ignite;
        this.step = new Step4_DataAffinity(ignite);
    }

    @TestFactory
    @DisplayName("Data affinity")
    List<DynamicTest> computeAffinity() {

        String name = "my-data-affinity-cache";
        IgniteCache<Team, List<User>> cache = ignite.cache(name);

        Team team42 = Data.teams().get(42);

        return asList(

            dynamicTest("populateCache() should store 1000 teams in cache", () -> {
                step.populateCache();
                assertThat(cache.size()).isEqualTo(1000);
            }),

            dynamicTest("affinityKey() should return key for team and its country", () -> {
                AffinityKey<Team> result = step.affinityKey(team42);
                assertThat(result).isNotNull();
                assertThat(result.key()).isEqualTo(team42);
                assertThat(result.<String>affinityKey()).isEqualTo(team42.getCountry());
            }),

            dynamicTest("findTopCommitterFromCountry() should return top committer from country", () -> {
                Optional<User> topCommitterFromCountry = step.findTopCommitterFromCountry(Locale.FRANCE.getDisplayCountry());
                assertThat(topCommitterFromCountry)
                    .isNotEmpty().map(User::getId).contains(423);
            })

        );
    }

}
