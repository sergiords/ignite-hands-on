package com.github.sergiords.ignite.client.part5_messaging;

import com.github.sergiords.ignite.client.ClientStep;
import org.apache.ignite.Ignite;

import java.util.UUID;

public class Step1_Messaging implements ClientStep {

    private static final String TOPIC_NAME = "my-topic";

    private final Ignite ignite;

    private final UUID listenerUUID;

    public Step1_Messaging(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - register a remote listener for messages in topic "my-topic"
         * - this listener should print the received message and return true, signaling it keeps listening to messages
         * - use IgniteMessaging#remoteListen
         */
        listenerUUID = ignite.message().remoteListen(TOPIC_NAME, (uuid, message) -> {
            System.out.println("message: " + message);
            return true;
        });
    }

    @Override
    public void run() {

        /*
         * TODO:
         * - send a message "Hello" to the topic "my-topic"
         */
        ignite.message().send("my-topic", "Hello");

        /*
         * TODO:
         * - send a message "Hello from Server Node" to the topic "my-topic" from one of the server nodes
         * - use IgniteCompute#run
         */
        ignite.compute().run(() -> ignite.message().send("my-topic", "Hello from Server Node"));
    }

    @Override
    public void close() {
        ignite.message().stopRemoteListen(listenerUUID);
    }

}
