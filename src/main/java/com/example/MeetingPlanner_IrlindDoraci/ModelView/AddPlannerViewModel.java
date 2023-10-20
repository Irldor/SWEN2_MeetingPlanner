package com.example.MeetingPlanner_IrlindDoraci.ModelView;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;

public class AddPlannerViewModel {
    private StringProperty noteField = new SimpleStringProperty();
    private StringProperty addTitle = new SimpleStringProperty();
    private ObjectProperty<LocalDate> fromDate = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDate> toDate = new SimpleObjectProperty<>();
    private StringProperty agenda = new SimpleStringProperty();
    private ListProperty<String> notesList = new SimpleListProperty<>(FXCollections.observableArrayList());
    private StringProperty nameTxt = new SimpleStringProperty();
    private StringProperty fromTimePicker = new SimpleStringProperty();
    private StringProperty toTimePicker = new SimpleStringProperty();

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

    public ListProperty<String> notesListProperty() {
        return notesList;
    }

    public StringProperty nameTxtProperty() {
        return nameTxt;
    }

    public StringProperty fromTimePickerProperty() {
        return fromTimePicker;
    }

    public StringProperty toTimePickerProperty() {
        return toTimePicker;
    }
}
