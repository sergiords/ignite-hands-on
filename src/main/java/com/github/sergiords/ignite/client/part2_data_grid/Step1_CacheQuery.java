package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheEntryProcessor;
import org.apache.ignite.cache.query.ScanQuery;
import org.apache.ignite.cache.query.SqlQuery;
import org.apache.ignite.cache.query.TextQuery;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache.Entry;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Step1_CacheQuery {

    private final IgniteCache<Integer, Team> cache;

    public Step1_CacheQuery(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-cache"
         * - use ignite.getOrCreateCache(...) to create a cache with this configuration
         */
        CacheConfiguration<Integer, Team> configuration = new CacheConfiguration<>();
        configuration.setName("my-cache");
        configuration.setIndexedTypes(Integer.class, Team.class);

        this.cache = ignite.getOrCreateCache(configuration);
    }

    public void populateCache() {

        List<Team> teams = Data.teams();

        /*
         * TODO:
         * - put all teams in the cache using team.getId() as cache key
         */
        teams.forEach(team -> cache.put(team.getId(), team));
    }

    public Team findByKey(Integer id) {

        /*
         * TODO:
         * - find team in cache by id
         */
        return cache.get(id);
    }

    public String findByKeyAndProcess(Integer id) {

        // Entry Processor

        /*
         * TODO:
         * - create a CacheEntryProcessor returning team name in uppercase
         * - use this processor and cache.invoke(...) to return processed team name for given id
         */
        CacheEntryProcessor<Integer, Team, String> processor = (entry, args) -> entry.getValue().getName().toUpperCase();

        return cache.invoke(id, processor);
    }

    public List<Team> findByScanQuery(String nameSearch) {

        // Scan query

        /*
         * TODO:
         * - create a ScanQuery finding teams whose name ends with given nameSearch
         * - use cache.query(...) to return teams from cache matching this query
         */
        ScanQuery<Integer, Team> scanQuery = new ScanQuery<>((key, value) -> value.getName().endsWith(nameSearch));

        return cache.query(scanQuery)
            .getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

    public List<Team> findByTextQuery(String nameSearch) {

        // Text Query

        /*
         * TODO:
         * - add @QueryTextField annotation to Team.name field to allow Lucene-based Text search on name
         * - create a TextQuery finding teams matching 'nameSearch' (see tests to find what search criteria looks like)
         * - use cache.query(...) to return teams from cache matching this query
         * TIP:
         * - define indexed types (cache key and cache value types) in cache configuration (see configuration.setIndexedTypes)
         */
        TextQuery<Integer, Team> textQuery = new TextQuery<>(Team.class, nameSearch);

        return cache.query(textQuery)
            .getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

    public List<Team> findBySqlQuery(String nameSearch) {

        // SQL Query

        /*
         * TODO:
         * - add @QuerySqlField annotation to Team.name field to allow SQL query name searches (see tests to find what search criteria looks like)
         * - create a SqlQuery finding teams where name is like 'nameSearch'
         * - query example: 'select * from team where name like ?'
         * - use cache.query(...) to return teams from cache matching this query
         * TIP:
         * - define indexed types (cache key and cache value types) in cache configuration (see configuration.setIndexedTypes)
         */
        SqlQuery<Integer, Team> sqlQuery = new SqlQuery<Integer, Team>(Team.class, "name like ?").setArgs(nameSearch);

        return cache.query(sqlQuery)
            .getAll().stream()
            .map(Entry::getValue)
            .collect(toList());
    }

}
