package com.miniproject.VoterApp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Election {
    private String electionName;
    private String additionalInstructions;
    private Timestamp startDateAndTime;
    private Timestamp endDateAndTime;
    private Timestamp resultDate;

    public void setElectionName(String electionName) {
        this.electionName = electionName;
    }

    public void setAdditionalInstructions(String additionalInstructions) {
        this.additionalInstructions = additionalInstructions;
    }

    public void setStartDateAndTime(String startDateAndTime) {
        this.startDateAndTime = Timestamp.valueOf(startDateAndTime);
    }

    public void setEndDateAndTime(String endDateAndTime) {
        this.endDateAndTime = Timestamp.valueOf(endDateAndTime);
    }

    public void setResultDate(String resultDate) {
        this.resultDate = Timestamp.valueOf(resultDate);
    }
}
