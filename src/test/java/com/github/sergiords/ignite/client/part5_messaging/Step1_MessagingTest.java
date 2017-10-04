package com.github.sergiords.ignite.client.part5_messaging;

import com.github.sergiords.ignite.server.ServerApp;
import com.github.sergiords.ignite.server.ServerAppTest;
import org.apache.ignite.Ignite;
import org.awaitility.Duration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.awaitility.Awaitility.await;

@ExtendWith(ServerAppTest.class)
class Step1_MessagingTest {

    private final Ignite ignite;

    Step1_MessagingTest(Ignite ignite) {
        this.ignite = ignite;
    }

    @Test
    @DisplayName("should dispatch message to all nodes using topic")
    void sendMessage() {

        try (Step1_Messaging step = new Step1_Messaging(ignite)) {

            // check message is effectively sent/received to/from a topic
            ignite.message().send("my-topic", "Hello Topic World!");

            await()
                .atMost(Duration.FIVE_SECONDS)
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(() -> expectMessage("Hello Topic World!"));

            // check message is effectively sent/received to/from ServerApp
            step.sendMessage("Hello World!");

            await()
                .atMost(Duration.FIVE_SECONDS)
                .pollInterval(Duration.ONE_HUNDRED_MILLISECONDS)
                .until(() -> expectMessage("Hello World!"));

        }

    }

    private boolean expectMessage(String expected) {
        return ignite.compute()
            .broadcast(ServerApp::watch).stream()
            .allMatch(expected::equals);
    }
}
