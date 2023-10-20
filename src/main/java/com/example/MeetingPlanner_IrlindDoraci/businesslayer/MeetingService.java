package com.example.MeetingPlanner_IrlindDoraci.businesslayer;

import com.example.MeetingPlanner_IrlindDoraci.Model.Meeting;
import com.example.MeetingPlanner_IrlindDoraci.Model.MeetingName;
import com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer.MeetingDAO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MeetingService {

    private final MeetingDAO meetingDAO = new MeetingDAO();

    public void addMeeting(String title, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) throws SQLException {

        meetingDAO.addMeeting(title, agenda, fromDate, toDate, fromTime, toTime, notes);
    }

    public List<MeetingName> getMeetingNames() throws SQLException {

        return meetingDAO.getMeetingNames();
    }
    public List<Meeting> getOverlappingMeetings(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) throws SQLException {
        return meetingDAO.getOverlappingMeetings(fromDate, toDate, fromTime, toTime);
    }

    public void deleteMeeting(int meetingNameId) throws SQLException {
         meetingDAO.deleteMeeting(meetingNameId);
    }

    public Meeting getMeetingDetails(int id) throws SQLException {
         return meetingDAO.getMeetingDetails(id);
    }
    public boolean isOverlapping(LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime) throws SQLException {
        return meetingDAO.isOverlapping(fromDate, toDate, fromTime, toTime);
    }
    public Meeting getMeetingDetailsByTitle(String title) throws SQLException {
        return meetingDAO.getMeetingDetailsByTitle(title);
    }
    public void updateMeeting(int id, String agenda, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, String notes) throws SQLException {
         meetingDAO.updateMeeting(id, agenda, fromDate, toDate, fromTime, toTime, notes);
    }

    public List<Meeting> getMeetingReport() throws SQLException {
         return meetingDAO.getMeetingReport();
    }
    public Meeting getMeetingById(int id) throws SQLException {
        return meetingDAO.getMeetingById(id);
    }

}
