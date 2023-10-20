package com.example.MeetingPlanner_IrlindDoraci;

import com.example.MeetingPlanner_IrlindDoraci.config.DatabaseConnection;
import com.example.MeetingPlanner_IrlindDoraci.config.DatabaseLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("homePage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 835, 700);
        stage.setTitle("Meeting Planner!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        // Initialize the observer
        DatabaseConnection.addObserver(new DatabaseLogger());

        launch();
    }
}
