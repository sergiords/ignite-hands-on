# Get Started

In this hands on, Ignite will be used as an embedded library.
>It can also be used in standalone mode.

=========
## Ignite cluster how-to

Ignite is designed to work in **cluster**. Every node can be started like this:

```java
import org.apache.ignite.Ignite;

public class Main {

    public static void main(String[] args) {

        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();

        Ignition.start(igniteConfiguration); // that's all folks !

    }

}
```

=========
## Server Nodes

By default, **server nodes** participate in all computing and caching operations in the cluster.

In this hands-on, our **server nodes** are plain-old Java applications.

=========
## Code

To configure **server nodes**, complete **ServerApp** class.

=========
## Run

**ServerApp** main class starts **server nodes**.

Start 3 server nodes using different terminals:
```bash
./gradlew runServer1
```
```bash
./gradlew runServer2
```
```bash
./gradlew runServer3
```
>see **build.gradle** file for details if you want to reproduce launch with your IDE (which is a bit faster)

=========
## Check

When server nodes are started you should progressively see in **server nodes** console:

```bash
Topology snapshot [ver=1, servers=1, clients=0, ...]
Topology snapshot [ver=1, servers=2, clients=0, ...]
Topology snapshot [ver=1, servers=3, clients=0, ...]
```

=========
## Client Nodes

By default, **client nodes** do not participate in cluster operations, but rather forward them to **server nodes**.

In this hands on, each step will be executed by a client node.

A cluster can perfectly work with **server nodes** only.

Using **client nodes** avoids restarting all **server nodes** every time a step is implemented. 

=========
## Code

To configure **client node**, complete **ClientApp** class.

=========
## Run

A **Client node** is started for each step but, good to know, they all rely on the same **ClientApp** main class.

This class prompts you for the **step id to run** and pressing return in console allows you to **run Step multiple times**.

```bash
./gradlew runClient
Part1_Step1
```

>see **build.gradle** file for details if you want to reproduce launch with your IDE (which is a bit faster)

=========
## Well-done !

We are now ready to execute some code in **server nodes** using **client nodes**.
