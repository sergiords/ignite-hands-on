package com.github.sergiords.ignite.client.part1_compute_grid;

import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterGroup;

import java.util.Collection;

public class Step3_Cluster {

    private final Ignite ignite;

    public Step3_Cluster(Ignite ignite) {
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
        return null;
    }

    public Collection<String> runInCustomClusterGroup() {

        /*
         * TODO:
         * - run a computation returning Server.getName() for all nodes in previous custom cluster group
         * - use ignite.compute(...).broadcast(...)
         */
        return null;
    }

}
