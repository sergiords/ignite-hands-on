package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@ExtendWith(ServerAppTest.class)
public class Step1_CacheTest {

    private final Ignite ignite;

    private final Step1_Cache step1;

    public Step1_CacheTest(Ignite ignite) {
        this.ignite = ignite;
        this.step1 = new Step1_Cache(ignite);
    }

    @TestFactory
    @DisplayName("partitioned cache")
    List<DynamicTest> partitionedCache() {

        IgniteCache<Integer, String> partitionedCache = step1.createPartitionedCache(ignite);

        return testCacheUsage(partitionedCache, "my-partitioned-cache", 1, 0);
    }

    @TestFactory
    @DisplayName("replicated cache")
    List<DynamicTest> replicatedCache() {

        IgniteCache<Integer, String> replicatedCache = step1.createReplicatedCache(ignite);

        return testCacheUsage(replicatedCache, "my-replicated-cache", 3, 2);
    }

    @TestFactory
    @DisplayName("backup cache")
    List<DynamicTest> backupCache() {

        IgniteCache<Integer, String> backupCache = step1.createBackupCache(ignite);

        return testCacheUsage(backupCache, "my-backup-cache", 2, 1);
    }

    private List<DynamicTest> testCacheUsage(IgniteCache<Integer, String> cache, String name, int allFactor, int backupFactor) {

        int expected = 1000;
        int all = expected * allFactor;
        int backups = expected * backupFactor;

        return asList(

            // Name
            dynamicTest("should be named " + name, () -> {
                assertThat(cache).isNotNull();
                assertThat(cache.getName()).isEqualTo(name);
            }),

            // Write
            dynamicTest("should have 1000 entries", () -> {
                IntStream.range(0, expected).boxed().forEach(i -> cache.put(i, String.valueOf(i)));
                assertThat(cache.size()).isEqualTo(expected);
            }),

            dynamicTest("should have cluster size: " + expected, () -> {
                Integer result = step1.getCacheSize(cache);
                assertThat(result).isEqualTo(expected);
            }),

            dynamicTest("should have ALL entries: " + all, () -> {
                int result = sum(step1.getCacheSizePerNode(cache));
                assertThat(result).isEqualTo(all);
            }),

            dynamicTest("should have PRIMARY entries: " + expected, () -> {
                int result = sum(step1.getCacheSizePerNodeForPrimaryKeys(cache));
                assertThat(result).isEqualTo(expected);
            }),

            dynamicTest("should have BACKUP entries: " + backups, () -> {
                int result = sum(step1.getCacheSizePerNodeForBackupKeys(cache));
                assertThat(result).isEqualTo(backups);
            })
        );
    }

    private int sum(Collection<Integer> sizes) {
        return sizes.stream().mapToInt(Integer::intValue).sum();
    }

}
