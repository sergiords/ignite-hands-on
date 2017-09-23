package com.github.sergiords.ignite.client.part1_compute_grid;

import com.github.sergiords.ignite.server.ServerApp;
import org.apache.ignite.Ignite;

import java.util.Collection;

import static java.util.Arrays.asList;

public class Step2_Callable {

    private final Ignite ignite;

    public Step2_Callable(Ignite ignite) {
        this.ignite = ignite;
    }

    public String getResultFromOneNode() {

        /*
         * TODO:
         * - return the uppercase value of System.getProperty("node.id") from only one node
         * - see IgniteCompute#call
         */
        return ignite.compute().call(ServerApp::getName);
    }

    public Collection<String> getResultFromAllNodes() {

        /*
         * TODO:
         * - return the node server node names
         * - use ServerApp.getName() and ignite.compute().broadcast()
         */
        return ignite.compute().broadcast(ServerApp::getName);
    }

    public Collection<String> getResultFromTwoNodes() {

        /*
         * TODO:
         * - return uppercase thread name from a first node and lowercase thread name from a second node
         * - use Thread.currentThread().getName() and IgniteCompute#call
         */
        return ignite.compute().call(asList(
            ServerApp::getName,
            ServerApp::getThreadName
        ));
    }

}
