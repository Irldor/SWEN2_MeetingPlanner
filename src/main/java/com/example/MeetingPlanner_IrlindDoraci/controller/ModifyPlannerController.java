package com.example.MeetingPlanner_IrlindDoraci.controller;

import com.example.MeetingPlanner_IrlindDoraci.Model.Meeting;
import com.example.MeetingPlanner_IrlindDoraci.ModelView.ModifyPlannerViewModel;
import com.example.MeetingPlanner_IrlindDoraci.businesslayer.MeetingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;

public class ModifyPlannerController implements Initializable {

    @FXML
    private TextField noteField;
    @FXML
    private TextField addTitle;
    @FXML
    private DatePicker fromDate;
    @FXML
    private DatePicker toDate;
    @FXML
    private TextArea agenda;
    @FXML
    private ListView<HBox> notesList;
    @FXML
    private Label nameTxt;
    @FXML
    private ComboBox<String> fromTimePicker;
    @FXML
    private ComboBox<String> toTimePicker;
    private int meetingId;

    private MeetingService meetingService = new MeetingService();
    private static final Logger logger = LogManager.getLogger(ModifyPlannerController.class);
    private final ModifyPlannerViewModel viewModel = new ModifyPlannerViewModel();

    private homeScreenController homeController;
    public void setHomeController(homeScreenController homeController) {
        this.homeController = homeController;
    }
    @FXML
    private void addNote() {
        logger.info("Attempting to add a note");

        viewModel.addNote(notesList.getItems());
    }

    @FXML
    private void addMeeting() {
        logger.info("Attempting to add a meeting");

        String agendaText = this.agenda.getText();
        LocalDate fromDateValue = this.fromDate.getValue();
        LocalDate toDateValue = this.toDate.getValue();
        LocalTime fromTimeValue = LocalTime.parse(fromTimePicker.getValue());
        LocalTime toTimeValue = LocalTime.parse(toTimePicker.getValue());
        String notesText = getNotes();

        // Validation checks
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (fromDateValue.isBefore(currentDate) ||
                (fromDateValue.equals(currentDate) && fromTimeValue.isBefore(currentTime)) ||
                fromDateValue.isAfter(toDateValue) ||
                (fromDateValue.equals(toDateValue) && (fromTimeValue.isAfter(toTimeValue) || fromTimeValue.equals(toTimeValue)))) {

            logger.warn("Invalid meeting time. Ensure the start time is in the future and before the end time, and that start and end times are not identical.");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Meeting Time");
            alert.setHeaderText(null);
            alert.setContentText("Ensure the start time is in the future and before the end time, and that start and end times are not identical.");
            alert.showAndWait();

            return;
        }

        try {
            viewModel.addMeeting(meetingId, agendaText, fromDateValue, toDateValue,fromTimeValue , toTimeValue, notesText);
            homeController.loadMeetingNames();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Update Successful");
            alert.setHeaderText(null);
            alert.setContentText("Meeting data has been successfully updated.");
            alert.showAndWait();
            logger.info("Meeting added successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Failed to add meeting", e);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Update Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to update the meeting data. Please try again.");
            alert.showAndWait();
        }
    }

    public void loadMeetingDetails(int id,String name) throws SQLException {
        this.meetingId = id;  // Store the meeting ID when loading the details
        logger.info("Loading meeting details for ID: " + id);

        Meeting meeting = meetingService.getMeetingDetails(id);
        if (meeting != null) {
            nameTxt.setText(name);
            addTitle.setText(name);  // You will need to get the name separately
            agenda.setText(meeting.getAgenda());
            fromDate.setValue(meeting.getFromDate());
            toDate.setValue(meeting.getToDate());
            fromTimePicker.setValue(meeting.getFromTime().toString());
            toTimePicker.setValue(meeting.getToTime().toString());
            logger.info("Meeting details loaded successfully");

            String notes = meeting.getNotes();
            if (notes != null) {
                String[] notesArray = notes.split("\n");
                for (String noteText : notesArray) {
                    if (!noteText.trim().isEmpty()) {
                        HBox noteItem = new HBox();
                        Text noteLabel = new Text(noteText);
                        Button deleteNoteButton = new Button("Delete");
                        deleteNoteButton.setStyle(
                                "-fx-background-color: linear-gradient(#ff5400, #be18d6);" +
                                        "-fx-background-radius: 30;" +
                                        "-fx-text-fill: white;" +
                                        "-fx-font-size: 12px;" +
                                        "-fx-padding: 5px 10px;" +
                                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"
                        );
                        deleteNoteButton.setOnAction(e -> notesList.getItems().remove(noteItem));

                        noteItem.getChildren().addAll(noteLabel, deleteNoteButton);
                        notesList.getItems().add(noteItem);
                    }
                }
            }
            else {
                logger.warn("No meeting details found for ID: " + id);
            }
        }
    }

    private String getNotes() {
        logger.info("Getting notes from the notes list");

        StringBuilder notes = new StringBuilder();
        for (HBox noteItem : notesList.getItems()) {
            Text noteLabel = (Text) noteItem.getChildren().get(0);
            notes.append(noteLabel.getText()).append("\n");
        }
        logger.info("Notes retrieved successfully");

        return notes.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logger.info("Initializing ModifyPlannerController");

        for (int i = 0; i < 24; i++) {

            for (int j = 0; j < 60; j += 15) {
                LocalTime time = LocalTime.of(i, j);
                fromTimePicker.getItems().add(time.toString());
            }
        }
        for (int i = 0; i < 24; i++) {
            for (int j = 0; j < 60; j += 15) {
                LocalTime time = LocalTime.of(i, j);
                toTimePicker.getItems().add(time.toString());
            }
        }
        fromTimePicker.getSelectionModel().selectFirst();
        toTimePicker.getSelectionModel().selectFirst();

    }

}