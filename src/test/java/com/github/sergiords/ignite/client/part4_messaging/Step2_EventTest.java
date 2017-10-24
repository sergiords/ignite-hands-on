package com.github.sergiords.ignite.client.part4_messaging;

import com.github.sergiords.ignite.server.ServerApp;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.awaitility.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@ExtendWith(ServerAppTest.class)
class Step2_EventTest {

    private final Ignite ignite;

    Step2_EventTest(Ignite ignite) {
        this.ignite = ignite;
    }

    @BeforeEach
    void setUp() {
        ignite.destroyCaches(ignite.cacheNames());
    }

    @Test
    void listenToEvents() {

        try (Step2_Event step = new Step2_Event(ignite)) {

            IgniteCache<String, String> cache = ignite.cache("my-cache");

            assertThat(cache).isNotNull();

            ignite.compute().run(() -> cache.put("Key42", "Value42"));

            await()
                .atMost(Duration.FIVE_SECONDS)
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(() -> expectMessage("key=Key42"));

            step.cacheValue();

            await()
                .atMost(Duration.FIVE_SECONDS)
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(() -> expectMessage("key=hello"));
        }

    }

    private boolean expectMessage(String expected) {
        return ServerApp.watch().contains(expected);
    }
}
