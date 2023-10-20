package com.example.MeetingPlanner_IrlindDoraci.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting {
    private final int id;
    private final String agenda;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final LocalTime fromTime;
    private final LocalTime toTime;
    private final String notes;
    private  String name;
    public Meeting(int id, String name, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) {
        this.id = id;
        this.name = name;
        this.agenda = agenda;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.notes = notes;
    }
    public Meeting(int id, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) {
        this.id = id;
        this.agenda = agenda;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.notes = notes;
    }
    public Meeting(ResultSet rs) throws SQLException {
        this.id = rs.getInt("meeting_name_id");
        this.name = rs.getString("meeting_name");
        this.agenda = rs.getString("agenda");
        this.fromDate = rs.getDate("from_date").toLocalDate();
        this.toDate = rs.getDate("to_date").toLocalDate();
        this.fromTime = rs.getTime("from_time").toLocalTime();
        this.toTime = rs.getTime("to_time").toLocalTime();
        this.notes = rs.getString("notes");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgenda() {
        return agenda;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public LocalTime getFromTime() {
        return fromTime;
    }

    public LocalTime getToTime() {
        return toTime;
    }

    public String getNotes() {
        return notes;
    }
}
