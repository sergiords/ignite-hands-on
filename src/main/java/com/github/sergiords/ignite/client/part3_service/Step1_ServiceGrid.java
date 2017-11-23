package com.github.sergiords.ignite.client.part3_service;

import com.github.sergiords.ignite.server.Computer;
import org.apache.ignite.Ignite;

public class Step1_ServiceGrid {

    private static final String SERVICE_NAME = "my-service";

    private final Ignite ignite;

    public Step1_ServiceGrid(Ignite ignite) {

        this.ignite = ignite;
    }

    public void deployClusterService() {

        /*
         * TODO:
         * - deploy a cluster singleton service named "my-service" using a new instance of ComputerService
         * - use ignite.services().deployClusterSingleton(...)
         */
    }

    public Integer runServiceUsingProxy(Integer x, Integer y) {

        /*
         * TODO:
         * - get a proxy instance of Computer
         * - proxy should not be sticky
         * - use ignite.services().serviceProxy(...)
         * TIP:
         * - this call is executed in a client node (started with tests)
         * - since services are deployed on server nodes, proxy forwards calls to those remote services
         */
        Computer computer = null;

        /*
         * TODO:
         * - use proxy to compute sum(x, y)
         */
        return null;
    }

    public Integer runServiceInCallable(Integer x, Integer y) {

        return ignite.compute().call(() -> {

            /*
             * TODO:
             * - get a service-proxy instance
             * - proxy should not be sticky
             * - use ignite.services().serviceProxy(...)
             * TIP:
             * - this call is executed in a server node (inside an IgniteCallable)
             * - if service is deployed on local node proxy uses local service otherwise it forwards calls to some other node service
             */
            Computer computerProxy = null;

            /*
             * TODO:
             * - use proxy to compute sum(x, y)
             */
            return null;
        });
    }

    public void deployNodeService() {

        /*
         * TODO:
         * - deploy a node singleton service named "my-service" using a new instance of ComputerService
         * - use ignite.services().deployNodeSingleton(...)
         */
    }

    public void deployCustomService() {

        /*
         * TODO:
         * - create a custom ServiceConfiguration instance
         * - set the name of the service to be deployed to "my-service"
         * - set the maxPerNodeCount to 2 (ie. 2 instances per node at most)
         * - set the totalCount to 5 (ie. 5 instances in cluster)
         * - set the service to an instance of ComputerService
         * - set the node filter to exclude client nodes from deployment targets
         * - deploy this service configuration
         * - use ignite.services().deploy(...)
         * TIP:
         * - notice this can be used to deploy same configurations as deployClusterSingleton() and deployNodeSingleton()
         */

    }

    public void deployAffinityService() {

        /*
         * TODO:
         * - create a cache named "my-cache"
         */

        /*
         * TODO:
         * - deploy a service named "my-service"
         * - this service should be deployed only on node where "Key42" is stored for cache named "my-cache"
         * - use ignite.services().deployKeyAffinitySingleton(...)
         */
    }

}
