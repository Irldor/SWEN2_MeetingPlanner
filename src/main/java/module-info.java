module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires kernel;
    requires layout;
    requires org.apache.logging.log4j;
    requires junit;

    opens com.example.MeetingPlanner_IrlindDoraci to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci;
    exports com.example.MeetingPlanner_IrlindDoraci.controller;
    opens com.example.MeetingPlanner_IrlindDoraci.controller to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci.config;
    opens com.example.MeetingPlanner_IrlindDoraci.config to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci.Model;
    opens com.example.MeetingPlanner_IrlindDoraci.Model to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer;
    opens com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci.businesslayer;
    opens com.example.MeetingPlanner_IrlindDoraci.businesslayer to javafx.fxml;
    exports com.example.MeetingPlanner_IrlindDoraci.junitTest to junit;
}
