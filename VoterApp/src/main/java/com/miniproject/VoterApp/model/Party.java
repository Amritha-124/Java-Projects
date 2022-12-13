package com.miniproject.VoterApp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Party {
    public String partyType;
    private String partyName;
    private MultipartFile partyLogo;
    private String electionName;
    private String candidateName;
    private String gender;
    private int age;
    private String ward;
    private String voterId;
    private int voteCount;
    private String logoURL;

}
