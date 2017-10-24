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
    @DisplayName("Should execute lambda with affinity (in server node hosting data as primary key)")
    List<DynamicTest> computeAffinity() {

        Step3_ComputeAffinity step = new Step3_ComputeAffinity(ignite);

        String name = "my-compute-affinity-cache";
        IgniteCache<Team, List<User>> cache = ignite.cache(name);
        Affinity<Team> affinity = ignite.affinity(name);

        Team team42 = Data.teams().get(42);
        ClusterNode team42Node = affinity.mapKeyToNode(team42);
        String team42NodeName = team42Node.attribute("node.id");

        return asList(

            dynamicTest("cache should have 1000 teams", () -> assertThat(cache.size()).isEqualTo(1000)),

            dynamicTest("findTopCommitter() should be executed on node " + team42NodeName, () ->
                assertThat(step.findTopCommitter(team42))
                    .hasValueSatisfying(user -> assertThat(user.getId()).isEqualTo(423))),

            dynamicTest("findTopCommitterFullVersion() should be executed on node " + team42NodeName, () ->
                assertThat(step.findTopCommitterFullVersion(team42))
                    .hasValueSatisfying(user -> assertThat(user.getId()).isEqualTo(423)))
        );
    }

}
