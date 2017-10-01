package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.lang.IgniteCallable;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

public class Step2_ComputeAffinity {

    private static final String CACHE_NAME = "my-compute-affinity-cache";

    private final Ignite ignite;

    private final Affinity<Team> affinity;

    private final IgniteCache<Team, List<User>> cache;

    public Step2_ComputeAffinity(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a partitioned cache named "my-compute-affinity-cache" just like in Step1_Cache
         */
        CacheConfiguration<Team, List<User>> configuration = new CacheConfiguration<>(CACHE_NAME);
        configuration.setCacheMode(CacheMode.PARTITIONED);

        this.cache = ignite.getOrCreateCache(configuration);

        /*
         * TODO:
         * - affinity function tells in which node cache entries are stored
         * - use ignite.affinity(...) to get affinity function associated to this cache
         */
        this.affinity = ignite.affinity(CACHE_NAME);

        /*
         * TODO:
         * - populate cache (Team => List<User>)
         * - use Data.teams() to find teams
         * - use Data.users(team) to find users for a team
         */
        Data.teams().forEach(team -> cache.put(team, Data.users(team)));
    }

    public Optional<User> findTopCommitter(Team team) {

        /*
         * TODO:
         * - use ignite.compute().affinityCall(...) to return user with most commits for the given team
         * - notice that cache.get(team) call does not require network since team is hosted on node where computation is sent
         */
        return ignite.compute().affinityCall(CACHE_NAME, team, topCommitterSearch(team));
    }

    public Optional<User> findTopCommitterFullVersion(Team team) {

        /*
         * TODO:
         * - use affinity.mapKeyToNode(...) to get ClusterNode hosting the given team entry
         */
        ClusterNode node = affinity.mapKeyToNode(team);

        /*
         * TODO:
         * - use ignite.cluster().forNode(...) to get a ClusterGroup with previous node only
         */
        ClusterGroup clusterGroup = ignite.cluster().forNode(node);

        /*
         * TODO:
         * - use ignite.compute(clusterGroup).call(...) to return user with most commits for the given team
         * - notice this is the long version of ignite.compute().affinityCall(...)
         */
        return ignite.compute(clusterGroup).call(topCommitterSearch(team));
    }

    private IgniteCallable<Optional<User>> topCommitterSearch(Team team) {

        return () -> cache.get(team).stream().max(comparing(User::getCommits));
    }

}
