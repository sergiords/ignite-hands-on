package com.github.sergiords.ignite.client.part1_compute_grid;

import org.apache.ignite.Ignite;

import java.util.Collection;

public class Step2_Callable {

    private final Ignite ignite;

    public Step2_Callable(Ignite ignite) {
        this.ignite = ignite;
    }

    public String getResultFromOneNode() {

        /*
         * TODO:
         * - use ignite.compute().call(...) to return a computation result from one (random) server node
         * - return ServerApp.getName() in computation
         */
        return null;
    }

    public Collection<String> getResultFromAllNodes() {

        /*
         * TODO:
         * - use ignite.compute().broadcast(...) to return computation results from all server nodes
         * - return ServerApp.getName() in computation
         */
        return null;
    }

    public Collection<String> getResultFromTwoNodes() {

        /*
         * TODO:
         * - use ignite.compute().call(...) to return computation results from two server nodes
         * - return ServerApp.getName() in first computation
         * - return ServerApp.getInfo() in second computation
         */
        return null;
    }

}
