package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CachePeekMode;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache.Entry;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Step1_Cache {

    private final Ignite ignite;

    public Step1_Cache(Ignite ignite) {
        this.ignite = ignite;
    }

    public IgniteCache<Integer, Team> createPartitionedCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-partitioned-cache"
         * - configure cache mode to PARTITIONED
         * - create and return a cache with this configuration
         */
        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-partitioned-cache");
        configuration.setCacheMode(CacheMode.PARTITIONED);
        configuration.setIndexedTypes(Integer.class, Team.class);

        return ignite.getOrCreateCache(configuration);
    }

    public void writeValues(IgniteCache<Integer, Team> cache) {

        List<Team> teams = Data.teams();

        /*
         * TODO:
         * - put all teams in the cache using team.getId() as cache key
         */
        teams.forEach(team -> cache.put(team.getId(), team));
    }

    public Team findByKey(IgniteCache<Integer, Team> cache, Integer id) {

        /*
         * TODO:
         * - find team in cache by id
         */
        return cache.get(id);
    }

    public List<Team> findByScanQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        // Scan query

        /*
         * TODO:
         * - create a ScanQuery finding teams whose name ends with 'nameSearch'
         * - use cache.query(...) to return teams from cache matching this query
         */
        ScanQuery<Integer, Team> scanQuery = new ScanQuery<>((key, value) -> value.getName().endsWith(nameSearch));

        return cache.query(scanQuery).getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

    public List<Team> findByTextQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        // Text Query

        /*
         * TODO:
         * - add @QueryTextField annotation to Team.name field to allow Lucene-based Text search on name
         * - create a TextQuery finding teams matching 'nameSearch'
         * - use cache.query(...) to return teams from cache matching this query
         */
        TextQuery<Integer, Team> textQuery = new TextQuery<>(Team.class, nameSearch);

        return cache.query(textQuery).getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

    public List<Team> findBySqlQuery(IgniteCache<Integer, Team> cache, String nameSearch) {

        // SQL Query

        /*
         * TODO:
         * - add @QuerySqlField annotation to Team.name field to allow SQL query name searches
         * - create a SqlQuery finding teams where name is like 'nameSearch'
         * - query example: 'select * from team where name like ?'
         * - use cache.query(...) to return teams from cache matching this query
         */
        SqlQuery<Integer, Team> sqlQuery = new SqlQuery<Integer, Team>(Team.class, "name like ?").setArgs(nameSearch);

        return cache.query(sqlQuery).getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

    public String processEntry(IgniteCache<Integer, Team> cache, Integer id) {

        // Entry Processor

        /*
         * TODO:
         * - create a CacheEntryProcessor returning team name in uppercase
         * - use cache.invoke(...) to return the processed name of the team with given id
         */
        CacheEntryProcessor<Integer, Team, String> processor = (entry, args) -> entry.getValue().getName().toUpperCase();

        return cache.invoke(id, processor);
    }

    public IgniteCache<Integer, Team> createReplicatedCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-replicated-cache"
         * - configure cache mode to REPLICATED
         * - create and return a cache with this configuration
         */
        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-replicated-cache");
        configuration.setCacheMode(CacheMode.REPLICATED);
        configuration.setIndexedTypes(Integer.class, Team.class);

        return ignite.getOrCreateCache(configuration);
    }

    public IgniteCache<Integer, Team> createBackupCache(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-backup-cache"
         * - configure cache mode to PARTITIONED
         * - configure cache to have 1 backup exactly for each entry
         * - create and return a cache with this configuration
         */
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
         * - return cache size in cluster (all keys in all nodes)
         * - use cache.size()
         */
        return cache.size();
    }

    public Collection<Integer> getCacheSizePerNode(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (all keys in each node)
         * - use cache.localSize(...)
         * - ignite.compute().broadcast() to get cache size for each server node
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.ALL));
    }

    public Collection<Integer> getCacheSizePerNodeForPrimaryKeys(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (only primary keys in each node)
         * - use cache.localSize(...)
         * - ignite.compute().broadcast() to get cache size for each server node
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.PRIMARY));
    }

    public Collection<Integer> getCacheSizePerNodeForBackupKeys(IgniteCache<Integer, Team> cache) {

        /*
         * TODO:
         * - return a collection containing cache size in each server node (only backup keys in each node)
         * - use cache.localSize(...)
         * - ignite.compute().broadcast() to get cache size for each server node
         */
        return ignite.compute().broadcast(() -> cache.localSize(CachePeekMode.BACKUP));
    }

}
