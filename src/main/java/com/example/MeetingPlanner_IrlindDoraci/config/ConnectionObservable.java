package com.example.MeetingPlanner_IrlindDoraci.config;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ConnectionObservable {
    private List<ConnectionObserver> observers = new ArrayList<>();
    private Connection connection;

    public void addConnectionObserver(ConnectionObserver observer) {
        observers.add(observer);
    }

    public void removeConnectionObserver(ConnectionObserver observer) {
        observers.remove(observer);
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
        notifyConnectionEstablished();
    }

    public void closeConnection() {
        this.connection = null;
        notifyConnectionClosed();
    }

    private void notifyConnectionEstablished() {
        for (ConnectionObserver observer : observers) {
            observer.onConnectionEstablished(connection);
        }
    }

    private void notifyConnectionClosed() {
        for (ConnectionObserver observer : observers) {
            observer.onConnectionClosed();
        }
    }
}
