package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

import static java.util.Arrays.asList;

public class Step1_Runnable implements ClientStep {

    private final Ignite ignite;

    public Step1_Runnable(Ignite ignite) {
        this.ignite = ignite;
    }

    @Override
    public void run() {

        runInOneNode();

        runInAllNodes();

        runInTwoNodes();
    }

    private void runInOneNode() {

        /*
         * TODO:
         * - print "Hello Single Node" in only one node
         * - use IgniteCompute#run
         */
        ignite.compute().run(() -> System.out.println("Hello Single Node"));
    }

    private void runInAllNodes() {

        /*
         * TODO:
         * - print "Hello Every Node" in each node
         * - use IgniteCompute#broadcast
         */
        ignite.compute().broadcast(() -> System.out.println("Hello Every Node"));
    }

    private void runInTwoNodes() {

        /*
         * TODO:
         * - print "Hello First Node" in one node and "Hello Second Node" in another node in only one computation call
         * - use IgniteCompute#run
         */
        ignite.compute().run(asList(
            () -> System.out.println("Hello First Node"),
            () -> System.out.println("Hello Second Node")
        ));
    }

    @Override
    public void close() {

    }

}
