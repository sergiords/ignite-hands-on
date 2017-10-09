package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.AffinityKey;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.util.Comparator.comparing;

public class Step4_DataAffinity {

    private static final String CACHE_NAME = "my-data-affinity-cache";

    private final Ignite ignite;

    private final IgniteCache<AffinityKey<Team>, List<User>> cache;

    public Step4_DataAffinity(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a partitioned cache named "my-data-affinity-cache" just like in Step1_Cache
         * - notice type for keys is AffinityKey<Team> not just Team
         */
        CacheConfiguration<AffinityKey<Team>, List<User>> configuration = new CacheConfiguration<>(CACHE_NAME);
        configuration.setCacheMode(CacheMode.PARTITIONED);

        this.cache = ignite.getOrCreateCache(configuration);
    }

    public AffinityKey<Team> affinityKey(Team team) {

        /*
         * TODO:
         * - return a new affinity key using team's country as the affinity key discriminator
         * - use new AffinityKey(...)
         */
        return new AffinityKey<>(team, team.getCountry());
    }

    public void populateCache() {

        /*
         * TODO:
         * - populate cache
         * - put all teams from the same country in the same node using affinityKey method defined above
         * - use Data.teams() to find teams
         * - use Data.users(team) to find users for a team
         */
        Data.teams().forEach(team -> cache.put(affinityKey(team), Data.users(team)));
    }

    public Optional<User> findTopCommitterFromCountry(String country) {

        /*
         * TODO:
         * - return top committer for the given country
         *
         * - keep in mind that all teams from a country are hosted in same node
         * - you can safely use ignite.compute().affinityCall(...) to make an affinity search in proper node (use country as affinity key)
         * - on this node, use cache.localEntries() to get all locally stored teams
         * TIP:
         * - cache.localEntries() returns an Iterable... that's annoying
         * - use StreamSupport.stream(myIterator.spliterator(), false) to get a plain-old stream
         */
        return ignite.compute().affinityCall(CACHE_NAME, country, () ->
            StreamSupport.stream(cache.localEntries().spliterator(), false)
                .filter(entry -> entry.getKey().key().getCountry().equals(country))
                .map(Cache.Entry::getValue)
                .flatMap(List::stream)
                .max(comparing(User::getCommits)));
    }

}
