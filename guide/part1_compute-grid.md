# Compute Grid

![img](img/compute-grid.png)

**Computations** are used to execute arbitrary code in server nodes.

Ignite ensures code is **serialized**, **sent over the network**, **deserialized** and **executed** in server nodes.


## Example

```java
import org.apache.ignite.*;

public class Main {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ignite.compute().run(() -> System.out.println("Hello World"));
    }
}
```
This code prints `Hello World` in one **server node** only.


## Runnable

An **IgniteRunnable** is executed on a server node without a result being returned.


>Complete **TODO**s in **Step1_Runnable** to fix all tests in **Step1_RunnableTest**.


## Callable

An **IgniteCallable** is executed on a server node but returns a result to calling node.

>Complete **TODO**s in **Step2_Callable** to fix all tests in **Step2_CallableTest**.


## Spot the difference

Can you spot the difference between case 1 and 2 ?

```java
import org.apache.ignite.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        // Case 1
        String nodeId1 = ignite.compute().call(() -> System.getProperty("node.id"));

        // Case 2
        String nodeId2 = System.getProperty("node.id");
        String nodeId3 = ignite.compute().call(() -> nodeId);
    }
}
```


>Solution
>- Case 1: value is computed **inside** the lambda, on the remote **server node**.
>- Case 2: value is computed **outside** the lambda, its value is fixed on **client node**, serialized, sent to **server node** and returned back to **client node**.


## Cluster Nodes and Groups

**ClusterNode** class represents **a node** in the cluster.

**ClusterGroup** class represents **a set of nodes** in the cluster.

Node and groups can be used to **restrict** the set of nodes where an operation is executed. They can be retrieved like this for example:
```java
import org.apache.ignite.*;
import org.apache.ignite.cluster.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ClusterNode clusterNode = ignite.cluster().localNode();
        ClusterGroup clusterGroup = ignite.cluster().forNode(clusterNode);

        ignite.compute(clusterGroup).run(() -> System.out.println("Hello local node!"));
    }
}
```

>Complete **TODO**s in **Step3_Cluster** to fix all tests in **Step3_ClusterTest**.


[Home](../readme.md) | [Back](part0_get-started.md) | [Next](./part2_data-grid.md)
