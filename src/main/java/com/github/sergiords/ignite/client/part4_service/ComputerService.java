package com.github.sergiords.ignite.client.part4_service;

import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import java.util.stream.Stream;

public class ComputerService implements Computer, Service {

    @Override
    public void init(ServiceContext ctx) {
        System.out.printf("%s initialized on node %s%n", ctx.name(), System.getProperty("node.id"));
    }

    @Override
    public void execute(ServiceContext ctx) {
        System.out.printf("%s executed on node %s%n", ctx.name(), System.getProperty("node.id"));
    }

    @Override
    public Integer sum(Integer... args) {
        System.out.printf("Sum executed on node %s%n", System.getProperty("node.id"));

        /*
         * TODO:
         * - return the sum of all args
         */
        return Stream.of(args).mapToInt(Integer::intValue).sum();
    }

    @Override
    public void cancel(ServiceContext ctx) {
        System.out.printf("%s cancelled on node %s%n", ctx.name(), System.getProperty("node.id"));
    }
}
