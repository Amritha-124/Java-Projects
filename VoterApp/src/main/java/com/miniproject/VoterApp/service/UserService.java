package com.miniproject.VoterApp.service;

import com.miniproject.VoterApp.entity.ResultDetails;
import com.miniproject.VoterApp.entity.VotingPanel;
import com.miniproject.VoterApp.model.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.List;


public interface UserService
{
    String userSignUp(User user);

    List<Election> home();

    boolean voterRegistration(VoterProfile voterProfile) throws IOException;

    List<VoterProfile> viewVoterProfile(String voterId);

    byte[] getVoterProfilePic(String NameOfVoter) throws Exception;

    String getImageUrl(String voterId);

    List<Election> viewElectionList();

    List<ExistingParty> viewExistingParty();
    List<Party> viewCandidatesByWard(String ward);

    List<Party> viewCandidatesByParty(String partyName);

    byte[] getPartyLogo(String partyName) throws Exception;

    String getLogoURL(String candidateName);

    VotingPanel VotingPanel(String electionName, String ward);

    String castVote(Vote vote);

    List<Election> viewResultPage();

    ResultDetails viewResultDetails(String electionName, String ward);

    // #########################JWT########################################################
    UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException;


    // #########################TFA########################################################
    void update2FAProperties(String mobileNumber, String tfacode);
    boolean checkCode(String mobileNumber, String code, String password);




}
