# Cluster

This part describes how **ClusterNodes** can be used and grouped in **ClusterGroup** instances.


## Cluster Nodes

**ClusterNode** class represents **a node** in the cluster.

An instance can be retrieved like this:

```java
import org.apache.ignite.*;
import org.apache.ignite.cluster.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ClusterNode clusterNode = ignite.cluster().localNode();

        double jobExecuteTime = clusterNode.metrics().getAverageJobExecuteTime();
    }
}
```


## Cluster Groups

**ClusterGroup** class represents **a set of nodes** in the cluster.

It can be used to **restrict** the set of nodes where an operation is executed. An instance can be retrieved like this:
```java
import org.apache.ignite.*;
import org.apache.ignite.cluster.*;

public class App {
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ClusterGroup clusterGroup = ignite.cluster().forServers();

        ignite.compute(clusterGroup).run(() -> System.out.println("Hello Server Nodes"));
    }
}
```

>Complete **TODO**s in **Step1_Cluster** to fix all tests in **Step1_ClusterTest**.


[Home](../readme.md) | [Back](part4_messaging.md) | [Next](./conclusion.md)
