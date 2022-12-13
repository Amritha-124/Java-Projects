package com.miniproject.VoterApp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CandidateCredentials {
    private String gender;
    private int age;
    private String ward;
}
