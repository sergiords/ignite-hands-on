package com.github.sergiords.ignite.client.part4_service;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

@SuppressWarnings("ConstantConditions")
public class Step3_CustomService implements ClientStep {

    private static final String SERVICE_NAME = "my-custom-service";

    private final Ignite ignite;

    public Step3_CustomService(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a custom ServiceConfiguration instance
         * - set the name of the service to be deployed to "my-custom-service"
         * - set the maxPerNodeCount to 2
         * - set the totalCount to 5
         * - set the service to an instance of ComputerService
         * - deploy this service configuration
         * - see: IgniteServices#deploy
         * TIP:
         * - previous deployClusterSingleton and deployNodeSingleton methods are ServiceConfiguration shortcuts
         */
    }

    @Override
    public void run() {

        Integer[] numbers = {0, 100, 200, 300, 400, 500, 600, 700, 800, 900};

        Integer sumUsingProxy = runServiceUsingProxy(numbers);

        Integer sumUsingCallable = runServiceInCallable(numbers);

        Integer serviceCount = serviceCount();

        System.out.printf("=== Custom Service ===%n");
        System.out.printf("Sum using proxy: %d%n", sumUsingProxy);
        System.out.printf("Sum using callable: %d%n", sumUsingCallable);
        System.out.printf("=> Service count: %d%n", serviceCount);
    }

    private Integer runServiceUsingProxy(Integer[] numbers) {

        /*
         * TODO:
         * - get a proxy instance of Computer (in client node) for the service deployed above
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
             * - like with node service we do not need a proxy here
             * - server nodes have at least one or two locally deployed services (total = 5, maxPerNode = 2)
             */
            Computer computer = null;

            /*
             * TODO:
             * - use service to return the sum of numbers
             */
            return null;
        });
    }

    private Integer serviceCount() {

        /*
         * TODO:
         * - return number of services named "my-custom-service" deployed across the cluster
         */
        return null;
    }

    @Override
    public void close() {
        ignite.services().cancel(SERVICE_NAME);
    }

}
