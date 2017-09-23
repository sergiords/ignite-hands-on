package com.github.sergiords.ignite.server;

public class ServerApp1 {

    public static void main(String[] args) {

        // shortcut to set "node.id" System Property
        System.setProperty("node.id", "Server1");

        ServerApp.main(args);

    }

}
