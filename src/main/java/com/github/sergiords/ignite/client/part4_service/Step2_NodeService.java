package com.github.sergiords.ignite.client.part4_service;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

@SuppressWarnings({"unused", "ConstantConditions"})
public class Step2_NodeService implements ClientStep {

    private static final String SERVICE_NAME = "my-node-service";

    private final Ignite ignite;

    public Step2_NodeService(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create and deploy a service named "my-node-service" using an instance of ComputerService
         * - see: IgniteServices#deployNodeSingleton
         */
    }

    @Override
    public void run() {

        Integer[] numbers = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90};

        Integer sumUsingProxy = runServiceUsingProxy(numbers);

        Integer sumUsingCallable = runServiceInCallable(numbers);

        System.out.printf("=== Node Service ===%n");
        System.out.printf("Sum using proxy: %d%n", sumUsingProxy);
        System.out.printf("Sum using callable: %d%n", sumUsingCallable);
    }

    private Integer runServiceUsingProxy(Integer[] numbers) {

        /*
         * TODO:
         * - get a proxy instance of Computer (in client node) for the node-singleton service deployed above
         * - proxy should not be sticky
         * - use: IgniteServices#serviceProxy
         * TIP:
         * - we still need a proxy here because this code is running in client
         */
        Computer computer = null;

        /*
         * TODO:
         * - use service proxy instance and call service method to return numbers sum
         */
        return null;
    }

    private Integer runServiceInCallable(Integer[] numbers) {

        return ignite.compute().call(() -> {

            /*
             * TODO:
             * - get a locally deployed service instance
             * - use: IgniteServices#service
             * TIP:
             * - in contrast with cluster service we do not need a service proxy here
             * - service is deployed in each server node so we can use local service instance
             */
            Computer computer = null;

            /*
             * TODO:
             * - use service to return the sum of numbers
             */
            return null;
        });
    }

    @Override
    public void close() {
        ignite.services().cancel(SERVICE_NAME);
    }

}
