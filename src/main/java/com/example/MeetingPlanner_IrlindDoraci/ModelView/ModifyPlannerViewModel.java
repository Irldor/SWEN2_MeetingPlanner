package com.example.MeetingPlanner_IrlindDoraci.ModelView;

import com.example.MeetingPlanner_IrlindDoraci.businesslayer.MeetingService;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class ModifyPlannerViewModel {
    private final MeetingService meetingService;

    public ModifyPlannerViewModel() {
        this.meetingService = new MeetingService();
    }

    // Properties for binding to the view
    private final StringProperty noteField = new SimpleStringProperty();
    private final StringProperty addTitle = new SimpleStringProperty();
    private final ObjectProperty<LocalDate> fromDate = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> toDate = new SimpleObjectProperty<>();
    private final StringProperty agenda = new SimpleStringProperty();
    private final StringProperty fromTimePicker = new SimpleStringProperty();
    private final StringProperty toTimePicker = new SimpleStringProperty();
    private final ListProperty<HBox> notesList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private final StringProperty nameTxt = new SimpleStringProperty();

    public StringProperty noteFieldProperty() {
        return noteField;
    }

    public StringProperty addTitleProperty() {
        return addTitle;
    }

    public ObjectProperty<LocalDate> fromDateProperty() {
        return fromDate;
    }

    public ObjectProperty<LocalDate> toDateProperty() {
        return toDate;
    }

    public StringProperty agendaProperty() {
        return agenda;
    }

    public StringProperty fromTimePickerProperty() {
        return fromTimePicker;
    }

    public StringProperty toTimePickerProperty() {
        return toTimePicker;
    }

    public ListProperty<HBox> notesListProperty() {
        return notesList;
    }

    public StringProperty nameTxtProperty() {
        return nameTxt;
    }

    public void addNote(ObservableList<HBox> items) {
        String noteText = noteField.get();
        if (!noteText.isEmpty()) {
            HBox noteItem = new HBox();
            Text noteLabel = new Text(noteText);
            noteItem.getChildren().addAll(noteLabel, createDeleteButton(noteItem, items));
            items.add(noteItem);
            noteField.set("");
        }
    }

    public Button createDeleteButton(HBox noteItem, ObservableList<HBox> items) {
        Button deleteNoteButton = new Button("Delete");
        deleteNoteButton.setStyle(
                "-fx-background-color: linear-gradient(#ff5400, #be18d6);" +
                        "-fx-background-radius: 30;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 12px;" +
                        "-fx-padding: 5px 10px;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0.0, 0, 1);"
        );
        deleteNoteButton.setOnAction(e -> items.remove(noteItem));
        return deleteNoteButton;
    }

    public void addMeeting(
            int id,
            String agenda,
            LocalDate fromDate,
            LocalDate toDate,
            LocalTime fromTime,
            LocalTime toTime,
            String notes
    ) throws SQLException {

        meetingService.updateMeeting(id, agenda, fromDate, toDate, fromTime, toTime, notes);
    }
}
