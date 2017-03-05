package com.github.sergiords.ignite;

import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.EventType;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.DiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.spi.failover.FailoverSpi;
import org.apache.ignite.spi.failover.always.AlwaysFailoverSpi;
import org.apache.ignite.spi.loadbalancing.LoadBalancingSpi;
import org.apache.ignite.spi.loadbalancing.roundrobin.RoundRobinLoadBalancingSpi;

import java.util.Collections;

/**
 * Ignite configuration for server and client nodes.
 */
public class Config {

    /*
     * ==============================================
     * DO NOT EDIT configuration bellow this comment.
     * ==============================================
     */

    /*
     * Discovery SPI: how nodes discover each other in the cluster
     */
    private static DiscoverySpi discoverySpi() {

        TcpDiscoveryVmIpFinder ipFinder = new TcpDiscoveryVmIpFinder();
        ipFinder.setAddresses(Collections.singleton("localhost:47500..47509"));

        TcpDiscoverySpi discoverySpi = new TcpDiscoverySpi();
        discoverySpi.setLocalAddress("localhost");
        discoverySpi.setLocalPort(47500);
        discoverySpi.setLocalPortRange(100);
        discoverySpi.setIpFinder(ipFinder);
        discoverySpi.setJoinTimeout(5_000);
        return discoverySpi;
    }

    /*
     * Communication SPI: how nodes send tasks to each other
     */
    private static TcpCommunicationSpi communicationSpi() {

        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setLocalAddress("localhost");
        communicationSpi.setLocalPort(47100);
        communicationSpi.setLocalPortRange(100);
        communicationSpi.setSharedMemoryPort(-1); // disable shared memory because limits are low on Mac
        return communicationSpi;
    }

    private static FailoverSpi failoverSpi() {

        AlwaysFailoverSpi failoverSpi = new AlwaysFailoverSpi();
        failoverSpi.setMaximumFailoverAttempts(5);
        return failoverSpi;
    }

    private static LoadBalancingSpi loadBalancingSpi() {

        return new RoundRobinLoadBalancingSpi();
    }

    public static IgniteConfiguration igniteConfiguration() {

        String nodeId = System.getProperty("node.id", "default");

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setConsistentId(nodeId);

        // allows loading remote nodes classes, but exclude lambdas from hands on part
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setPeerClassLoadingLocalClassPathExclude(
            "com.github.sergiords.ignite.client.*",
            "com.github.sergiords.ignite.client.part1_compute_grid.*",
            "com.github.sergiords.ignite.client.part2_data_grid.*",
            "com.github.sergiords.ignite.client.part3_affinity.*",
            "com.github.sergiords.ignite.client.part4_service.*",
            "com.github.sergiords.ignite.client.part5_messaging.*",
            "com.github.sergiords.ignite.client.part6_cluster.*"
        );

        // disable metrics for hands on
        igniteConfiguration.setMetricsLogFrequency(0);

        // configure a few SPIs
        igniteConfiguration.setDiscoverySpi(discoverySpi());
        igniteConfiguration.setCommunicationSpi(communicationSpi());
        igniteConfiguration.setFailoverSpi(failoverSpi());
        igniteConfiguration.setLoadBalancingSpi(loadBalancingSpi());

        // enable some events
        igniteConfiguration.setIncludeEventTypes(EventType.EVT_JOB_FINISHED, EventType.EVT_CACHE_OBJECT_PUT);

        return igniteConfiguration;
    }
}
