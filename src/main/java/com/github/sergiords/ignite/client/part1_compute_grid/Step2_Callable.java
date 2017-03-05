package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

import java.util.Collection;

@SuppressWarnings({"unused", "FieldCanBeLocal", "ConstantConditions"})
public class Step2_Callable implements ClientStep {

    private final Ignite ignite;

    public Step2_Callable(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public void run() {

        String resultFromOneNode = getResultFromOneNode();

        Collection<Long> resultFromAllNodes = getResultFromAllNodes();

        Collection<String> resultFromTwoNodes = getResultFromTwoNodes();

        System.out.printf("===== IgniteCompute Callables =====%n");
        System.out.printf("=> Result from one node is: %s%n", resultFromOneNode);
        System.out.printf("=> Result from all nodes is: %s%n", resultFromAllNodes);
        System.out.printf("=> Result from two nodes is: %s%n", resultFromTwoNodes);

    }

    private String getResultFromOneNode() {

        /*
         * TODO:
         * - get and return the uppercase value of System.getProperty("node.id") from only one node
         * - see IgniteCompute#call
         */
        return null;
    }

    private Collection<Long> getResultFromAllNodes() {

        /*
         * TODO:
         * - get and return the percentage of available memory from each node
         * - use Runtime.getRuntime().freeMemory()
         * - use Runtime.getRuntime().totalMemory()
         * - see IgniteCompute#broadcast
         */
        return null;
    }

    private Collection<String> getResultFromTwoNodes() {

        /*
         * TODO:
         * - get and return uppercase thread name from a first node and lowercase thread name from a second node
         * - use Thread.currentThread().getName()
         * - see IgniteCompute#call
         */

        return null;
    }

    @Override
    public void close() {

    }

}
