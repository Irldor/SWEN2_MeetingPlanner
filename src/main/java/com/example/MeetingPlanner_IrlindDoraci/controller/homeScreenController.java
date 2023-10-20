package com.example.MeetingPlanner_IrlindDoraci.controller;

import com.example.MeetingPlanner_IrlindDoraci.Model.Meeting;
import com.example.MeetingPlanner_IrlindDoraci.ModelView.HomeScreenViewModel;
import com.example.MeetingPlanner_IrlindDoraci.businesslayer.MeetingService;
import com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer.MeetingDAO;
import com.example.MeetingPlanner_IrlindDoraci.Model.MeetingName;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.properties.TextAlignment;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import javafx.scene.layout.Region;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class homeScreenController  implements Initializable {
  private static final Logger logger = LogManager.getLogger(homeScreenController.class);
  @FXML
  private TextField txtSearch;
  @FXML
  private ListView<MeetingName> nameListView;
  @FXML
  private AnchorPane anchorPane;
  private MeetingDAO meetingDAO = new MeetingDAO();
  private MeetingService meetingService = new MeetingService();
  private FilteredList<MeetingName> filteredMeetingNames;
  private HomeScreenViewModel viewModel;



  @FXML
  public void addMeeting() {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MeetingPlanner_IrlindDoraci/addPlanner.fxml"));
      Parent root = loader.load();
      addPlannerController controller = loader.getController();

      controller.setHomeController(this);

      anchorPane.getChildren().clear();  // Clear existing children
      anchorPane.getChildren().setAll(root);  // Set the new content

    } catch (IOException e) {
      e.printStackTrace();
      logger.error("Failed to load the Add Planner screen", e);

      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setHeaderText(null);
      alert.setContentText("Failed to load the Add Planner screen. Please try again.");
      alert.showAndWait();
    }
  }
  public void generateReport(List<Meeting> meetings) {
    try {
      String dest = "MeetingReport.pdf";
      PdfWriter writer = new PdfWriter(dest);
      PdfDocument pdf = new PdfDocument(writer);
      Document document = new Document(pdf);

      // Title Page
      document.add(new Paragraph("Meeting Report")
              .setTextAlignment(TextAlignment.CENTER)
              .setFontSize(24)
              .setBold()
              .setMarginTop(150));
      document.add(new Paragraph("Generated on: " + LocalDate.now().toString())
              .setTextAlignment(TextAlignment.CENTER)
              .setFontSize(18)
              .setItalic());
      document.add(new AreaBreak());

      // Meetings Details
      for (Meeting meeting : meetings) {
        document.add(new Paragraph("Meeting ID: " + meeting.getId())
                .setBold()
                .setFontSize(14));
        document.add(new Paragraph("Meeting Name: " + meeting.getName())
                .setFontSize(12));
        document.add(new Paragraph("Agenda: " + meeting.getAgenda())
                .setFontSize(12));
        document.add(new Paragraph("From Date: " + meeting.getFromDate())
                .setFontSize(12));
        document.add(new Paragraph("To Date: " + meeting.getToDate())
                .setFontSize(12));
        document.add(new Paragraph("From Time: " + meeting.getFromTime())
                .setFontSize(12));
        document.add(new Paragraph("To Time: " + meeting.getToTime())
                .setFontSize(12));
        document.add(new Paragraph("Notes: " + meeting.getNotes())
                .setFontSize(12));
        document.add(new Paragraph("------------------------------------------------")
                .setFontSize(12));
      }

      document.close();
      logger.info("Report generated successfully at MeetingReport.pdf");

    } catch (Exception e) {
      logger.error("Failed to generate report", e);

      e.printStackTrace();
    }
  }

  @FXML
  public void generateReportById() {
    logger.info("Initiating report generation by ID");

    MeetingName selectedName = nameListView.getSelectionModel().getSelectedItem();
    System.out.println(selectedName);
    int selectedId2 = selectedName.getId();
    System.out.println(selectedId2);

    if (selectedName != null) {

      int selectedId = selectedName.getId();

      ProgressBar progressBar = new ProgressBar();
      progressBar.setPrefWidth(350);

      Alert alert = new Alert(Alert.AlertType.INFORMATION);
      alert.setTitle("Report Generation");
      alert.setHeaderText("Generating report, please wait...");
      alert.getDialogPane().setContent(progressBar);
      alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
      alert.show();

      new Thread(() -> {
        try {
          Meeting meeting = meetingService.getMeetingById(selectedId);
          System.out.println(meeting);
          if (meeting != null) {
            List<Meeting> meetings = Arrays.asList(meeting);
            generateReport(meetings);
            Platform.runLater(() -> {
               alert.setAlertType(Alert.AlertType.CONFIRMATION);
              alert.setHeaderText("Report generated successfully!");
              alert.getDialogPane().setContent(null);
            });
            logger.info("Report generation completed successfully");
          } else {
            Platform.runLater(() -> {
               alert.setAlertType(Alert.AlertType.ERROR);
              alert.setHeaderText("No meeting found for the selected ID.");
              alert.getDialogPane().setContent(null);
            });
            logger.warn("No meeting found for the selected ID");
          }
        } catch (SQLException e) {
          logger.error("Failed to generate report", e);

          e.printStackTrace();
          Platform.runLater(() -> {
             alert.setAlertType(Alert.AlertType.ERROR);
            alert.setHeaderText("Failed to generate report.");
            alert.setContentText(e.getMessage());
          });
        }
      }).start();

    } else {
      logger.warn("No meeting selected for report generation");

      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("No meeting selected. Please select a meeting to generate a report.");
      alert.showAndWait();
    }
  }

  @FXML
  public void generateReport() {
    logger.info("Initiating report generation");


    ProgressBar progressBar = new ProgressBar();
    progressBar.setPrefWidth(350);

    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Report Generation");
    alert.setHeaderText("Generating report, please wait...");
    alert.getDialogPane().setContent(progressBar);
    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
    alert.show();

    new Thread(() -> {
      try {
        List<Meeting> meetings = meetingService.getMeetingReport();
        generateReport(meetings);
        Platform.runLater(() -> {
           alert.setAlertType(Alert.AlertType.CONFIRMATION);
          alert.setHeaderText("Report generated successfully!");
          alert.getDialogPane().setContent(null);
        });
        logger.info("Report generation completed successfully");

      } catch (SQLException e) {
        logger.error("Failed to generate report", e);

        e.printStackTrace();
        Platform.runLater(() -> {
           alert.setAlertType(Alert.AlertType.ERROR);
          alert.setHeaderText("Failed to generate report.");
          alert.setContentText(e.getMessage());
        });
      }
    }).start();
  }
  @FXML
  public void modifyMeeting() {
    MeetingName selectedName = nameListView.getSelectionModel().getSelectedItem();
    if (selectedName != null) {
      try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/MeetingPlanner_IrlindDoraci/modifyPlanner.fxml"));
        Parent root = loader.load();

        ModifyPlannerController controller = loader.getController();
        controller.setHomeController(this);
        controller.loadMeetingDetails(selectedName.getId(), selectedName.getName());

        anchorPane.getChildren().clear();  // Clear existing children
        anchorPane.getChildren().setAll(root);  // Set the new content

      } catch (IOException | SQLException e) {
        e.printStackTrace();
        logger.error("Failed to load the Modify Planner screen", e);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Failed to load the Modify Planner screen. Please try again.");
        alert.showAndWait();
      }
    } else {
      logger.warn("No meeting selected for modification");

      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("No meeting selected. Please select a meeting to modify.");
      alert.showAndWait();
    }
  }


  @FXML
  public void handleDeleteButtonClick() {

    MeetingName selectedName = nameListView.getSelectionModel().getSelectedItem();
    if (selectedName != null) {
      try {
        viewModel.deleteMeeting(selectedName);

       // meetingService.deleteMeeting(selectedName.getId());
        System.out.println("Successfully deleted the meeting from the database.");

        // Create a new modifiable list, remove the selected item, and set it on the ListView
        ObservableList<MeetingName> items = FXCollections.observableArrayList(nameListView.getItems());
        items.remove(selectedName);
        nameListView.setItems(items);
        anchorPane.getChildren().clear();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Deletion Successful");
        alert.setHeaderText(null);
        alert.setContentText("The meeting has been successfully deleted.");
        alert.showAndWait();
        logger.info("Meeting deleted successfully");

      } catch (SQLException e) {
        logger.error("Failed to delete the meeting", e);

        e.printStackTrace();
        System.out.println("Failed to delete the meeting from the database.");

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Deletion Failed");
        alert.setHeaderText(null);
        alert.setContentText("Failed to delete the meeting. Please try again.");
        alert.showAndWait();
      }
    } else {
      logger.warn("No meeting selected for deletion");

      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("No Selection");
      alert.setHeaderText(null);
      alert.setContentText("No meeting selected. Please select a meeting to delete.");
      alert.showAndWait();
    }
  }

  public void loadMeetingNames() {
    try {
      List<MeetingName> meetingNames = meetingService.getMeetingNames();
      ObservableList<MeetingName> observableMeetingNames = FXCollections.observableArrayList(meetingNames);
      filteredMeetingNames = new FilteredList<>(observableMeetingNames);
      nameListView.setItems(filteredMeetingNames);
      logger.info("Loaded meeting names successfully");
    } catch (SQLException e) {
      logger.error("Failed to load meeting names", e);
      e.printStackTrace();
    }
  }





  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    logger.info("Initializing homeScreenController");

    this.viewModel = new HomeScreenViewModel();

    try {
      nameListView.setItems(viewModel.loadMeetingNames());
    } catch (SQLException e) {
      logger.error("Failed to load meeting names", e);
      e.printStackTrace();
    }

    FilteredList<MeetingName> filteredMeetingNames = new FilteredList<>(nameListView.getItems());
    nameListView.setItems(filteredMeetingNames);

    txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
      viewModel.filterMeetingNames(filteredMeetingNames, newValue);
    });
  }
}