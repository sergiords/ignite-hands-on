package com.github.sergiords.ignite.client.part4_service;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

public class Step1_ClusterService implements ClientStep {

    private static final String SERVICE_NAME = "my-cluster-service";

    private final Ignite ignite;

    public Step1_ClusterService(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - deploy a service named "my-cluster-service" using an instance of ComputerService
         * - see: IgniteServices#deployClusterSingleton
         */
        ignite.services().deployClusterSingleton(SERVICE_NAME, new ComputerService());
    }

    @Override
    public void run() {

        Integer[] numbers = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};

        Integer sumUsingProxy = runServiceUsingProxy(numbers);

        Integer sumUsingCallable = runServiceInCallable(numbers);

        System.out.printf("=== Cluster Service ===%n");
        System.out.printf("Sum using proxy: %d%n", sumUsingProxy);
        System.out.printf("Sum using callable: %d%n", sumUsingCallable);
    }

    private Integer runServiceUsingProxy(Integer... numbers) {

        /*
         * TODO:
         * - get a proxy instance of Computer (here in client node) for the cluster-singleton service deployed above
         * - proxy should not be sticky
         * - use: IgniteServices#serviceProxy
         */
        Computer computer = ignite.services().serviceProxy(SERVICE_NAME, Computer.class, false);

        /*
         * TODO:
         * - use service proxy instance and call service method to return numbers sum
         */
        return computer.sum(numbers);
    }

    private Integer runServiceInCallable(Integer... numbers) {

        return ignite.compute().call(() -> {

            /*
             * TODO:
             * - get the service-proxy instance (here in server-node)
             * - proxy should not be sticky
             * - use: IgniteServices#serviceProxy
             * TIP:
             * - we need a proxy because service might not be deployed in node this callable is actually running into
             */
            Computer computerProxy = ignite.services().serviceProxy(SERVICE_NAME, Computer.class, false);

            /*
             * TODO:
             * - use proxy to return the sum of numbers
             */
            return computerProxy.sum(numbers);
        });
    }

    @Override
    public void close() {
        ignite.services().cancel(SERVICE_NAME);
    }

}
