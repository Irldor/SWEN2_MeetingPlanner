package com.example.MeetingPlanner_IrlindDoraci.config;

import java.sql.Connection;

public interface ConnectionObserver {
    void onConnectionEstablished(Connection connection);
    void onConnectionClosed();
}
