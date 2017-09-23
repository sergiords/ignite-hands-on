package com.github.sergiords.ignite.client.part3_affinity;

import com.github.sergiords.ignite.client.ClientStep;
import com.github.sergiords.ignite.data.CacheData;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.AffinityKey;

import java.util.List;
import java.util.Map;

@SuppressWarnings({"unused", "ConstantConditions", "FieldCanBeLocal"})
public class Step2_DataAffinity implements ClientStep {

    private static final String CACHE_NAME = "my-data-affinity-cache";

    private final Ignite ignite;

    private final IgniteCache<AffinityKey<Team>, List<User>> cache;

    public Step2_DataAffinity(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a partitioned cache named "my-data-affinity-cache" just like in Step1_Cache
         */
        this.cache = null;
    }

    @Override
    public void run() {

        populateCache();

        Map<String, List<Team>> teamsByNode = findTeamsByNode();

        String country = CacheData.getCountries().get(0);

        List<User> topCommitterFromCountry = topCommitterFromCountry(country);

        System.out.printf("===== DataAffinity =====%n");
        System.out.printf("=> Teams by Node: %s%n", teamsByNode);
        System.out.printf("=> Top committers from %s are %s%n", country, topCommitterFromCountry);
    }

    private AffinityKey<Team> affinityKey(Team team) {

        /*
         * TODO:
         * - return a new affinity key using team's country as the affinity key discriminator
         * - use new AffinityKey(...)
         */
        return null;
    }

    private void populateCache() {

        /*
         * TODO:
         * - populate cache
         * - put all teams from the same country in the same node using affinityKey method defined above
         * - use CacheData#teams to find teams
         * - use CacheData#users to find users for a team
         */
    }

    private Map<String, List<Team>> findTeamsByNode() {

        /*
         * TODO:
         * - return a map containing (NodeId => locally stored Team List)
         * - use IgniteCache#localEntries to find local cache entries and do this in every node
         * - use System.getProperty("node.id") to retrieve NodeId
         * - use Pair or HashMap.SimpleEntry if you need a pair structure
         * TIP:
         * - unfortunately localCacheEntries returns an Iterable... that's annoying
         * - use StreamSupport.stream(myIterator.spliterator(), false) to get a plain-old stream
         */
        return null;
    }

    private List<User> topCommitterFromCountry(String country) {

        /*
         * TODO:
         * - return top committer from each team from the given country
         * - since each team from the same country are hosted in same node you can safely use IgniteCompute#affinityCall
         * TIP:
         * - unfortunately localCacheEntries returns an Iterable... that's annoying
         * - use StreamSupport.stream(myIterator.spliterator(), false) to get a plain-old stream
         */

        return null;
    }

    @Override
    public void close() {
        ignite.destroyCache(CACHE_NAME);
    }

}
