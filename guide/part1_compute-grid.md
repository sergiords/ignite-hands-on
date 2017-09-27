# Compute Grid

In this part we will learn how to execute basic **compute** operations in our cluster.

![img](img/compute-grid.png)

## Compute Grid: Example

Executing code in our cluster is as easy as:

```java
import org.apache.ignite.*;

public class Main {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        ignite.compute().run(() -> System.out.println("Hello World"));
    }
}
```
This code prints in one **server node**: 
```bash
Hello World
```

## Ignite Runnable

An **IgniteRunnable** does not return any result. It just sends computations to the cluster.

## Ignite Runnable: Exercise

Complete TODOs in **Step1_Runnable** and make all tests in **Step1_RunnableTest** pass.

## Ignite Callable

An **IgniteCallable** does return a result. It sends computation in the cluster and get result back to calling node.

## Ignite Callable: Exercise
Complete TODOs in **Step1_Callable** and make all tests in **Step1_CallableTest** pass.

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

Try it !

## Spot the difference: Solution

- Case 1: value is computed **inside** the lambda, on the executing **server node**.

- Case 2: value is computed **outside** the lambda, its value is fixed on **client node**, serialized and sent to **server node**.

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
They are not serialized, but each reference is retrieved from corresponding node.

## Enough computing

That's it for computing part. Although steps were pretty easy, we have learned enough about how to distribute computations across the cluster. Let's move to data grid.

[Home](../readme.md) | [Back](./get-started.md) | [Next](./part2_data-grid.md)
