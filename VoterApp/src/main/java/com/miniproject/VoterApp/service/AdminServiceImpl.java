package com.miniproject.VoterApp.service;

import com.miniproject.VoterApp.entity.CandidateCredentials;
import com.miniproject.VoterApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Service
public class AdminServiceImpl implements AdminService
{
    @Autowired
    JdbcTemplate jdbcTemplate;
    int sessionId;
    boolean partyExistence = false;

    //    CREATING ADMIN ACCOUNT
    @Override
    public String adminSignUp(Admin admin)
    {
        try {
            jdbcTemplate.update("insert into admin(adminName,password,emailId) values (?,?,?)", admin.getAdminName(), admin.getPassword(), admin.getEmailId());
            return "Admin Sign up successful, Welcome to voting app";
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return "Sign up Failed";
    }
    //    __________________________________________________________________________________________________

    //    ADMIN LOGIN
    @Override
    public int adminSignIn(String emailId, String password)
    {
        try {

            String query = "select * from admin where emailId= '" + emailId + "'and password='" + password + "'";
            jdbcTemplate.queryForObject(query, new BeanPropertyRowMapper<>(Admin.class));
            sessionId = new Random().nextInt(1000);
            return sessionId;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    //   __________________________________________________________________________________________________

    //    ADDING  ELECTION
    @Override
    public String addElection(Election election, int sID)
    {
        if (sID == sessionId)
        {
            try {
                jdbcTemplate.update("insert into election (electionName,additionalInstruction,startDateAndTime,endDateAndTime) values(?,?,?,?)"
                        , election.getElectionName(), election.getAdditionalInstructions(), election.getStartDateAndTime(), election.getEndDateAndTime());
                return "election added ";
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return "This election is already present";
            }
        }
        return "invalid id";
    }
//    __________________________________________________________________________________________________

    //    UPDATE ELECTION
    @Override
    public String updateElection(Election election, int sID)
    {
        if (sID == sessionId) {
            int count =jdbcTemplate.queryForObject("select count(electionName) from election where electionName='"+election.getElectionName()+"'",Integer.class);
            try {
                if (count > 0)
                {
                    jdbcTemplate.update("update election set additionalInstruction ='" + election.getAdditionalInstructions() + "',startDateAndTime='" + election.getStartDateAndTime() + "',endDateAndTime='" + election.getEndDateAndTime() + "' where electionName='" + election.getElectionName()+ "'");
                    return "Election "+election.getElectionName()+" updated " ;
                }
                return "Incorrect election Name";

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return "invalid";
    }
    //    __________________________________________________________________________________________________

   //   RESULT ANNOUNCING
    @Override
    public String announcingResultDate(Election election, int sID) {
        if (sID == sessionId) {
            int count =jdbcTemplate.queryForObject("select count(electionName) from election where electionName='"+election.getElectionName()+"'",Integer.class);
            try {
                if (count > 0)
                {
                    jdbcTemplate.update("update election set resultDate='" + election.getResultDate() + "' where electionName='" + election.getElectionName()+ "'");
                    return "Election "+election.getElectionName()+" updated ";
                }
                return "Incorrect election Name";

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return "invalid";

    }
    //    __________________________________________________________________________________________________

    //    VIEWING VOTER LIST
    @Override
    public List<VoterProfile> votersList(int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select NameOfVoter,gender,age,ward,voterId,aadharNumber,mobileNumber,address,imageUrl from voterProfile", new BeanPropertyRowMapper<>(VoterProfile.class));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("invalid id");
        return null;
    }

    //    __________________________________________________________________________________________________
    @Override
    public List<VoterProfile> votersListByWard(String ward, int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select NameOfVoter,gender,age,ward,voterId,aadharNumber,mobileNumber,address,imageUrl from voterProfile where ward ='" + ward + "'", new BeanPropertyRowMapper<>(VoterProfile.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("invalid id");
        return null;
    }
    //    __________________________________________________________________________________________________

    //    VIEW VOTER PROFILE PIC
    @Override
    public byte[] getVoterProfilePic(String NameOfVoter) throws Exception
    {
        try {

            return jdbcTemplate.queryForObject("select profilePic from voterProfile where NameOfVoter = '" + NameOfVoter + "'", byte[].class);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

    }

    //    __________________________________________________________________________________________________
    @Override
    public String getImageUrl(String voterId, int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.queryForObject("select imageUrl from voterProfile where voterId = '" + voterId + "'", String.class);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                return "Incorrect credentials";
            }
        }
        return"invalid id";
    }
    //    __________________________________________________________________________________________________

    //    VIEW VOTER BY NAME
    @Override
    public List<VoterProfile> viewVoterByName(String NameOfVoter, int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select NameOfVoter,gender,age,ward,voterId,aadharNumber,mobileNumber,address,imageUrl from voterProfile where NameOfVoter = '"
                        + NameOfVoter + "'", new BeanPropertyRowMapper<>(VoterProfile.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("voter not registered");
            }
        }
        System.out.println("invalid user");
        return null;
    }
    //    __________________________________________________________________________________________________

    //    ADDING EXISTING PARTY
    @Override
    public String addExistingParty(ExistingParty existingParty, int sID)
    {
        if (sID == sessionId) {
            String fileName = StringUtils.cleanPath(existingParty.getPartyLogo().getOriginalFilename());
            String downloadURL;
            try {
                if (fileName.contains("..")) {
                    throw new Exception("file name is invalid" + fileName);
                }
                downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/getPartyLogo/")
                        .path(existingParty.getPartyName())
                        .toUriString();

                String logoURL = downloadURL;
                jdbcTemplate.update("insert into existingParty(partyName,partyLogo,logoURL) values(?,?,?)", existingParty.getPartyName(), existingParty.getPartyLogo().getBytes(), logoURL);

                return "added successfully";

            } catch (Exception e) {
                e.printStackTrace();
                return "party already existed or cannot add";
            }

        }
        return "invalid id";
    }
    //    __________________________________________________________________________________________________

    //    VIEWING EXISTING PARTY LIST
    @Override
    public List<ExistingParty> viewExistingParty(int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select partyName,logoURL from existingParty", new BeanPropertyRowMapper<>(ExistingParty.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("invalid user");
        return null;
    }
    //    __________________________________________________________________________________________________

    //  ADDING PARTY
    @Override
    public boolean addParty(Party party, int sID) throws IOException
    {
        if (sID == sessionId) {
            if (party.getPartyType().equalsIgnoreCase("EXISTING PARTY")) {
                int count = jdbcTemplate.queryForObject("select count(*) from existingParty where partyName='" + party.getPartyName() + "'", Integer.class);
                if (count > 0) {
                    partyExistence = true;
                }
            } else {
                if (party.getPartyType().equalsIgnoreCase("NEW PARTY")) {
                    String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/getPartyLogo/")
                            .path(party.getPartyName())
                            .toUriString();
                    String logoURL = downloadURL;
                    jdbcTemplate.update("insert into existingParty(partyName,partyLogo,logoURL) values(?,?,?)", party.getPartyName(), party.getPartyLogo().getBytes(), logoURL);
                }
                partyExistence = true;
            }
            if (partyExistence) {
                String fileName = StringUtils.cleanPath(party.getPartyLogo().getOriginalFilename());
                String downloadURL;
                CandidateCredentials candidateCredentials = jdbcTemplate.queryForObject("select gender, age ,ward from voterProfile where voterId = '" + party.getVoterId() + "'", new BeanPropertyRowMapper<>(CandidateCredentials.class));
                if (party.getAge() < 18) {
                    return false;
                }
                try {
                    if (fileName.contains("..")) {
                        throw new Exception("file name is invalid" + fileName);
                    }
                    downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/getPartyLogo/")
                            .path(party.getPartyName())
                            .toUriString();

                    String logoUrl = downloadURL;
                    if (candidateCredentials.getGender().equalsIgnoreCase(party.getGender()) && candidateCredentials.getAge() == party.getAge() && candidateCredentials.getWard().equalsIgnoreCase(party.getWard())) {
                        jdbcTemplate.update("insert into party(partyType,partyName,partyLogo,electionName,candidateName,gender,age,ward,voterId,voteCount,logoURL) values(?,?,?,?,?,?,?,?,?,?,?)",
                                party.getPartyType(), party.getPartyName(), party.getPartyLogo().getBytes(), party.getElectionName(), party.getCandidateName(), party.getGender(), party.getAge(), party.getWard(), party.getVoterId(),
                                party.getVoteCount(), logoUrl);
                        return true;
                    }
                    return false;

                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        return false;
    }
    //    __________________________________________________________________________________________________

    //        VIEWING PARTY LIST
    @Override
    public List<Party> viewAllParty(int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select partyType,partyName,candidateName,electionName,gender,age,ward,voterId,voteCount,logoURL from party", new BeanPropertyRowMapper<>(Party.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("invalid user");
        return null;
    }
    //    __________________________________________________________________________________________________

    //  GETTING PARTY LOGO
    @Override
    public byte[] getPartyLogo(String partyName) throws Exception
    {
        try {
            return jdbcTemplate.queryForObject("select partyLogo from existingParty where partyName = '" + partyName + "'", byte[].class);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    //    __________________________________________________________________________________________________

    //    LOGO URL
    @Override
    public String getLogoURL(String partyName, int sID)
    {
        if (sID == sessionId) {
            return jdbcTemplate.queryForObject("select logoURL from existingParty where partyName = '" + partyName + "'", String.class);
        }
        return null;
    }
    //    __________________________________________________________________________________________________

    //    CANDIDATE LIST
    @Override
    public List<Party> getCandidateList(String partyName, int sID)
    {
        if (sID == sessionId) {
            try {
                return jdbcTemplate.query("select partyType,partyName,candidateName,electionName,gender,age,ward,voterId,voteCount,logoURL from party where partyName ='" + partyName + "'", new BeanPropertyRowMapper<>(Party.class));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("invalid user");
        return null;
    }
    //    __________________________________________________________________________________________________

}
