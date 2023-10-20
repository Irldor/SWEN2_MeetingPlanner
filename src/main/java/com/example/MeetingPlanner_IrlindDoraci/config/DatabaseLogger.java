package com.example.MeetingPlanner_IrlindDoraci.config;

import java.sql.Connection;

public class DatabaseLogger implements ConnectionObserver {

    @Override
    public void onConnectionEstablished(Connection connection) {
        System.out.println("Database connected: " + connection.toString());
    }

    @Override
    public void onConnectionClosed() {
        System.out.println("Database connection closed.");
    }
}
