package com.github.sergiords.ignite.server;

import org.apache.ignite.services.Service;
import org.apache.ignite.services.ServiceContext;

import static com.github.sergiords.ignite.server.ServerApp.logger;
import static java.lang.String.format;

public class ComputerService implements Computer, Service {

    public static final String NAME = System.getProperty("node.id");

    @Override
    public void init(ServiceContext ctx) {
        logger.info(() -> format("%s initialized on node %s%n", ctx.name(), NAME));
    }

    @Override
    public void execute(ServiceContext ctx) {
        logger.info(() -> format("%s executed on node %s%n", ctx.name(), NAME));
    }

    @Override
    public Integer sum(Integer x, Integer y) {
        logger.info(() -> format("Sum executed on node %s%n", NAME));
        return x + y;
    }

    @Override
    public void cancel(ServiceContext ctx) {
        logger.info(() -> format("%s cancelled on node %s%n", ctx.name(), NAME));
    }
}
