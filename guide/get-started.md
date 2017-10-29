# Get Started

In this hands on, **Ignite** is used as an embedded library.
>It can also be used in standalone mode.

## Example

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
>/!\ For the hands on, do not use default configuration settings, otherwise you'll be joining a cluster with all nodes from local network.
>Instead use configuration from **Config.igniteConfiguration()**.

## Server Nodes

By default, **server nodes** participate in all computing and caching operations in the cluster.
In this hands-on **server nodes** are plain-old Java applications.

>Complete **TODO** in **ServerApp**.
>
>Start 3 **ServerApp** instances (**ServerApp1**, **ServerApp2** and **ServerApp3**).
>
>When server nodes are started you should progressively see in each **ServerApp** node:
>
>```bash
>Topology snapshot [ver=1, servers=1, clients=0, ...]
>Topology snapshot [ver=1, servers=2, clients=0, ...]
>Topology snapshot [ver=1, servers=3, clients=0, ...]
>```

## Client Nodes

By default, **client nodes** do not participate in cluster operations, but rather forward them to **server nodes**.

In this hands on, each test starts a new **client node** and sends compute queries to **server nodes**.

A cluster can perfectly work with **server nodes** only. Using **client nodes** avoids restarting all **server nodes** every time a test is executed.

>Complete **TODO** in **ClientApp**.
>
>Play around and execute some compute queries from **ClientApp** node. Try this for example:
>
>```java
>import org.apache.ignite.Ignite;
>
>public class Main {
>
>    public static void main(String[] args) {
>
>        Ignite ignite = Ignition.start();
>
>        ignite.compute().broadcast(() -> System.out.println("Hello World"));
>    }
>}
>```
>Check each **ServerApp** console!


[Home](../readme.md) | [Back](./introduction.md) | [Next](./part1_compute-grid.md)
