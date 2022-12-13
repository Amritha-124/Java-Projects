package com.miniproject.VoterApp.service;

import com.miniproject.VoterApp.entity.*;
import com.miniproject.VoterApp.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService
{
    @Autowired
    JdbcTemplate jdbcTemplate;

    //  CREATING USER ACCOUNT
    @Override
    public String userSignUp(User user)
    {
        try {
            jdbcTemplate.update("insert into user(mobileNumber,password,emailId) values (?,?,?)", user.getMobileNumber(), user.getPassword(), user.getEmailId());
            return "Sign up successful, Welcome to voting app ";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "Sign up Failed";

        }

    }
    //    __________________________________________________________________________________________________

   //    VIEWING UPCOMING ELECTIONS
   @Override
    public List<Election> home()
    {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return jdbcTemplate.query("select electionName,startDateAndTime from election where startDateAndTime > '" + currentDateTime + "' order by startDateAndTime asc", new BeanPropertyRowMapper<>(Election.class));
    }
    //    __________________________________________________________________________________________________

    //   VOTER REGISTRATION
    @Override
    public boolean voterRegistration(VoterProfile voterProfile) throws IOException
    {
        String fileName = StringUtils.cleanPath(voterProfile.getProfilePic().getOriginalFilename());
        String downloadURL;
        if (voterProfile.getAge() < 18) {
            return false;
        }
        try {
            if (fileName.contains("..")) {
                throw new Exception("file name is invalid" + fileName);
            }
            downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/getProPic/")
                    .path(voterProfile.getNameOfVoter())
                    .toUriString();

            String imageUrl = downloadURL;
            jdbcTemplate.update("insert into voterProfile(NameOfVoter,profilePic,gender,age,ward,voterId,aadharNumber,mobileNumber,address,imageUrl) values(?,?,?,?,?,?,?,?,?,?)",
                    voterProfile.getNameOfVoter(), voterProfile.getProfilePic().getBytes(), voterProfile.getGender(), voterProfile.getAge(), voterProfile.getWard(), voterProfile.getVoterId(),
                    voterProfile.getAadharNumber(), voterProfile.getMobileNumber(), voterProfile.getAddress(), imageUrl);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("duplicate entry");
            return false;
        }
    }
    //    __________________________________________________________________________________________________

    //    VIEWING OUR OWN PROFILE
    @Override
    public List<VoterProfile> viewVoterProfile(String voterId)
    {
        try {
            return jdbcTemplate.query("select NameOfVoter,gender,age,ward,voterId,aadharNumber,mobileNumber,address,imageUrl from voterProfile where voterId = '"
                    + voterId + "'", new BeanPropertyRowMapper<>(VoterProfile.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //    __________________________________________________________________________________________________

    // VOTER PROFILE PIC
    @Override
    public byte[] getVoterProfilePic(String NameOfVoter) throws Exception
    {
        return jdbcTemplate.queryForObject("select profilePic from voterProfile where NameOfVoter = '" + NameOfVoter + "'", byte[].class);
    }
    //    __________________________________________________________________________________________________

   // PROFILE PIC URL
    @Override
    public String getImageUrl(String voterId)
    {
        return jdbcTemplate.queryForObject("select imageUrl from voterProfile where voterId = '" + voterId + "'", String.class);
    }
    //    __________________________________________________________________________________________________

   // GET ELECTION LIST
    @Override
    public List<Election> viewElectionList()
    {
        try {
            return jdbcTemplate.query("select electionName,startDateAndTime,endDateAndTime from election", new BeanPropertyRowMapper<>(Election.class));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    //    __________________________________________________________________________________________________

    //    VIEWING EXISTING PARTY LIST
    @Override
    public List<ExistingParty> viewExistingParty()
    {
        try {
            return jdbcTemplate.query("select partyName,logoURL from existingParty", new BeanPropertyRowMapper<>(ExistingParty.class));
            }catch (Exception e) {
            System.out.println(e.getMessage());
            return  null;
        }
    }
    //    __________________________________________________________________________________________________

    //   VIEWING CANDIDATES OF PARTICULAR WARD
    @Override
    public List<Party> viewCandidatesByWard(String ward)
    {
        try {
            return jdbcTemplate.query("select partyName,candidateName,ward,logoURL from party where ward = '" + ward + "'", new BeanPropertyRowMapper<>(Party.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("ward not found");
            return null;
        }
    }
    //    __________________________________________________________________________________________________


    //    VIEWING CANDIDATES OF PARTICULAR PARTY
    @Override
    public List<Party> viewCandidatesByParty(String partyName)
    {
        try {
            return jdbcTemplate.query("select candidateName ,partyName,logoURL from party where partyName = '" + partyName + "'", new BeanPropertyRowMapper<>(Party.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("invalid");
            return null;
        }
    }
    //    __________________________________________________________________________________________________

    // PARTY LOGO
    public byte[] getPartyLogo(String partyName) throws Exception
    {
        return jdbcTemplate.queryForObject("select partyLogo from existingParty where partyName = '" + partyName + "'", byte[].class);
    }
    //    __________________________________________________________________________________________________

    //  LOGO URL
    @Override
    public String getLogoURL(String partyName)
    {
        return jdbcTemplate.queryForObject("select logoURL from existingParty where partyName = '" + partyName + "'", String.class);
    }
    //    __________________________________________________________________________________________________

   // VIEW VOTING PANEL
    @Override
    public VotingPanel VotingPanel(String electionName, String ward)
    {
        try {
            VotingPanel votingPanel = jdbcTemplate.queryForObject(" select electionName as electionName,startDateAndTime as startDateAndTime from election where electionName =?", new BeanPropertyRowMapper<>(VotingPanel.class), electionName);
            List<PartiesList> parties = jdbcTemplate.query("select logoURL as logoURL ,partyName as partyName from party where electionName='" + electionName + "'and ward='" + ward + "'", new BeanPropertyRowMapper<>(PartiesList.class));
            votingPanel.setParties(parties);
            return votingPanel;
        } catch (Exception e) {
            System.out.println("error");
            System.out.println(e.getMessage());
            return null;
        }
    }
    //    __________________________________________________________________________________________________

    //     VOTING
    @Override
    public String castVote(Vote vote)
    {
        CandidateCredentials voterCredentials = jdbcTemplate.queryForObject("select ward from voterProfile where voterId = '" + vote.getVoterId() + "'", new BeanPropertyRowMapper<>(CandidateCredentials.class));
        try {
            LocalDateTime now = LocalDateTime.now();
            Timestamp timestamp = Timestamp.valueOf(now);
            Timestamp start_date = jdbcTemplate.queryForObject("select startDateAndTime from election where electionName ='" + vote.getElectionName() + "'", Timestamp.class);
            Timestamp end_date = jdbcTemplate.queryForObject("select endDateAndTime from election where electionName ='" + vote.getElectionName() + "'", Timestamp.class);
            if (timestamp.after(start_date) && timestamp.before(end_date)) {
                if (voterCredentials.getWard().equalsIgnoreCase(vote.getWard())) {

                    jdbcTemplate.update("insert into vote (electionName,partyName,voterId,ward) values (?,?,?,?)", vote.getElectionName(), vote.getPartyName(), vote.getVoterId(), vote.getWard());
                    jdbcTemplate.update("update party set voteCount = voteCount+1 where electionName = '" + vote.getElectionName() + "'and partyName ='" + vote.getPartyName() + "'and ward ='" + vote.getWard() + "'");
                    return "vote registered";
                }
                return "cannot vote in this ward";
            }
            return "election is not active now";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "already voted or invalid entries";
        }
    }
    //    __________________________________________________________________________________________________

    //    VIEW RESULT PAGE
    @Override
    public List<Election> viewResultPage()
    {
        try {
            LocalDateTime currentDateTime = LocalDateTime.now();
            return jdbcTemplate.query("select electionName from election where endDateAndTime >'" + currentDateTime + "' ", new BeanPropertyRowMapper<>(Election.class));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

    }
    //    __________________________________________________________________________________________________

   //    VIEW RESULT DETAILS
    @Override
    public ResultDetails viewResultDetails(String electionName, String ward)
    {
        try {
            ResultDetails resultDetails = jdbcTemplate.queryForObject(" select party.candidateName as candidateName,party.electionName as electionName,party.ward as ward,voterProfile.imageUrl as imageUrl,party.voteCount from party  " +
                            "inner join voterProfile on  party.candidateName =voterProfile.NameOfVoter where party.electionName='" + electionName + "' and party.ward ='" + ward + "'and party.voteCount=(select max(voteCount) from party where electionName=? and ward =?)",
                    new BeanPropertyRowMapper<>(ResultDetails.class), electionName,ward);
            List<ResultList> resultList = jdbcTemplate.query("select candidateName as candidateName,partyName as partyName,logoURL as logoURL,voteCount as voteCount from party where electionName='" + electionName + "' and ward ='" + ward + "' order by voteCount desc", new BeanPropertyRowMapper<>(ResultList.class));
            resultDetails.setResultList(resultList);
            return resultDetails;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    //    ######################################GET JWT TOKEN##################################################
    @Override
    public UserDetails loadUserByUsername(String mobileNumber) throws UsernameNotFoundException
    {
        String emailId = jdbcTemplate.queryForObject("select mobileNumber from user where mobileNumber=?", String.class, new Object[]{mobileNumber});
        String password = jdbcTemplate.queryForObject("select password from user where mobileNumber=?", String.class, new Object[]{mobileNumber});
        return new org.springframework.security.core.userdetails.User(emailId, password, new ArrayList<>());
    }

    //    #######################################GET TFA CODE#################################################
    public void update2FAProperties(String emailId, String tfacode)
    {
        jdbcTemplate.update("update user set 2faCode=?, 2faExpiryTime=? where emailId=?", new Object[]
                {
                        tfacode, (System.currentTimeMillis() / 1000) + 60, emailId
                });
    }

    //    #######################################VERIFY TFA CODE AND UPDATING PASSWORD#################################################
    public boolean checkCode(String emailId, String code, String password)
    {
        try {
            boolean query = jdbcTemplate.queryForObject("select count(*) from user where 2faCode=? and emailId=? and 2faExpiryTime>=?", new Object[]{code, emailId, System.currentTimeMillis() / 1000}, Integer.class) > 0;
            String reset_pass = "update user set password ='" + password + "' where emailId ='" + emailId + "'";
            jdbcTemplate.update(reset_pass);
            System.out.println("updated password");
            return query;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //    __________________________________________________________________________________________________


}
