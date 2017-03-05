package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
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
    }

    private void runInAllNodes() {

        /*
         * TODO:
         * - print "Hello Every Node" in each node
         * - use IgniteCompute#broadcast
         */
    }

    private void runInTwoNodes() {

        /*
         * TODO:
         * - print "Hello First Node" in one node and "Hello Second Node" in another node in only one computation call
         * - use IgniteCompute#run
         */
    }

    @Override
    public void close() {

    }

}
