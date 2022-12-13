package com.miniproject.VoterApp.controller;

import com.miniproject.VoterApp.entity.JWTRequest;
import com.miniproject.VoterApp.entity.JWTResponse;
import com.miniproject.VoterApp.entity.ResultDetails;
import com.miniproject.VoterApp.entity.VotingPanel;
import com.miniproject.VoterApp.model.*;
import com.miniproject.VoterApp.service.EmailService;
import com.miniproject.VoterApp.service.SmsService;
import com.miniproject.VoterApp.service.UserServiceImpl;
import com.miniproject.VoterApp.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Random;

@RestController
public class UserController {
    @Autowired
    EmailService emailService;
    @Autowired
    SmsService smsService;
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserServiceImpl userService;

    @PostMapping("/userSignUp")
    public ResponseEntity<String> userSignUp(@RequestBody User user) {
        if(userService.userSignUp(user).equalsIgnoreCase("Sign up Failed")) {
            return new ResponseEntity<>("Already registered",HttpStatus.ALREADY_REPORTED);
        }

        return new ResponseEntity<>("Sign up successful, Welcome to voting app", HttpStatus.CREATED);
    }

    @GetMapping("/viewHome")
    public ResponseEntity<List<Election>> home() {
        if(userService.home().size()>0) {
            return new ResponseEntity<>(userService.home(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @PostMapping("/voterRegistration")
    public ResponseEntity<Boolean>voterRegistration(@ModelAttribute VoterProfile voterProfile) throws IOException {
//        if(userService.voterRegistration(voterProfile)) {
            return new ResponseEntity<>(userService.voterRegistration(voterProfile), HttpStatus.CREATED);
//        }
//        return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/myProfile/{voterId}")
    public ResponseEntity<List<VoterProfile>> viewVoterProfile(@PathVariable String voterId) {
        if(userService.viewVoterProfile(voterId).size()>0) {
            return new ResponseEntity<>(userService.viewVoterProfile(voterId), HttpStatus.OK);
        }
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getProPic/{NameOfVoter}")
    public ResponseEntity<Resource> getVoterProfilePic(@PathVariable String NameOfVoter) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" + NameOfVoter + ".png" + "\"").body(new ByteArrayResource(userService.getVoterProfilePic(NameOfVoter)));
    }

    @GetMapping("/getImageURL/{voterId}")
    public ResponseEntity<String> getImageUrl(@PathVariable String voterId) {
        return new ResponseEntity<>(userService.getImageUrl(voterId), HttpStatus.OK);
    }

    @GetMapping("/electionList")
    public ResponseEntity<List<Election>> viewElectionList() {
        if(userService.viewElectionList().size()>0) {
            return new ResponseEntity<>(userService.viewElectionList(), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/viewExistingParties")
    public ResponseEntity<List<ExistingParty>> viewExistingParties() {
        if(userService.viewExistingParty().size()>0) {
            return new ResponseEntity<>(userService.viewExistingParty(), HttpStatus.OK);
        }
           return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/candidateListByWard/{ward}")
    public ResponseEntity<List<Party>> viewCandidatesByWard(@PathVariable String ward) {
        if(userService.viewCandidatesByWard(ward).size()>0) {
            return new ResponseEntity<List<Party>>(userService.viewCandidatesByWard(ward), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/candidateListByParty/{partyName}")
    public ResponseEntity<List<Party>> viewCandidatesByParty(@PathVariable String partyName) {
        if(userService.viewCandidatesByParty(partyName).size()>0) {
            return new ResponseEntity<>(userService.viewCandidatesByParty(partyName), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }

    @GetMapping("/getPartyLogoByUser/{partyName}")
    public ResponseEntity<Resource> getPartyLogo(@PathVariable String partyName) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" + partyName + ".png" + "\"").body(new ByteArrayResource(userService.getPartyLogo(partyName)));
    }

    @GetMapping("/getLogoURLByUser/{partyName}")
    public ResponseEntity<String> getLogoURL(@PathVariable String partyName) {
        return new ResponseEntity<>(userService.getLogoURL(partyName), HttpStatus.OK);
    }

    @GetMapping("/getVotingPanel/{electionName}/{ward}")
    public ResponseEntity<VotingPanel> votingPanel(@PathVariable String electionName, @PathVariable String ward) {

        return new ResponseEntity<>(userService.VotingPanel(electionName, ward), HttpStatus.OK);

    }

    @PostMapping("/castVote")
    public ResponseEntity<String> castVote(@RequestBody Vote vote) {
        return new ResponseEntity<>(userService.castVote(vote), HttpStatus.CREATED);
    }

//    ##################################JWT#########################################################

    @GetMapping("/ResultPage")
    public ResponseEntity<List<Election>> viewResultPage() {
        return new ResponseEntity<>(userService.viewResultPage(), HttpStatus.OK);
    }

    //    ##################################JWT#########################################################

//    ##################################TFA######################################

    @GetMapping("/viewResultDetails/{electionName}/{ward}")
    public ResponseEntity<ResultDetails> viewResultDetails(@PathVariable String electionName, @PathVariable String ward) {
        return new ResponseEntity<>(userService.viewResultDetails(electionName, ward), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody JWTRequest jwtRequest) throws Exception {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getMobileNumber(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        final UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getMobileNumber());

        final String token = jwtUtility.generateToken(userDetails);

        return new JWTResponse(token);
    }

    @PutMapping("/user/send2faCodeInEmail")
    public ResponseEntity<Object> send2faCodeInEmail(@RequestParam String mobileNumber, @RequestParam String emailId) throws MessagingException {
        String tfaCode = String.valueOf(new Random().nextInt(9999) + 1000);
        emailService.sendEmail(emailId, tfaCode);
        userService.update2FAProperties(mobileNumber, tfaCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/user/send2faCodeInSMS/{emailId}")
    public ResponseEntity<Object> send2faCodeInSMS(@RequestBody String mobileNumber,@PathVariable String emailId) {
        String tfaCode = String.valueOf(new Random().nextInt(9999) + 1000);
        smsService.sendSms(mobileNumber, tfaCode);
        userService.update2FAProperties(emailId, tfaCode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/verify")
    public ResponseEntity<Object> verify(@RequestParam String emailId, @RequestParam String code, @RequestParam String password) {
        boolean isValid = userService.checkCode(emailId, code, password);
        if (isValid) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);

    }
}
