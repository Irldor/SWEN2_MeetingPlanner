package com.example.MeetingPlanner_IrlindDoraci.junitTest;

import com.example.MeetingPlanner_IrlindDoraci.Model.Meeting;
import com.example.MeetingPlanner_IrlindDoraci.Model.MeetingName;
import com.example.MeetingPlanner_IrlindDoraci.config.DatabaseConnection;
import com.example.MeetingPlanner_IrlindDoraci.dataaccesslayer.MeetingDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;


public class MeetingDAOTest {

    private MeetingDAO meetingDAO;

    private DatabaseConnection dbConnection;


    @Before
    public void setUp() throws Exception {
        dbConnection = new DatabaseConnection();
        meetingDAO = new MeetingDAO();
    }

    @After
    public void tearDown() throws Exception {
        dbConnection.closeConnection();
    }

    @Test
    public void testAddMeeting() throws SQLException {
        String title = "Test Meeting";
        String agenda = "Discuss testing strategies";
        LocalDate fromDate = LocalDate.now();
        LocalDate toDate = LocalDate.now();
        LocalTime fromTime = LocalTime.of(9, 0);
        LocalTime toTime = LocalTime.of(10, 0);
        String notes = "Bring laptops";

        meetingDAO.addMeeting(title, agenda, fromDate, toDate, fromTime, toTime, notes);

        Meeting meeting = meetingDAO.getMeetingDetailsByTitle(title);

        assertNotNull(meeting);
        assertEquals(title, meeting.getName());
        assertEquals(agenda, meeting.getAgenda());
        assertEquals(fromDate, meeting.getFromDate());
        assertEquals(toDate, meeting.getToDate());
        assertEquals(fromTime, meeting.getFromTime());
        assertEquals(toTime, meeting.getToTime());

    }

    @Test
    public void testGetMeetingNames() throws SQLException {
        List<MeetingName> meetingNames = meetingDAO.getMeetingNames();
        assertNotNull(meetingNames);
        assertFalse(meetingNames.isEmpty());
    }
    @Test
    public void testDeleteMeeting() throws SQLException {

        String title = "Test Meeting for Deletion";
        meetingDAO.addMeeting(title, "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "Notes");
        Meeting meeting = meetingDAO.getMeetingDetailsByTitle(title);
        assertNotNull(meeting);

        meetingDAO.deleteMeeting(meeting.getId());
        meeting = meetingDAO.getMeetingDetailsByTitle(title);
        assertNull(meeting);
    }
    @Test
    public void testGetMeetingDetails() throws SQLException {
        String title = "Test Meeting for Details";
        meetingDAO.addMeeting(title, "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "Notes");
        Meeting meeting = meetingDAO.getMeetingDetailsByTitle(title);
        assertNotNull(meeting);
        assertEquals(title, meeting.getName());
    }
    @Test
    public void testUpdateMeeting() throws SQLException {

        String title = "Test Meeting for Update";
        meetingDAO.addMeeting(title, "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "Notes");
        Meeting meeting = meetingDAO.getMeetingDetailsByTitle(title);
        assertNotNull(meeting);

        String updatedAgenda = "Updated Agenda";
        meetingDAO.updateMeeting(meeting.getId(), updatedAgenda, LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "Updated Notes");
        meeting = meetingDAO.getMeetingDetailsByTitle(title);
        assertEquals(updatedAgenda, meeting.getAgenda());
    }
    @Test
    public void testGetOverlappingMeetings() throws SQLException {

        meetingDAO.addMeeting("Overlapping Meeting 1", "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), "Notes");
        meetingDAO.addMeeting("Overlapping Meeting 2", "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.of(9, 30), LocalTime.of(10, 30), "Notes");

        List<Meeting> overlappingMeetings = meetingDAO.getOverlappingMeetings(LocalDate.now(), LocalDate.now(), LocalTime.of(9, 15), LocalTime.of(9, 45));
        assertEquals(2, overlappingMeetings.size());
    }
    @Test
    public void testIsOverlapping() throws SQLException {

        meetingDAO.addMeeting("Overlapping Meeting", "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.of(9, 0), LocalTime.of(10, 0), "Notes");

        boolean isOverlapping = meetingDAO.isOverlapping(LocalDate.now(), LocalDate.now(), LocalTime.of(9, 30), LocalTime.of(10, 30));
        assertTrue(isOverlapping);
    }

    @Test
    public void testGetMeetingReport() throws SQLException {

        meetingDAO.addMeeting("Meeting for Report", "Agenda", LocalDate.now(), LocalDate.now(), LocalTime.now(), LocalTime.now(), "Notes");

        List<Meeting> meetingReport = meetingDAO.getMeetingReport();
        assertNotNull(meetingReport);

        assertFalse(meetingReport.isEmpty());
    }
}