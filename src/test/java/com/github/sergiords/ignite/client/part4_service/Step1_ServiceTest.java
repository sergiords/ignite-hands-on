package com.github.sergiords.ignite.client.part4_service;

import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.services.ServiceDescriptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(ServerAppTest.class)
class Step1_ServiceTest {

    private final Ignite ignite;

    private final Step1_Service step;

    public Step1_ServiceTest(Ignite ignite) {
        this.ignite = ignite;
        this.step = new Step1_Service(ignite);
    }

    @TestFactory
    @DisplayName("cluster service")
    List<DynamicTest> clusterService() {

        ignite.services().cancelAll();

        step.deployClusterService();

        ServiceDescriptor serviceDescriptor = getServiceDescriptor();

        return asList(
            dynamicTest("should be deployed in one node only", () -> {
                assertThat(serviceDescriptor.totalCount()).isEqualTo(1);
                assertThat(serviceDescriptor.maxPerNodeCount()).isEqualTo(1);
                assertThat(serviceDescriptor.topologySnapshot()).hasSize(3).containsValues(1, 0, 0);
            }),
            computeInClient(),
            computeInCallable()
        );
    }

    @TestFactory
    @DisplayName("node service")
    List<DynamicTest> nodeService() {

        ignite.services().cancelAll();

        step.deployNodeService();

        ServiceDescriptor serviceDescriptor = getServiceDescriptor();

        return asList(
            dynamicTest("should be deployed in each node", () -> {
                assertThat(serviceDescriptor.totalCount()).isEqualTo(0);
                assertThat(serviceDescriptor.maxPerNodeCount()).isEqualTo(1);
                assertThat(serviceDescriptor.topologySnapshot()).hasSize(3).containsValues(1, 1, 1);
            }),
            computeInClient(),
            computeInCallable()
        );
    }

    @TestFactory
    @DisplayName("custom service")
    List<DynamicTest> customService() {

        ignite.services().cancelAll();

        step.deployCustomService();

        ServiceDescriptor serviceDescriptor = getServiceDescriptor();

        return asList(
            dynamicTest("should be deployed 5 times with 2 instances max per node", () -> {
                assertThat(serviceDescriptor.totalCount()).isEqualTo(5);
                assertThat(serviceDescriptor.maxPerNodeCount()).isEqualTo(2);
                assertThat(serviceDescriptor.topologySnapshot()).hasSize(3).containsValues(1, 2, 2);
            }),
            computeInClient(),
            computeInCallable()
        );
    }

    private ServiceDescriptor getServiceDescriptor() {
        return ignite.services()
            .serviceDescriptors().stream()
            .filter(serviceDescriptor -> "my-service".equals(serviceDescriptor.name()))
            .findFirst().orElseThrow(() -> new AssertionError("my-service is not deployed"));
    }

    private DynamicTest computeInClient() {
        return dynamicTest("should compute sum using proxy", () ->
            assertThat(step.runServiceUsingProxy(1, 2)).isEqualTo(3));
    }

    private DynamicTest computeInCallable() {
        return dynamicTest("should compute sum using proxy inside a callable", () ->
            assertThat(step.runServiceInCallable(1, 2)).isEqualTo(3));
    }

}
