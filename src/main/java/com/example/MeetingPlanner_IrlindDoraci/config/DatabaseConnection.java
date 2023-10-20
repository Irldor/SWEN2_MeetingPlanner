package com.example.MeetingPlanner_IrlindDoraci.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection connection;
    private static Properties properties = new Properties();
    private static List<ConnectionObserver> observers = new ArrayList<>();

    public static void addObserver(ConnectionObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(ConnectionObserver observer) {
        observers.remove(observer);
    }

    static {
        try (InputStream input = new FileInputStream("C:\\Users\\user\\Desktop\\MeetingPlanner_IrlindDoraci\\src\\main\\java\\com\\example\\MeetingPlanner_IrlindDoraci\\config\\config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = properties.getProperty("db.url");
            String username = properties.getProperty("db.username");
            String password = properties.getProperty("db.password");
            connection = DriverManager.getConnection(url, username, password);

            // Notify observers that the connection has been established
            for (ConnectionObserver observer : observers) {
                observer.onConnectionEstablished(connection);
            }
        }
        return connection;
    }

    public static void closeConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();

            // Notify observers that the connection has been closed
            for (ConnectionObserver observer : observers) {
                observer.onConnectionClosed();
            }
        }
    }
}
