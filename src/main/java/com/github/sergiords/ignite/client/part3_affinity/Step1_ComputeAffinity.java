package com.github.sergiords.ignite.client.part3_affinity;

import com.github.sergiords.ignite.client.ClientStep;
import com.github.sergiords.ignite.data.AffinityData;
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

public class Step1_ComputeAffinity implements ClientStep {

    private static final String CACHE_NAME = "my-compute-affinity-cache";

    private final Ignite ignite;

    private final Affinity<Team> affinity;

    private final IgniteCache<Team, List<User>> cache;

    public Step1_ComputeAffinity(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - create a partitioned cache named "my-compute-affinity-cache" just like in Step1_PartitionedCache
         */
        CacheConfiguration<Team, List<User>> configuration = new CacheConfiguration<>(CACHE_NAME);
        configuration.setCacheMode(CacheMode.PARTITIONED);
        this.cache = ignite.getOrCreateCache(configuration);

        /*
         * TODO:
         * - populate cache
         * - use AffinityData#teams to find teams
         * - use AffinityData#users to find users for a team
         */
        AffinityData.teams().forEach(team -> cache.put(team, AffinityData.users(team)));

        /*
         * TODO:
         * - get the affinity function associated to the cache named "my-compute-affinity-cache"
         * - affinity is a function telling in which node keys are stored
         * - use Ignite#affinity
         */
        this.affinity = ignite.affinity(CACHE_NAME);
    }

    @Override
    public void run() {

        Team team = AffinityData.teams().get(5);

        int partition = findPartition(team);

        ClusterNode nodeForPartition = findNode(partition);

        ClusterNode nodeForKey = findNode(team);

        String nodeIdForKey = findNodeId(team);

        Optional<User> resultFromNode = executeOnNode(team);

        Optional<User> resultFromAffinity = executeWithCacheAffinity(team);

        System.out.printf("===== ComputeAffinity =====%n");
        System.out.printf("Team %s is in partition %d%n", team, partition);
        System.out.printf("Partition %d is on node %s%n", partition, nodeForPartition.attribute("node.id"));
        System.out.printf("Team %s is on node %s [mapNodeToKey]%n", team, nodeForKey.attribute("node.id"));
        System.out.printf("AffinityCall returned nodeId %s%n", nodeIdForKey);
        System.out.printf("Team %s has TopCommitter %s [Result from targeted node]%n", team, resultFromNode);
        System.out.printf("Team %s has TopCommitter %s [Result from affinityCall]%n", team, resultFromAffinity);
    }

    private int findPartition(Team team) {

        /*
         * TODO:
         * - return the partition associated to the given team
         * - use Affinity#partition
         */
        return affinity.partition(team);
    }

    private ClusterNode findNode(int partition) {

        /*
         * TODO:
          * - return ClusterNode associated to the given partition (the primary node)
          * - use Affinity#mapPartitionTo..
         */
        return affinity.mapPartitionToNode(partition);
    }

    private ClusterNode findNode(Team team) {

        /*
         * TODO:
         * - return ClusterNode associated to the given team
         * - this method is a shortcut of the two previous methods implemented above
         * - use Affinity#mapKeyTo...
         */
        return affinity.mapKeyToNode(team);
    }

    private String findNodeId(Team team) {

        /*
         * TODO:
         * - return nodeId from ClusterNode where the affinity call is executed
         * - use IgniteCompute#affinityCall and System.getProperty("node.id")
         */
        return ignite.compute().affinityCall(CACHE_NAME, team, () -> System.getProperty("node.id"));
    }

    private Optional<User> executeOnNode(Team team) {

        /*
         * TODO:
         * - return topCommitter for the given team using a simple IgniteCompute#call
         * - use findNode to find ClusterNode where team is stored
         * - use IgniteCluster#forNode to get ClusterGroup for this ClusterNode
         */
        ClusterNode node = findNode(team);
        ClusterGroup clusterGroup = ignite.cluster().forNode(node);
        return ignite.compute(clusterGroup).call(topCommitterCallable(team));
    }

    private Optional<User> executeWithCacheAffinity(Team team) {

        /*
         * TODO:
         * - return topCommitter for the given team using an IgniteCompute#affinityCall
         * - this method is a shortcut of the previous methods used (ClusterNode -> ClusterGroup -> IgniteCompute#Call)
         */
        return ignite.compute().affinityCall(CACHE_NAME, team, topCommitterCallable(team));
    }

    private IgniteCallable<Optional<User>> topCommitterCallable(Team team) {

        /*
         * TODO:
         * - return an IgniteCallable which returns the user from the given team wo has the most commits
         * - use the cache to find users in the team
         */
        return () -> cache.get(team).stream().max(comparing(User::getCommits));
    }

    @Override
    public void close() {
        ignite.destroyCache(CACHE_NAME);
    }

}
