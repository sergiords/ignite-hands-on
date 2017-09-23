package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.server.ServerApp;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ServerAppTest.class)
class Step1_RunnableTest {

    private final Ignite ignite;

    private final Step1_Runnable step1;

    public Step1_RunnableTest(Ignite ignite) {
        this.ignite = ignite;
        this.step1 = new Step1_Runnable(ignite);
    }

    @Test
    @DisplayName("Should run one lambda in one server node only")
    void testRunInOnNode() {

        step1.runInOneNode();

        assertThat(ignite.compute().broadcast(ServerApp::watch)).containsOnly(null, null, "Hello Single Node");
    }

    @Test
    @DisplayName("Should run one lambda in all server nodes")
    void testRunInAllNodes() {

        step1.runInAllNodes();

        assertThat(ignite.compute().broadcast(ServerApp::watch))
            .containsOnly("Hello Every Node", "Hello Every Node", "Hello Every Node");
    }

    @Test
    @DisplayName("Should run two lambdas in two server nodes")
    void testRunInTwoNodes() {

        step1.runInTwoNodes();

        assertThat(ignite.compute().broadcast(ServerApp::watch))
            .containsOnly(null, "Hello First Node", "Hello Second Node");
    }
}
