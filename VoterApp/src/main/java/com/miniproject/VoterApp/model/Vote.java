package com.miniproject.VoterApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Vote {
    private String electionName;
    private String partyName;
    private String voterId;
    private String ward;
}
