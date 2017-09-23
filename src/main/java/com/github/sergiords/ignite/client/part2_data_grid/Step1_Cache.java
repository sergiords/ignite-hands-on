package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.CacheData;
import com.github.sergiords.ignite.data.Team;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.IntStream.range;

@SuppressWarnings({"unused", "ConstantConditions", "Duplicates", "FieldCanBeLocal"})
public class Step1_Cache {

    private final Ignite ignite;

    public Step1_Cache(Ignite ignite) {
        this.ignite = ignite;
    }

    public IgniteCache<Integer, Team> createPartitionedCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-partitioned-cache"
         * - set the cache mode to PARTITIONED in this configuration
         * - create a cache using this configuration
         */
        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-partitioned-cache");
        configuration.setCacheMode(CacheMode.PARTITIONED);
        configuration.setIndexedTypes(Integer.class, Team.class);

        return ignite.getOrCreateCache(configuration);
    }

    public void writeValues(IgniteCache<Integer, Team> cache) {

        List<Team> teams = CacheData.teams();

        /*
         * TODO:
         * - put 1000 entries in the cache in the following format: "Key1"=>"Value1", "Key2"=>"Value2", ...
         */
        range(0, teams.size()).forEach(i -> cache.put(i, teams.get(i)));
    }

    public Team getByKey(IgniteCache<Integer, Team> cache, Integer key) {

        return cache.get(key);
    }

    /*
     * Scan query
     */
    public List<Team> findByScanQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        ScanQuery<Integer, Team> scanQuery = new ScanQuery<>((key, value) -> value.getName().endsWith(nameSearch));

        return cache.query(scanQuery).getAll().stream()
            .map(Cache.Entry::getValue)
            .collect(Collectors.toList());
    }

    /*
     * Text Query
     */
    public List<Team> findByTextQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        /*
         * Add @QueryTextField annotation to Team#name field to allow Lucene-based Text search on name
         */

        TextQuery<Integer, Team> textQuery = new TextQuery<>(Team.class, nameSearch);

        return cache.query(textQuery).getAll().stream()
            .map(Cache.Entry::getValue)
            .collect(Collectors.toList());
    }

    /*
     * SQL Query
     */
    public List<Team> findBySqlQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        /*
         * Add @QuerySqlField annotation to Team.name field to allow SQL query name searches
         */

        /*
         * 'select * from team where name like ?'
         * 'select * where name like ?'
         * 'where name like ?'
         * 'name like ?'
         *
         * All these queries work
         */
        SqlQuery<Integer, Team> sqlQuery = new SqlQuery<Integer, Team>(Team.class, "name like ?").setArgs(nameSearch);

        return cache.query(sqlQuery).getAll().stream()
            .map(Cache.Entry::getValue)
            .collect(Collectors.toList());
    }

    public String processTeam(IgniteCache<Integer, Team> cache, Integer id) {

        return cache.invoke(id, (entry, args) -> entry.getValue().getName().toUpperCase());
    }

    public IgniteCache<Integer, Team> createReplicatedCache(Ignite ignite) {

        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-replicated-cache");
        configuration.setCacheMode(CacheMode.REPLICATED);
        configuration.setIndexedTypes(Integer.class, Team.class);

        return ignite.getOrCreateCache(configuration);
    }

    public IgniteCache<Integer, Team> createBackupCache(Ignite ignite) {

        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-backup-cache");
        configuration.setBackups(1);
        configuration.setCacheMode(CacheMode.PARTITIONED);
        configuration.setIndexedTypes(Integer.class, Team.class);

        return ignite.getOrCreateCache(configuration);
    }

    public Integer getCacheSize(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return cluster-wide cache size
         * - see IgniteCache#size
         */
        return cache.size();
    }

    public Collection<Integer> getCacheSizePerNode(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a collection containing cache size of each remote node
         * - use cache.localSize()
         * - use ignite.compute().broadcast() to get cache size of each remote node
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.ALL));
    }

    public Collection<Integer> getCacheSizePerNodeForPrimaryKeys(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a map containing cache size for primary entries in each node (NodeId => localCacheSize)
         * - see IgniteCache#localSize
         * - retrieve NodeId using System.getProperty("node.id")
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.PRIMARY));
    }

    public Collection<Integer> getCacheSizePerNodeForBackupKeys(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a map containing cache size for backup entries in each node (NodeId => localCacheSize)
         * - see IgniteCache#localSize
         * - retrieve NodeId using System.getProperty("node.id")
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.BACKUP));
    }

}
