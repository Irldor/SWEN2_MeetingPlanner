package com.example.MeetingPlanner_IrlindDoraci.controller;
import com.example.MeetingPlanner_IrlindDoraci.ModelView.AddPlannerViewModel;
import com.example.MeetingPlanner_IrlindDoraci.businesslayer.MeetingService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class addPlannerController implements Initializable {

    @FXML
    private TextField noteField;
    @FXML
    private TextField addTitle;
    private AddPlannerViewModel viewModel;

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

    private homeScreenController homeController;
    private static final Logger logger = LogManager.getLogger(MeetingService.class);
    private MeetingService meetingService = new MeetingService();

    public void setHomeController(homeScreenController homeController) {
        this.homeController = homeController;
    }
    @FXML
    private void addNote() {
        logger.info("Attempting to add a note");

        String noteText = noteField.getText();
        if (!noteText.isEmpty()) {
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
            noteField.clear();
            logger.info("Note added successfully");

        } else {
            logger.warn("Note text is empty, no note added");
        }
    }

    @FXML
    private void addMeeting() {
        logger.info("Attempting to add a meeting");

        String title = addTitle.getText();
        String agendaText = this.agenda.getText();
        LocalDate startDate = this.fromDate.getValue();
        LocalDate endDate = this.toDate.getValue();
        LocalTime startTime = LocalTime.parse(fromTimePicker.getValue());
        LocalTime endTime = LocalTime.parse(toTimePicker.getValue());
        String notes = getNotes();

        // Validation checks
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        if (startDate.isBefore(currentDate) ||
                (startDate.equals(currentDate) && startTime.isBefore(currentTime)) ||
                startDate.isAfter(endDate) ||
                (startDate.equals(endDate) && (startTime.isAfter(endTime) || startTime.equals(endTime)))) {

            logger.warn("Invalid meeting time. Ensure the start time is in the future and before the end time, and that start and end times are not identical.");

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Meeting Time");
            alert.setHeaderText(null);
            alert.setContentText("Ensure the start time is in the future and before the end time, and that start and end times are not identical.");
            alert.showAndWait();

            return;
        }

        try {
            meetingService.addMeeting(title, agendaText, startDate, endDate, startTime, endTime, notes);
            homeController.loadMeetingNames();
            logger.info("Meeting added successfully");

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Addition Successful");
            alert.setHeaderText(null);
            alert.setContentText("The meeting has been successfully added.");
            alert.showAndWait();

        } catch (SQLException e) {
            logger.error("Failed to add meeting", e);

            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Addition Failed");
            alert.setHeaderText(null);
            alert.setContentText("Failed to add the meeting. Please try again.");
            alert.showAndWait();
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

        viewModel = new AddPlannerViewModel();

        // Bind the noteField property
        noteField.textProperty().bindBidirectional(viewModel.noteFieldProperty());

        // Bind the addTitle property
        addTitle.textProperty().bindBidirectional(viewModel.addTitleProperty());

        // Bind the fromDate property (assuming fromDate is a DatePicker)
        fromDate.valueProperty().bindBidirectional(viewModel.fromDateProperty());

        // Bind the toDate property (assuming toDate is a DatePicker)
        toDate.valueProperty().bindBidirectional(viewModel.toDateProperty());

        // Bind the agenda property (assuming agenda is a TextArea)
        agenda.textProperty().bindBidirectional(viewModel.agendaProperty());

        // Bind the nameTxt property (assuming nameTxt is a Label)
        nameTxt.textProperty().bindBidirectional(viewModel.nameTxtProperty());


        // Bind the fromTimePicker property (assuming fromTimePicker is a ComboBox)
        fromTimePicker.valueProperty().bindBidirectional(viewModel.fromTimePickerProperty());

        // Bind the toTimePicker property (assuming toTimePicker is a ComboBox)
        toTimePicker.valueProperty().bindBidirectional(viewModel.toTimePickerProperty());

        logger.info("Initializing addPlannerController");

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