package com.github.sergiords.ignite.client.part2_data_grid;

import com.github.sergiords.ignite.data.Data;
import com.github.sergiords.ignite.data.Team;
import com.github.sergiords.ignite.data.User;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.cluster.ClusterGroup;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;

import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;

public class Step3_ComputeAffinity {

    private static final String CACHE_NAME = "my-compute-affinity-cache";

    private final Ignite ignite;

    private final IgniteCache<Team, List<User>> cache;

    public Step3_ComputeAffinity(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a cache named "my-compute-affinity-cache"
         */
        CacheConfiguration<Team, List<User>> configuration = new CacheConfiguration<>(CACHE_NAME);

        this.cache = ignite.getOrCreateCache(configuration);
    }

    public void populateCache() {

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

        return ignite.compute()
            .affinityCall(CACHE_NAME, team, () -> cache.get(team).stream().max(comparing(User::getCommits)));
    }

    public ClusterNode findNode(Team team) {

        /*
         * TODO:
         * - affinity function tells in which node cache entries are stored
         * - use ignite.affinity(...) to get affinity function associated to this cache
         * - use affinity.mapKeyToNode(...) to get ClusterNode hosting the given team entry
         */
        Affinity<Team> affinity = ignite.affinity(CACHE_NAME);

        return affinity.mapKeyToNode(team);
    }

    public Optional<User> findTopCommitterFullVersion(Team team) {

        ClusterNode node = findNode(team);

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

        return ignite.compute(clusterGroup)
            .call(() -> cache.get(team).stream().max(comparing(User::getCommits)));
    }

}
