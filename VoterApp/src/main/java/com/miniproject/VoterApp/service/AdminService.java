package com.miniproject.VoterApp.service;

import com.miniproject.VoterApp.model.*;

import java.io.IOException;
import java.util.List;


public interface AdminService {

    String adminSignUp(Admin admin);

    int adminSignIn(String emailId, String password);

    List<VoterProfile> votersList(int sID);

    List<VoterProfile> votersListByWard(String ward, int sId);

    byte[] getVoterProfilePic(String NameOfVoter) throws Exception;

    String getImageUrl(String voterId, int sID);

    List<VoterProfile> viewVoterByName(String NameOfVoter, int sID);

    String addElection(Election election, int sID);

    String updateElection(Election election, int sID);

    String announcingResultDate(Election election,int sID);

    String addExistingParty(ExistingParty existingParty, int sID);

    List<ExistingParty> viewExistingParty(int sID);

    boolean addParty(Party party, int sID) throws IOException;

    List<Party> viewAllParty(int sID);

    byte[] getPartyLogo(String partyName) throws Exception;

    String getLogoURL(String candidateName, int sID);

    List<Party> getCandidateList(String partyName, int SID);


}
