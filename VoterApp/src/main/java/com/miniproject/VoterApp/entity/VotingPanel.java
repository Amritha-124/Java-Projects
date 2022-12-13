package com.miniproject.VoterApp.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VotingPanel {
    private String electionName;
    private Timestamp startDateAndTime;
    List<PartiesList> parties;
}
