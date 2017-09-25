# Get Started

In this hands on, **Ignite** will be used as an embedded library.
>It can also be used in standalone mode.

## Get Started: Example

**Ignite** is designed to work in **cluster**. Every node can be started like this:

```java
import org.apache.ignite.Ignite;

public class Main {

    public static void main(String[] args) {

        IgniteConfiguration configuration = new IgniteConfiguration();

        Ignition.start(configuration); // that's all folks !
    }
}
```

## Server Nodes

By default, **server nodes** participate in all computing and caching operations in the cluster.

In this hands-on, our **server nodes** are plain-old Java applications.

## Server Nodes: Exercise

### Step 1
Complete TODO in **ServerApp**.

### Step 2
Start 3 **ServerApp** instances (use **ServerApp1**, **ServerApp2** and **ServerApp3** shortcuts).

When server nodes are started you should progressively see in **ServerApp** nodes:

```bash
Topology snapshot [ver=1, servers=1, clients=0, ...]
Topology snapshot [ver=1, servers=2, clients=0, ...]
Topology snapshot [ver=1, servers=3, clients=0, ...]
```

## Client Nodes

By default, **client nodes** do not participate in cluster operations, but rather forward them to **server nodes**.

In this hands on, each test starts a **a client node** and send compute queries to **server nodes**.

A cluster can perfectly work with **server nodes** only. Using **client nodes** avoids restarting all **server nodes** every time a test is executed. 

## Client Nodes: Exercise

### Step 1
Complete TODO in **ClientApp**.

### Step 2
Play around to execute some compute queries from **ClientApp** node. Try this for example:
```java
import org.apache.ignite.Ignite;

public class Main {
    
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ignite.compute().broadcast(() -> System.out.println("Hello World"));
    }
}
```
Check each **ServerApp** console!

## Well-done !

We are now ready to go for next steps.

[Home](../readme.md) | [Back](./introduction.md) | [Next Step](./part1_compute-grid.md)
