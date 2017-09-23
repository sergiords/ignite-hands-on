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
