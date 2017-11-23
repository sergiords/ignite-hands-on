package com.github.sergiords.ignite.client.part1_compute_grid;

import org.apache.ignite.Ignite;

public class Step1_Runnable {

    private final Ignite ignite;

    public Step1_Runnable(Ignite ignite) {
        this.ignite = ignite;
    }

    public void runInOneNode() {

        /*
         * TODO:
         * - use ignite.compute().run(...) to send a computation to one (random) server node
         * - call ServerApp.print("Hello Single Node") in computation
         */
    }

    public void runInAllNodes() {

        /*
         * TODO:
         * - use ignite.compute().broadcast(...) to send a computation to all server nodes
         * - call ServerApp.print("Hello All Nodes") in computation
         */
    }

    public void runInTwoNodes() {

        /*
         * TODO:
         * - use ignite.compute().run(...) to send two computations to two (random) server nodes
         * - call ServerApp.print("Hello First Node") in first computation
         * - call ServerApp.print("Hello Second Node") in second computation
         */
    }

}
