# Cluster

This part describes how **ClusterNodes** can be used and grouped in **ClusterGroup** instances.

![img](img/cluster.png)

=========
## Cluster Nodes

**ClusterNode** is the class representing **a node** in the cluster.

It can be used to retrieve statistics about a particular node.

=========
## Cluster Nodes: How To

**ClusterNode** instance can be retrieved like this:

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

Here we retrieve **localNode** and its average job execution time using **ClusterNode metrics**.

=========
## Cluster Groups

**ClusterGroup** is the class representing **a set of nodes** in the cluster.

It can be used in many Ignite methods to **target the set of nodes** Ignites uses **to distribute operations**.

=========
## Cluster Groups: How To

**ClusterGroups** instance can be retrieved like this:

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

Here we retrieve a group made of **server nodes** only and run an IgniteRunnable **on nodes from this group only**.

=========
## Cluster Nodes and Cluster Groups: Code It

Complete **Step1_Cluster** class.

Run it:
```bash
./gradlew runClient
Part6_Step1
```

=========
## Cluster Nodes and Groups: Checks

Ensure second message is only displayed in **server node 1 and 2**.

For the first message, well, it depends on your machine.

If your CpuLoad is above 50%, message **won't be displayed on any node**.

Otherwise, message **will be displayed on all nodes**.

=========
## Cluster

**ClusterGroup** and **ClusterNode** can be used in almost all Ignite APIs: **IgniteCompute**, **IgniteServices**, **IgniteMessages** and **IgniteEvents**.

They can also be used to filter nodes where **caches are deployed**.

Enough ! Let's take a look at a real use case for Ignite.
