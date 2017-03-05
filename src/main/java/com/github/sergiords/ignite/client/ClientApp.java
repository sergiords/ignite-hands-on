package com.github.sergiords.ignite.client;

import com.github.sergiords.ignite.client.part1_compute_grid.Step1_Runnable;
import com.github.sergiords.ignite.client.part1_compute_grid.Step2_Callable;
import com.github.sergiords.ignite.client.part2_data_grid.Step1_PartitionedCache;
import com.github.sergiords.ignite.client.part2_data_grid.Step2_ReplicatedCache;
import com.github.sergiords.ignite.client.part2_data_grid.Step3_BackupCache;
import com.github.sergiords.ignite.client.part2_data_grid.Step4_StoredCache;
import com.github.sergiords.ignite.client.part3_affinity.Step1_ComputeAffinity;
import com.github.sergiords.ignite.client.part3_affinity.Step2_DataAffinity;
import com.github.sergiords.ignite.client.part4_service.Step1_ClusterService;
import com.github.sergiords.ignite.client.part4_service.Step2_NodeService;
import com.github.sergiords.ignite.client.part4_service.Step3_CustomService;
import com.github.sergiords.ignite.client.part4_service.Step4_AffinityService;
import com.github.sergiords.ignite.client.part5_messaging.Step1_Messaging;
import com.github.sergiords.ignite.client.part5_messaging.Step2_Event;
import com.github.sergiords.ignite.client.part6_cluster.Step1_Cluster;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.joining;

@SuppressWarnings("ConstantConditions")
public class ClientApp {

    public static void main(String[] args) throws IOException {

        /*
         * TODO:
         * - start an ignite instance with configuration from Config#igniteConfiguration
         * - configure this ignite instance in client mode
         * - this instance is going to be injected in all hands on Steps
         */
        Ignite ignite = null;

        /*
         * ===========================================================
         * DO NOT EDIT code bellow this comment.
         * This code reads stepId and launches appropriate Step Class.
         * ===========================================================
         */

        try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in))) {

            String stepName = readStep(console);

            ClientStep clientStep = STEPS.get(stepName).apply(ignite);

            // Ensure clientStep resources are released when Client terminates
            Runtime.getRuntime().addShutdownHook(new Thread(clientStep::close));

            String stepClassName = clientStep.getClass().getSimpleName();

            IgniteCompute allNodes = ignite.compute(ignite.cluster().forPredicate(node -> true));

            do {

                allNodes.broadcast(() -> System.out.printf("%s => Begin%n", stepClassName));

                clientStep.run();

                allNodes.broadcast(() -> System.out.printf("%s => End%n%n", stepClassName));

            } while (readStepExecution(stepName, console) != null);

        }
    }

    private static String readStep(BufferedReader console) throws IOException {

        String orderedSteps = STEPS.keySet().stream().sorted().collect(joining("|", "\n", ""));

        String step;
        do {

            System.out.printf("%nPlease enter a valid step to execute from:%s%n", orderedSteps);

        } while (!STEPS.containsKey((step = console.readLine())));

        return step;
    }

    private static String readStepExecution(String step, BufferedReader console) throws IOException {

        System.out.printf("Press return to run %s again...%n", step);

        return console.readLine();
    }

    private static Map<String, Function<Ignite, ClientStep>> STEPS = new HashMap<String, Function<Ignite, ClientStep>>() {{
        put("Part1_Step1", Step1_Runnable::new);
        put("Part1_Step2", Step2_Callable::new);
        put("Part2_Step1", Step1_PartitionedCache::new);
        put("Part2_Step2", Step2_ReplicatedCache::new);
        put("Part2_Step3", Step3_BackupCache::new);
        put("Part2_Step4", Step4_StoredCache::new);
        put("Part3_Step1", Step1_ComputeAffinity::new);
        put("Part3_Step2", Step2_DataAffinity::new);
        put("Part4_Step1", Step1_ClusterService::new);
        put("Part4_Step2", Step2_NodeService::new);
        put("Part4_Step3", Step3_CustomService::new);
        put("Part4_Step4", Step4_AffinityService::new);
        put("Part5_Step1", Step1_Messaging::new);
        put("Part5_Step2", Step2_Event::new);
        put("Part6_Step1", Step1_Cluster::new);
    }};

}
