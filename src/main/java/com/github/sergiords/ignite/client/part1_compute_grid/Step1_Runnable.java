package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.server.ServerApp;
import org.apache.ignite.Ignite;

import static java.util.Arrays.asList;

public class Step1_Runnable {

    private final Ignite ignite;

    public Step1_Runnable(Ignite ignite) {
        this.ignite = ignite;
    }

    public void runInOneNode() {

        /*
         * TODO:
         * - print "Hello Single Node" in only one node
         * - use IgniteCompute#run
         */
        ignite.compute().run(() -> ServerApp.print("Hello Single Node"));
    }

    public void runInAllNodes() {

        /*
         * TODO:
         * - print "Hello Every Node" in each node
         * - use IgniteCompute#broadcast
         */
        ignite.compute().broadcast(() -> ServerApp.print("Hello Every Node"));
    }

    public void runInTwoNodes() {

        /*
         * TODO:
         * - print "Hello First Node" in one node and "Hello Second Node" in another node in only one computation call
         * - use IgniteCompute#run
         */
        ignite.compute().run(asList(
            () -> ServerApp.print("Hello First Node"),
            () -> ServerApp.print("Hello Second Node")
        ));
    }

}
