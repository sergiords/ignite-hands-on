package com.github.sergiords.ignite.client.part5_cluster;

import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ServerAppTest.class)
class Step1_ClusterTest {

    private final Ignite ignite;

    private final Step1_Cluster step;

    Step1_ClusterTest(Ignite ignite) {
        this.ignite = ignite;
        this.step = new Step1_Cluster(ignite);
    }

    @Test
    @DisplayName("customClusterGroup() should return a group with Server1 and Server2 nodes only")
    void customClusterGroup() {

        ClusterGroup result = step.customClusterGroup();

        assertThat(result).isNotNull();
        assertThat(result.nodes())
            .hasSize(2)
            .extracting(ClusterNode::consistentId)
            .containsOnly("Server1", "Server2");
    }

    @Test
    @DisplayName("runInCustomClusterGroup() should return ServerApp.getName() from Server1 and Server2 nodes only")
    void runInCustomClusterGroup() {

        Collection<String> result = step.runInCustomClusterGroup();

        assertThat(result)
            .hasSize(2)
            .containsOnly("Server1", "Server2");
    }

}
