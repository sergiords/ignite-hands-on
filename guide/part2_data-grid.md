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


# Affinity

In this part we will learn how to collocate compute with data and data with data

=========
## Compute Affinity

**Affinity** is a function used to **map cache keys to nodes**.

**Computations** can then be dispatched to nodes hosting specific **cache keys** by using this function.

This allows code to be executed in node hosting data with **local cache calls** only.

=========
## Compute affinity: How To

```java
import org.apache.ignite.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        IgniteCache<Key, Value> cache = ignite.getOrCreateCache("my-cache");
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        String value = ignite.compute()
            .affinityCall("my-cache", "key2", () -> cache.get("key2").toUpperCase());

    }

}
```

Here Ignite does no network call on ``cache.get("key2")`` call because `affinityCall` **executes code in node where `"key2"` is stored**.

=========
## Compute Affinity: Code It

Complete **Step1_ComputeAffinity** class.

Run it:
```bash
./gradlew runClient
Part3_Step1
```

=========
## Compute Affinity: Check

Ensure all computations and partition are on the same **server node**.

**Kill server node** where all computations are done... wait.

**Execute step again**, computations and partition should now be on another node.

Restart killed **server node**.

=========
## Data affinity

**AffinityKey** allows grouping multiple entries in the same nodes.

Data is distributed across nodes with a specific key: **affinity key**, which differs from the key used for cache.

=========
## Data affinity: How To

```java
import org.apache.ignite.*;
import org.apache.ignite.cache.affinity.*;

public class App {
    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        IgniteCache<Object, Value> cache = ignite.getOrCreateCache("my-cache");
        cache.put(new AffinityKey<>("key1", "group1"), "value1");
        cache.put(new AffinityKey<>("key2", "group1"), "value2");
        cache.put(new AffinityKey<>("key3", "group2"), "value3");
        cache.put(new AffinityKey<>("key4", "group2"), "value4");

        String value = ignite.compute().affinityCall("my-cache", "group1",
            () -> cache.get("key1") + cache.get("key2"));
    }
}
```

Ignite ensures `key1` and `key2` are hosted on the same node since they are stored with same **affinity key**.

`affinityCall` executes code where keys associated to `"group1"` **affinity key** are stored.

=========
## Data Affinity: Code It

Complete **Step2_DataAffinity** class.

Run it:
```bash
./gradlew runClient
Part3_Step2
```

=========
## Data Affinity: Check

Ensure all teams from the same country are on the same **server nodes**.

**Kill one or two server nodes**... wait.

**Execute step again**, teams from the same country should stay on the same node.

Restart killed **server nodes**.

=========
## That was awesome !

Now that we can manipulate Ignite's **compute and data grid** we can think of higher level tools on top of that.

Ignite brings [MANY more tools](https://apacheignite.readme.io/docs/)... But let's focus.

Next part will be about **Service grid**.

[Home](../readme.md) | [Back](./part2_data-grid.md) | [Next](part3_service-grid.md)


=========
## Enough data

There's a lot more to say about data grid:

- Eviction or Expiry policies
- Cache queries, continuous queries
- Transactions and atomicity modes, rebalancing modes
- Memory modes (OFF_HEAP)
- SQL grid over data grid

For this hands on, we've have learned enough, so let's move to affinity.

[Home](../readme.md) | [Back](./part1_compute-grid.md) | [Next](./part3_service-grid.md)
