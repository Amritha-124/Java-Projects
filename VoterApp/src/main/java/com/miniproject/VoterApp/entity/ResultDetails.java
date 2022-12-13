package com.miniproject.VoterApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultDetails {
    private String candidateName;
    private String imageUrl;
    private Integer voteCount;
    private String electionName;
    private String ward;
    List<ResultList> resultList;
}
