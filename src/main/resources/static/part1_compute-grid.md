# Compute Grid

In this part we will learn how to execute basic **compute** operations in our cluster.

![img](img/compute-grid.png)

=========
## Compute grid how-to

Executing code in our cluster is as-easy-as:

```java
import org.apache.ignite.*;

public class Main {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ignite.compute().run(() -> System.out.println(1 + 1));

    }

}
```
This code prints in one random-picked **server node** console: 
```bash
2
```

=========
## Step1_Runnable

An **IgniteRunnable** does not return any result. It just sends computations in the cluster.

Complete **Step1_Runnable** class.

Run it:
```bash
./gradlew runClient
Part1_Step1
```

=========
## Step1_Runnable: checks

Ensure on every execution that:

 - **"Hello Single Node"** is logged in one node only

 - **"Hello Every Node"** is logged in all nodes

 - **"Hello First Node"** and **"Hello Second Node"** are each logged in one node only

=========
## Step2_Callable

An **IgniteCallable** does return a result.

It sends computation in the cluster and get its result back to calling node.

Complete **Step1_Callable** class.

Run it:
```bash
./gradlew runClient
Part1_Step2
```

=========
## Spot the difference

Can you spot the difference between case 1 and 2 ?

```java
import org.apache.ignite.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        // Case 1
        UUID uuid1 = ignite.compute().call(() -> ignite.cluster().localNode().id());

        // Case 2
        ClusterNode node = ignite.cluster().localNode();
        UUID uuid2 = ignite.compute().call(() -> node.id());

    }

}
```

Try it !

=========
## Solution

- Case 1: value is computed **inside** the lambda, on the executing **server node**.

- Case 2: value is computed **outside** the lambda, its value is fixed on **client node**, serialized and **sent to server node**.

=========
## For the curious

What about `ignite` variable inside lambda ?

```java
import org.apache.ignite.*;

public class App {
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();
        UUID uuid1 = ignite.compute().call(() -> ignite.cluster().localNode().id());

    }
}
```

>Ignite implementations are Externalizable but not Serializable (see IgniteKernal).

=========
## Enough computing

That's it for computing part.

Although steps were pretty easy, we have learned enough about how to distribute computations across the cluster.

Let's move to data grid.
