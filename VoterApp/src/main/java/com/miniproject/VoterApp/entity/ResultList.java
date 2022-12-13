package com.miniproject.VoterApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultList {
    private String candidateName;
    private String partyName;
    private String logoURL;
    private int voteCount;

}
