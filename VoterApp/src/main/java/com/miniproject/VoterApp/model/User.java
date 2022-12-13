package com.miniproject.VoterApp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String mobileNumber;
    private String password;
    private String emailId;

}
