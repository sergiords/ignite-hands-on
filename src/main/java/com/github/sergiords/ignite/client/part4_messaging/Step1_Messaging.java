package com.github.sergiords.ignite.client.part4_messaging;

import com.github.sergiords.ignite.server.ServerApp;
import org.apache.ignite.Ignite;

import java.util.Optional;
import java.util.UUID;

public class Step1_Messaging implements AutoCloseable {

    private final Ignite ignite;

    private final UUID listenerUUID;

    public Step1_Messaging(Ignite ignite) {

        this.ignite = ignite;

        /*
         * TODO:
         * - register a remote listener for messages in topic "my-topic"
         * - this listener should send message to ServerApp.send(...) and return true, to keep on listening to messages
         * - use ignite.messaging().remoteListen(...)
         * TIP:
         * - a remote listener registers listeners in each node
         * - a local listener registers a listener in current node only (but listens to events in all nodes)
         */
        listenerUUID = ignite.message().remoteListen("my-topic", (uuid, message) -> {
            ServerApp.send(message);
            return true;
        });
    }

    public void sendMessage(String message) {

        /*
         * TODO:
         * - send given message to topic "my-topic"
         * - use ignite.messaging().send(...)
         */
        ignite.message().send("my-topic", message);
    }

    @Override
    public void close() {
        Optional.ofNullable(listenerUUID)
            .ifPresent(ignite.message()::stopRemoteListen);
    }

}
