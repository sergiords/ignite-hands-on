# Data Grid

![img](img/data-grid.png)

In this part we will learn how to create and use different distributed **caches** in our cluster.

Ignite ensures cache data is **serialized** and **partitioned** across server nodes and available to any node in cluster.


## Example

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

## Cache queries

Cache can be queried by key or using SQL, Text or Scan queries.

>Complete **TODO**s in **Step1_CacheQuery** to fix all tests in **Step1_CacheQueryTest**.


## Cache Modes

Cache entry distribution across server nodes can be adjusted by 3 means:

- **Partitioned Cache**: each entry is in one **primary node** node only
- **Replicated Cache**: each entry is in one **primary node** and **n-1 backup nodes** (**n** is cluster size).
- **Cache Backups**: each entry is in one **primary node** and **x backup nodes** (**x** is backup size).

>Complete **TODO**s in **Step2_CacheMode** to fix all tests in **Step2_CacheModeTest**.


## Compute affinity

**Compute Affinity** allows sending computations according to a **cache entry** location.

Affinity avoids cross-node cache queries by **sending computations only to relevant nodes**. For example:

```java
import org.apache.ignite.*;

public class App {

    public static void main(String[] args) {

        Ignite ignite = Ignition.start();

        IgniteCache<Key, Value> cache = ignite.getOrCreateCache("my-cache");
        cache.put("key1", "value1");
        cache.put("key2", "value2");

        String value = ignite.compute()
            .affinityCall("my-cache", "key2", () -> cache.get("key2"));
    }
}
```
Here Ignite does no network call on `cache.get("key2")` call because `affinityCall()` is **executed on node where `"key2"` is stored**.


>Complete **TODO**s in **Step3_ComputeAffinity** to fix all tests in **Step3_ComputeAffinityTest**.


## Data affinity

**AffinityKey** allows grouping multiple cache entries in same node.

Data is distributed across nodes with a specific key: **affinity key**, which differs from the key used for cache. For example:

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

        String value = ignite.compute()
            .affinityCall("my-cache", "group1", () -> cache.get("key1") + cache.get("key2"));
    }
}
```

Ignite ensures `key1` and `key2` are hosted on the same node since they are stored with same **affinity key** - `"group1"`.

Here Ignite does no network call on `cache.get("key1")` and `cache.get("key2")` calls because `affinityCall()` is **executed on node where keys associated to `"group1"`** are stored.


>Complete **TODO**s in **Step4_DataAffinity** to fix all tests in **Step4_DataAffinityTest**.


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
        Collection<Integer> sizes1 = ignite.compute().broadcast(() -> cache.localSize());

        // Case 2
        Collection<Integer> sizes2 = new ArrayList<>();
        ignite.compute().broadcast(() -> sizes2.add(cache.localSize()));
    }
}
```


>Solution
>- Case 1: each server node computes `cache.localSize()` and results are reduced by Ignite to a collection in client node.
>- Case 2: each server node computes `cache.localSize()` but results are added to **a copy** of the **sizes2** collection defined in client node. Original collection is never updated.


[Home](../readme.md) | [Back](./part1_compute-grid.md) | [Next](part3_service-grid.md)
