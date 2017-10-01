package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collection;
import java.util.List;

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
    @DisplayName("should create a partitioned cache")
    List<DynamicTest> partitionedCache() {

        return testCacheUsage(step1.createPartitionedCache(ignite), "my-partitioned-cache", 1, 0);
    }

    @TestFactory
    @DisplayName("should create a replicated cache")
    List<DynamicTest> replicatedCache() {

        return testCacheUsage(step1.createReplicatedCache(ignite), "my-replicated-cache", 3, 2);
    }

    @TestFactory
    @DisplayName("should create a backup cache")
    List<DynamicTest> backupCache() {

        return testCacheUsage(step1.createBackupCache(ignite), "my-backup-cache", 2, 1);
    }

    private List<DynamicTest> testCacheUsage(IgniteCache<Integer, Team> cache, String name, int allFactor, int backupFactor) {

        int expected = Data.teams().size();
        int all = expected * allFactor;
        int backups = expected * backupFactor;

        return asList(

            // Name
            dynamicTest("name", () -> {
                assertThat(cache).isNotNull();
                assertThat(cache.getName()).isEqualTo(name);
            }),

            // Write
            dynamicTest("write values", () -> {
                step1.writeValues(cache);
                assertThat(cache.size()).isEqualTo(expected);
            }),

            // Queries
            dynamicTest("get value", () -> assertThat(step1.findByKey(cache, 42))
                .isNotNull().satisfies(team -> assertThat(team.getId()).isEqualTo(42))),
            dynamicTest("scan query", () -> assertThat(step1.findByScanQuery(cache, "99"))
                .extracting(Team::getId).hasSize(10).allMatch(id -> id % 100 == 99)),
            dynamicTest("text query", () -> assertThat(step1.findByTextQuery(cache, "Team*99"))
                .extracting(Team::getId).hasSize(10).allMatch(id -> id % 100 == 99)),
            dynamicTest("sql query", () -> assertThat(step1.findBySqlQuery(cache, "%99"))
                .extracting(Team::getId).hasSize(10).allMatch(id -> id % 100 == 99)),
            dynamicTest("process entry", () -> assertThat(step1.processEntry(cache, 99))
                .isNotNull().isEqualTo("TEAM99")),

            // Size
            dynamicTest("cache size", () -> assertThat(step1.getCacheSize(cache)).isEqualTo(expected)),
            dynamicTest("cache size (ALL)", () -> assertThat(sum(step1.getCacheSizePerNode(cache)))
                .isEqualTo(all)),
            dynamicTest("cache size (PRIMARY)", () -> assertThat(sum(step1.getCacheSizePerNodeForPrimaryKeys(cache)))
                .isEqualTo(expected)),
            dynamicTest("cache size (BACKUP)", () -> assertThat(sum(step1.getCacheSizePerNodeForBackupKeys(cache)))
                .isEqualTo(backups))
        );
    }

    private int sum(Collection<Integer> sizes) {
        return sizes.stream().mapToInt(Integer::intValue).sum();
    }

}
