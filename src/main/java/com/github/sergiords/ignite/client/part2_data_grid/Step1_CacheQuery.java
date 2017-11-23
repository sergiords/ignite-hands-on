package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;

import java.util.List;

public class Step1_CacheQuery {

    private final IgniteCache<Integer, Team> cache;

    public Step1_CacheQuery(Ignite ignite) {

        /*
         * TODO:
         * - create a CacheConfiguration named "my-cache"
         * - use ignite.getOrCreateCache(...) to create a cache with this configuration
         */
        this.cache = null;
    }

    public void populateCache() {

        List<Team> teams = Data.teams();

        /*
         * TODO:
         * - put all teams in the cache using team.getId() as cache key
         */
    }

    public Team findByKey(Integer id) {

        /*
         * TODO:
         * - find team in cache by id
         */
        return null;
    }

    public String findByKeyAndProcess(Integer id) {

        // Entry Processor

        /*
         * TODO:
         * - create a CacheEntryProcessor returning team name in uppercase
         * - use this processor and cache.invoke(...) to return processed team name for given id
         */

        return null;
    }

    public List<Team> findByScanQuery(String nameSearch) {

        // Scan query

        /*
         * TODO:
         * - create a ScanQuery finding teams whose name ends with given nameSearch
         * - use cache.query(...) to return teams from cache matching this query
         */

        return null;
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

        return null;
    }

    public List<Team> findBySqlQuery(String nameSearch) {

        // SQL Query

        /*
         * TODO:
         * - add @QuerySqlField annotation to Team.name field to allow SQL query name searches
         * - create a SqlQuery finding teams where name is like 'nameSearch' (see tests to find what search criteria looks like)
         * - query example: 'select * from team where name like ?'
         * - use cache.query(...) to return teams from cache matching this query
         * TIP:
         * - define indexed types (cache key and cache value types) in cache configuration (see configuration.setIndexedTypes)
         */

        return null;
    }

}
