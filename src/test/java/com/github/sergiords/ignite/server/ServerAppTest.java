package com.github.sergiords.ignite.server;

import com.github.sergiords.ignite.Config;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.opentest4j.TestAbortedException;

import java.util.UUID;

public class ServerAppTest implements ParameterResolver, BeforeTestExecutionCallback {

    private static final String CLUSTER_MESSAGE = "3 server nodes must be started before running tests";

    private final Ignite ignite;

    public ServerAppTest() {

        IgniteConfiguration igniteConfiguration = Config.igniteConfiguration()
            .setClientMode(true)
            .setConsistentId("Client")
            .setIgniteInstanceName(UUID.randomUUID().toString());

        try {

            //
            ignite = Ignition.start(igniteConfiguration);

            // reset all caches
            ignite.destroyCaches(ignite.cacheNames());

        } catch (Exception e) {
            throw new TestAbortedException(CLUSTER_MESSAGE);
        }

        int count = ignite.cluster().forServers().nodes().size();
        if (count != 3) {
            throw new TestAbortedException(CLUSTER_MESSAGE);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return parameterContext.getParameter().getType() == Ignite.class;
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) {
        return ignite;
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) throws Exception {
        // reset all registered data
        ignite.compute().broadcast(ServerApp::reset);
    }
}
