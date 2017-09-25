# Data Grid

In this part we will learn how to create and use different distributed **caches** in our cluster.

![img](img/data-grid.jpg)

=========
## Data grid: how-to

Creating a distributed **cache** in our cluster is as easy as:

```java
import org.apache.ignite.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        CacheConfiguration<Key, Value> configuration = new CacheConfiguration<>("my-cache");

        IgniteCache<Key, Value> cache = ignite.getOrCreateCache(configuration);

    }

}
```

=========
## Cache Observer

Complete **CacheObserver** class.

This class will be used in following steps to populate cache and observe data distribution.

=========
## Partitioned Cache

![img](img/partitioned-cache.png)

=========
## Partitioned Cache

**Partitioned Cache** entries are distributed across all cluster nodes.

Complete **Step1_PartitionedCache** class.

Run it:
```bash
./gradlew runClient
Part2_Step1
```

=========
## Partitioned Cache: Checks

Ensure cache has 1000 entries distributed across all nodes.

**Kill one server node of your choice**... wait.

Argh, you should observe that cache has less entries now, data is lost.

Restart killed **server node**.

=========
## Prevent data loss

To prevent data loss while some nodes are still up we can use a **replicated cache**.

=========
## Replicated Cache

![img](img/replicated-cache.png)

=========
## Replicated Cache

**Replicated Cache** entries are replicated across all cluster nodes.

For each entry there is one **primary node** and **n-1 backup nodes** (assuming **n** is cluster size).

Complete **Step2_ReplicatedCache** class.

Run it:
```bash
./gradlew runClient
Part2_Step2
```

=========
## Replicated Cache: Checks

Ensure cache has 1000 entries replicated across all nodes.

**Kill one server node of your choice**... wait.

GG, you should observe that cache still has 1000 entries, data was rebalanced across alive nodes.

Restart killed **server node**.

=========
## Prevent data overflow

Replicated entries can lead to over memory consumption.

To balance between data loss and over memory consumption we can use **Cache Backups**.

=========
## Backup Cache

**Cache Backups** allow entries to be replicated to some cluster nodes only.

For each entry there is one **primary node** and **x backup nodes** (**x** can be configured).

Complete **Step3_BackupCache** class.

Run it:
```bash
./gradlew runClient
Part2_Step3
```

=========
## Backup Cache: Checks

Ensure cache has 1000 entries and each node has about 330 primary entries and 330 backup entries:
```sh
Cache size: 1000
Cache size per node: {node2=284, node3=327, node1=389}
Primary cache size per node: {node2=284, node3=327, node1=389}
Backup cache size per node: {node2=374, node3=329, node1=297}
```

**Kill one server node of your choice**... wait. Ensure each node now has about 500 primary entries and 500 backup entries:

```sh
Cache size: 1000
Cache size per node: {node2=461, node1=539}
Primary cache size per node: {node2=461, node1=539}
Backup cache size per node: {node2=539, node1=461}
```

Restart killed **server node**.

=========
## Persistent data

In the 3 previous configurations, all data is kept **in memory**.

As long as nodes are alive data is not lost.

To persist data between cluster restarts or failures we can use **Stored Cache**.

=========
## Stored Cache

**Cache** entries can be persisted (read/write) using a **Cache Store**.

Complete **Step4_StoredCache** class.

Run it:
```bash
./gradlew runClient
Part2_Step4
```

=========
## Stored Cache: Checks

Ensure `build/stored-cache-test` directory has 1000 files due to **write-through** configuration.

Kill 1 or 2 nodes and ensure that entries are read and cached again due to **read-through** configuration.

Restart killed **server nodes**.

=========
## What about Store updates ?

As you may wonder, if the store gets updated, updates are not magically reflected in the cache.

That's not the purpose of the store. Store acts as a cache persistent backup.

If an entry needs to be updated it has to be via `cache#put` calls.

Store will then be updated due to **write-through** configuration.

=========
## Spot the difference

Can you spot the difference between case 1 and 2 ?

```java
import org.apache.ignite.*;
import org.apache.ignite.cache.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();
        IgniteCache<String, String> cache = ignite.getOrCreateCache("test");

        // Case 1
        Collection<Integer> sizes1 = ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.PRIMARY));

        // Case 2
        Collection<Integer> sizes2 = new ArrayList<>();
        ignite.compute().broadcast(() -> sizes2.add(cache.localSize()));

    }

}
```

Try it !

=========
## Solution

- Case 1: each server node computes `cache#localSize` locally and each value is reduced in a single collection: **sizes1** in client node.

- Case 2: each server node computes `cache#localSize` locally but add size to **a copy** of the **sizes2** collection defined in client node.

=========
## For the curious

What about `cache` variable inside lambda ?

```java
import org.apache.ignite.*;
import org.apache.ignite.cache.*;

public class App {
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();
        IgniteCache<String, String> cache = ignite.getOrCreateCache("test");
        Collection<Integer> sizes = ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.PRIMARY));

    }
}
```

>IgniteCache implementations are Externalizable but not Serializable (see IgniteCacheProxy).

=========
## Enough data

There's a lot more to say about data grid:

- Eviction or Expiry policies
- Cache queries, continuous queries
- Transactions and atomicity modes, rebalancing modes
- Memory modes (OFF_HEAP)
- SQL grid over data grid

For this hands on, we've have learned enough, so let's move to affinity.

[Home](../readme.md) | [Back](./part1_compute-grid.md) | [Next](./part3_affinity.md)
