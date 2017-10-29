# Service Grid

![img](img/service-grid.png)

**Services** are used in **computations** to avoid heavy lambda serializations.

Ignite allows deploying multiple instances of a given **Service** across the cluster.

Ignite ensures **proper deployment** and **fault tolerance** of deployed services. 


## Example

```java
import org.apache.ignite.*;
import org.apache.ignite.services.*;

public class App {
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ServiceConfiguration configuration = new ServiceConfiguration();
        configuration.setService(new MyService());
        configuration.setName("my-service");
        configuration.setTotalCount(6);
        configuration.setMaxPerNodeCount(3);

        ignite.services().deploy(configuration);

        String value = ignite.services().service("my-service").myServiceComputation();
    }
}
```

Here Ignite deploys **6 instances** across the cluster with **3 instances max per node**. 

`myServiceComputation` method is then called on a **locally deployed service instance**.


## Exercise

Service deployment can be adjusted by 4 means:

- **cluster service**: **only one instance** is deployed across **all nodes**.
- **Node service**: **one instance** is deployed in **each node**.
- **Custom service**: **x instances** are deployed in **every node** with **y** instances maximum per node.
- **Affinity service**: service is deployed according to cache entry location (**service is collocated with data**)

Complete TODOs in **Step3_ServiceGrid** class and make all tests in **Step3_ServiceGridTest** pass.


[Home](../readme.md) | [Back](./part2_data-grid.md) | [Next](part4_messaging.md)
