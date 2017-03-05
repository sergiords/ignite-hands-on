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
         * - return the uppercase value of System.getProperty("node.id") from only one node
         * - see IgniteCompute#call
         */
        return null;
    }

    private Collection<Long> getResultFromAllNodes() {

        /*
         * TODO:
         * - return the free memory from alls node
         * - use Runtime.getRuntime().freeMemory() and IgniteCompute#broadcast
         */
        return null;
    }

    private Collection<String> getResultFromTwoNodes() {

        /*
         * TODO:
         * - return uppercase thread name from a first node and lowercase thread name from a second node
         * - use Thread.currentThread().getName() and IgniteCompute#call
         */
        return null;
    }

    @Override
    public void close() {

    }

}
