package com.github.sergiords.ignite.server;

public class ServerApp3 {

    public static void main(String[] args) {

        // shortcut to set "node.id" System Property
        System.setProperty("node.id", "Server3");

        ServerApp.main(args);

    }

}
