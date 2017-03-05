package com.github.sergiords.ignite.client;

import java.io.Closeable;

public interface ClientStep extends Runnable, Closeable {

    @Override
    void close(); // suppress exception

}
