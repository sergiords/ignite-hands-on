package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.server.ServerApp;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ServerAppTest.class)
class Step2_CallableTest {

    private final Ignite ignite;

    private final Step2_Callable step2;

    public Step2_CallableTest(Ignite ignite) {
        this.ignite = ignite;
        this.step2 = new Step2_Callable(ignite);
    }

    @Test
    @DisplayName("Should get node name from one server node")
    void getResultFromOneNode() {

        String result = step2.getResultFromOneNode();

        assertThat(result).isNotEmpty();
        assertThat(ignite.compute().broadcast(ServerApp::watch)).containsOnly(result, null, null);
    }

    @Test
    @DisplayName("Should get node name from all server nodes")
    void getResultFromAllNodes() {

        Collection<String> result = step2.getResultFromAllNodes();

        assertThat(result).hasSize(3);
        assertThat(ignite.compute().broadcast(ServerApp::watch)).containsAll(result);
    }

    @Test
    @DisplayName("Should get node name and thread name from two server nodes in one call only")
    void getResultFromTwoNodes() {

        Collection<String> result = step2.getResultFromTwoNodes();

        assertThat(result).hasSize(2);
        assertThat(ignite.compute().broadcast(ServerApp::watch)).containsAll(result);
    }

}
