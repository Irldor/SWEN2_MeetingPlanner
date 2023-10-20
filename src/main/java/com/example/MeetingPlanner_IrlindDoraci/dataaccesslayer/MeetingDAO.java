package com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer;

import com.example.MeetingPlanner_IrlindDoraci.Model.Meeting;
import com.example.MeetingPlanner_IrlindDoraci.Model.MeetingName;
import com.example.MeetingPlanner_IrlindDoraci.config.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MeetingDAO {

    public void addMeeting(String title, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) throws SQLException {
        if(isOverlapping(fromDate, toDate, fromTime, toTime)) {
            System.out.println("The meeting time overlaps with an existing meeting.");
            return;
        }
        String insertMeetingNameSQL = "INSERT INTO meeting_names (name) VALUES (?) RETURNING id";
        String insertMeetingSQL = "INSERT INTO meetings (meeting_name_id, agenda, from_date, to_date, from_time, to_time, notes) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(insertMeetingNameSQL)) {
                pstmt.setString(1, title);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    int meetingNameId = rs.getInt(1);

                    try (PreparedStatement pstmt2 = conn.prepareStatement(insertMeetingSQL)) {
                        pstmt2.setInt(1, meetingNameId);
                        pstmt2.setString(2, agenda);
                        pstmt2.setDate(3, Date.valueOf(fromDate));
                        pstmt2.setDate(4, Date.valueOf(toDate));
                        pstmt2.setTime(5, Time.valueOf(fromTime));
                        pstmt2.setTime(6, Time.valueOf(toTime));
                        pstmt2.setString(7, notes);

                        pstmt2.executeUpdate();
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public List<MeetingName> getMeetingNames() throws SQLException {
        List<MeetingName> meetingNames = new ArrayList<>();
        String sql = "SELECT id, name FROM meeting_names";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                meetingNames.add(new MeetingName(id, name));
            }
        }

        return meetingNames;
    }
    public void deleteMeeting(int meetingNameId) throws SQLException {
        String deleteMeetingSQL = "DELETE FROM meetings WHERE meeting_name_id = ?";
        String deleteMeetingNameSQL = "DELETE FROM meeting_names WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement(deleteMeetingSQL)) {
                pstmt.setInt(1, meetingNameId);
                pstmt.executeUpdate();
            }

            try (PreparedStatement pstmt = conn.prepareStatement(deleteMeetingNameSQL)) {
                pstmt.setInt(1, meetingNameId);
                pstmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
             e.printStackTrace();
        }
    }
    public Meeting getMeetingDetails(int id) throws SQLException {
        String sql = "SELECT * FROM meetings WHERE meeting_name_id = ?";
        Meeting meeting = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String agenda = rs.getString("agenda");
                Date fromDate = rs.getDate("from_date");
                Date toDate = rs.getDate("to_date");
                Time fromTime = rs.getTime("from_time");
                Time toTime = rs.getTime("to_time");
                String notes = rs.getString("notes");

                meeting = new Meeting(id, agenda, fromDate.toLocalDate(), toDate.toLocalDate(), fromTime.toLocalTime(), toTime.toLocalTime(), notes);
            }
        }

        return meeting;
    }

    public void updateMeeting(int id, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) throws SQLException {
        String sql = "UPDATE meetings SET agenda = ?, from_date = ?, to_date = ?, from_time = ?, to_time = ?, notes = ? WHERE meeting_name_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, agenda);
            pstmt.setDate(2, Date.valueOf(fromDate));
            pstmt.setDate(3, Date.valueOf(toDate));
            pstmt.setTime(4, Time.valueOf(fromTime));
            pstmt.setTime(5, Time.valueOf(toTime));
            pstmt.setString(6, notes);
            pstmt.setInt(7, id);

            pstmt.executeUpdate();
        }
    }
    public List<Meeting> getOverlappingMeetings(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) throws SQLException {
        String sql = "SELECT meetings.*, meeting_names.name AS meeting_name " +
                "FROM meetings " +
                "JOIN meeting_names ON meetings.meeting_name_id = meeting_names.id " +
                "WHERE NOT (to_date < ? OR from_date > ?) AND NOT (from_date = ? AND to_time <= ?) AND NOT (to_date = ? AND from_time >= ?)";

        List<Meeting> overlappingMeetings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            pstmt.setDate(3, Date.valueOf(fromDate));
            pstmt.setTime(4, Time.valueOf(fromTime));
            pstmt.setDate(5, Date.valueOf(toDate));
            pstmt.setTime(6, Time.valueOf(toTime));


            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                 overlappingMeetings.add(new Meeting(rs));
            }
        }

        return overlappingMeetings;
    }

    public boolean isOverlapping(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) throws SQLException {
        String sql = "SELECT COUNT(*) " +
                "FROM meetings " +
                "WHERE NOT (to_date < ? OR from_date > ? " +
                "OR (from_date = ? AND to_time <= ?) " +
                "OR (to_date = ? AND from_time >= ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(fromDate));
            pstmt.setDate(2, Date.valueOf(toDate));
            pstmt.setDate(3, Date.valueOf(fromDate));
            pstmt.setTime(4, Time.valueOf(fromTime));
            pstmt.setDate(5, Date.valueOf(toDate));
            pstmt.setTime(6, Time.valueOf(toTime));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }


    public List<Meeting>  getMeetingReport() throws SQLException {
        List<Meeting> meetings = new ArrayList<>();
        String sql = "SELECT meetings.*, meeting_names.name AS meeting_name FROM meetings JOIN meeting_names ON meetings.meeting_name_id = meeting_names.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("meeting_name_id");
                String name = rs.getString("meeting_name");
                String agenda = rs.getString("agenda");
                LocalDate fromDate = rs.getDate("from_date").toLocalDate();
                LocalDate toDate = rs.getDate("to_date").toLocalDate();
                LocalTime fromTime = rs.getTime("from_time").toLocalTime();
                LocalTime toTime = rs.getTime("to_time").toLocalTime();
                String notes = rs.getString("notes");

                meetings.add(new Meeting(id, name, agenda, fromDate, toDate, fromTime, toTime, notes));
            }
        }

        return meetings;
    }
    public Meeting getMeetingById(int meetingId) throws SQLException {
        Meeting meeting = null;
        String sql = "SELECT meetings.*, meeting_names.name AS meeting_name FROM meetings JOIN meeting_names ON meetings.meeting_name_id = meeting_names.id WHERE meeting_names.id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, meetingId);  // Set the meeting ID parameter
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt("meeting_name_id");
                    String name = rs.getString("meeting_name");
                    String agenda = rs.getString("agenda");
                    LocalDate fromDate = rs.getDate("from_date").toLocalDate();
                    LocalDate toDate = rs.getDate("to_date").toLocalDate();
                    LocalTime fromTime = rs.getTime("from_time").toLocalTime();
                    LocalTime toTime = rs.getTime("to_time").toLocalTime();
                    String notes = rs.getString("notes");

                    meeting = new Meeting(id, name, agenda, fromDate, toDate, fromTime, toTime, notes);
                }
            }
        }

        return meeting;
    }

    public Meeting getMeetingDetailsByTitle(String title) throws SQLException {
        String sql = "SELECT meetings.*, meeting_names.name AS meeting_name FROM meetings JOIN meeting_names ON meetings.meeting_name_id = meeting_names.id WHERE meeting_names.name = ?";
        Meeting meeting = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("meeting_name_id");
                String name = rs.getString("meeting_name");
                String agenda = rs.getString("agenda");
                LocalDate fromDate = rs.getDate("from_date").toLocalDate();
                LocalDate toDate = rs.getDate("to_date").toLocalDate();
                LocalTime fromTime = rs.getTime("from_time").toLocalTime();
                LocalTime toTime = rs.getTime("to_time").toLocalTime();
                String notes = rs.getString("notes");

                meeting = new Meeting(id, name, agenda, fromDate, toDate, fromTime, toTime, notes);
            }
        }

        return meeting;
    }

}

