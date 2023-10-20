package com.example.MeetingPlanner_IrlindDoraci.ModelView;

import com.example.MeetingPlanner_IrlindDoraci.Model.MeetingName;
import com.example.MeetingPlanner_IrlindDoraci.businesslayer.MeetingService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.sql.SQLException;
import java.util.List;

public class HomeScreenViewModel {
    private final MeetingService meetingService;

    public HomeScreenViewModel() {
        this.meetingService = new MeetingService();
    }

    public ObservableList<MeetingName> loadMeetingNames() throws SQLException {
        List<MeetingName> meetingNames = meetingService.getMeetingNames();
        return FXCollections.observableArrayList(meetingNames);
    }

    public void filterMeetingNames(FilteredList<MeetingName> filteredMeetingNames, String query) {
        if (query == null || query.isEmpty()) {
             filteredMeetingNames.setPredicate(null);
        } else {
             filteredMeetingNames.setPredicate(meetingName -> meetingName.getName().toLowerCase().contains(query.toLowerCase()));
        }
    }

    public void deleteMeeting(MeetingName meetingName) throws SQLException {
        meetingService.deleteMeeting(meetingName.getId());
    }
}
