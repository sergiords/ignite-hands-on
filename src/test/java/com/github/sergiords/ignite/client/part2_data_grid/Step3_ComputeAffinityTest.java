package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(ServerAppTest.class)
class Step3_ComputeAffinityTest {

    private final Ignite ignite;

    public Step3_ComputeAffinityTest(Ignite ignite) {
        this.ignite = ignite;
    }

    @BeforeEach
    void setUp() {
        ignite.destroyCaches(ignite.cacheNames());
    }

    @TestFactory
    @DisplayName("compute affinity")
    List<DynamicTest> computeAffinity() {

        Step3_ComputeAffinity step = new Step3_ComputeAffinity(ignite);

        String name = "my-compute-affinity-cache";
        IgniteCache<Team, List<User>> cache = ignite.cache(name);

        Team team42 = Data.teams().get(42);

        return asList(

            dynamicTest("populateCache() should store 1000 teams in cache", () -> {
                assertThat(cache).isNotNull();
                assertThat(cache.getName()).isEqualTo(name);
                step.populateCache();
                assertThat(cache.size()).isEqualTo(1000);
            }),

            dynamicTest("findTopCommitter() should find top committer", () -> {
                Optional<User> result = step.findTopCommitter(team42);
                assertThat(result).hasValueSatisfying(user -> assertThat(user.getId()).isEqualTo(423));
            }),

            dynamicTest("findNode() should return node where team is stored", () -> {
                assertThat(cache).isNotNull();
                assertThat(cache.getName()).isEqualTo(name);
                Affinity<Team> affinity = ignite.affinity(name);
                ClusterNode expected = affinity.mapKeyToNode(team42);
                ClusterNode result = step.findNode(team42);
                assertThat(result).isEqualTo(expected);
            }),

            dynamicTest("findTopCommitterFullVersion() should find top committer", () -> {
                Optional<User> result = step.findTopCommitterFullVersion(team42);
                assertThat(result).hasValueSatisfying(user -> assertThat(user.getId()).isEqualTo(423));
            })

        );
    }

}
