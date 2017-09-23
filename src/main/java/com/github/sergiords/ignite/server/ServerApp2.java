package com.github.sergiords.ignite.server;

public class ServerApp2 {

    public static void main(String[] args) {

        // shortcut to set "node.id" System Property
        System.setProperty("node.id", "Server2");

        ServerApp.main(args);

    }

}
