package com.github.sergiords.ignite.client.part6_cluster;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterGroup;

@SuppressWarnings({"ConstantConditions", "unused"})
public class Step1_Cluster implements ClientStep {

    private final Ignite ignite;

    public Step1_Cluster(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public void run() {

        runInNodesWithLowCpuLoad();

        runInNodeWithAttributes();

    }

    private void runInNodesWithLowCpuLoad() {

        /*
         * TODO:
         * - get a cluster group with nodes whose CpuLoad is under 50%
         * - use IgniteCluster#forPredicate and ClusterNode#metrics to find appropriate nodes
         */
        ClusterGroup clusterGroup = null;

        /*
         * TODO:
         * - print "Hello cluster group" on all nodes from clusterGroup above
         * - use Ignite#compute#broadcast
         */
    }

    private void runInNodeWithAttributes() {

        /*
         * TODO:
         * - get a cluster group composed of nodes with attribute "node.id" = "Server1" or "node.id" = "Server2"
         * - yes attribute method exposes System.properties which are used throughout almost all hands on steps
         * - use IgniteCluster#forPredicate and ClusterNode#attribute to find appropriate nodes
         */
        ClusterGroup clusterGroup = null;

        /*
         * TODO:
         * - print "Hello node1 and node2" on all nodes from clusterGroup above
         * - use IgniteCompute#broadcast
         */
    }

    @Override
    public void close() {

    }

}
