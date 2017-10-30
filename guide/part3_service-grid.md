# Service Grid

![img](img/service-grid.png)

**Services** are used in **computations** to avoid heavy lambda serializations.

Ignite allows deploying multiple instances of a given **Service** across the cluster and ensures **proper deployment** and **fault tolerance** of deployed services.

```java
import org.apache.ignite.*;
import org.apache.ignite.services.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ignite.services().deployClusterSingleton("my-service", new MyService());

        String value = ignite.services().service("my-service").myServiceComputation();
    }
}
```

Service deployment can be adjusted by 4 means:

- **cluster service**: **only one instance** is deployed across **all nodes**.
- **Node service**: **one instance** is deployed in **each node**.
- **Custom service**: **x instances** are deployed in **every node** with **y** instances maximum per node.
- **Affinity service**: service is deployed according to cache entry location (**service is collocated with data**)

>Complete **TODO**s in **Step1_ServiceGrid** to fix all tests in **Step1_ServiceGridTest**.


[Home](../readme.md) | [Back](./part2_data-grid.md) | [Next](part4_messaging.md)
