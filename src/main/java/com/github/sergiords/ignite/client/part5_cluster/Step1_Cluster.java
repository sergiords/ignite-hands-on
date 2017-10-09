package com.github.sergiords.ignite.client.part5_cluster;

import com.github.sergiords.ignite.server.ServerApp;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterGroup;

import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;

public class Step1_Cluster {

    private final Ignite ignite;

    public Step1_Cluster(Ignite ignite) {
        this.ignite = ignite;
    }

    public ClusterGroup customClusterGroup() {

        /*
         * TODO:
         * - get a cluster group composed of nodes with attribute "node.id" = "Server1" or "node.id" = "Server2"
         * - use ignite.cluster().forPredicate(...) to find appropriate nodes
         * FYI:
         * - ClusterNode.attribute() method exposes system property of remote server node
         */
        List<String> expectedNodeIds = asList("Server1", "Server2");
        return ignite.cluster().forPredicate(node -> expectedNodeIds.contains(node.<String>attribute("node.id")));
    }

    public Collection<String> runInCustomClusterGroup() {

        /*
         * TODO:
         * - run a computation returning Server.getName() for all nodes in previous custom cluster group
         * - use ignite.compute(...).broadcast(...)
         */
        return ignite.compute(customClusterGroup()).broadcast(ServerApp::getName);
    }

}
