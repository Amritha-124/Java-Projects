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
public class VoterProfile {
    private String NameOfVoter;
    private MultipartFile profilePic;
    private String gender;
    private int age;
    private String ward;
    private String voterId;
    private String aadharNumber;
    private long mobileNumber;
    private String address;
    private String imageUrl;


}
