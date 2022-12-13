package com.miniproject.VoterApp.controller;

import com.miniproject.VoterApp.model.*;
import com.miniproject.VoterApp.service.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{
    @Autowired
    AdminServiceImpl adminService;

    @PostMapping("/adminSignUp")
    public ResponseEntity<String> adminSignUp(@RequestBody Admin admin) {
        if(adminService.adminSignUp(admin).equalsIgnoreCase("Sign up Failed")){
            return new ResponseEntity<>("Sign up Failed",HttpStatus.ALREADY_REPORTED);
        }
        return new ResponseEntity<>("Admin Sign up successful, Welcome to voting app", HttpStatus.CREATED);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/adminSignIn/{emailId}/{password}")
    public ResponseEntity<Integer> adminSignIn(@PathVariable String emailId, @PathVariable String password) {
        return new ResponseEntity<>(adminService.adminSignIn(emailId, password), HttpStatus.OK);
    }
   //     ---------------------------------------------------------------------------------------------------------------------------------------------
    @GetMapping("/viewVotersList/{sID}")
    public ResponseEntity<List<VoterProfile>> votersList(@PathVariable int sID) {
        if (adminService.votersList(sID).size() > 0) {
            return new ResponseEntity<>(adminService.votersList(sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/viewVotersListByWard/{ward}/{sID}")
    public ResponseEntity<List<VoterProfile>> votersListByWard(@PathVariable String ward, @PathVariable int sID) {
        if(adminService.votersListByWard(ward, sID).size()>0)
        {
            return new ResponseEntity<>(adminService.votersListByWard(ward, sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/getProPic/{NameOfVoter}")
    public ResponseEntity<Resource> getVoterProfilePic(@PathVariable String NameOfVoter) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" + NameOfVoter + ".png" + "\"").body(new ByteArrayResource(adminService.getVoterProfilePic(NameOfVoter)));
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/getImageURL/{voterId}/{sID}")
    public ResponseEntity<String> getImageUrl(@PathVariable String voterId, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.getImageUrl(voterId, sID), HttpStatus.OK);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/voterByName/{NameOfVoter}/{sID}")
    public ResponseEntity<List<VoterProfile>> getVoterByName(@PathVariable String NameOfVoter, @PathVariable int sID) {
        if(adminService.viewVoterByName(NameOfVoter, sID).size()>0) {
            return new ResponseEntity<>(adminService.viewVoterByName(NameOfVoter, sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/addElection/{sID}")
    public ResponseEntity<String> addElection(@RequestBody Election election, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.addElection(election, sID), HttpStatus.CREATED);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @PatchMapping ("/updateElection/{sID}")
    public ResponseEntity<String> updateElection(@RequestBody Election election, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.updateElection(election, sID), HttpStatus.OK);
    }
    @PatchMapping("/AnnounceResultDate/{sID}")
    public ResponseEntity<String> announcingResultDate(@RequestBody Election election, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.announcingResultDate(election, sID), HttpStatus.OK);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/addExistingParty/{sID}")
    public ResponseEntity<String> addExistingParty(@ModelAttribute ExistingParty existingParty, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.addExistingParty(existingParty, sID), HttpStatus.CREATED);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/viewExistingParty/{sID}")
    public ResponseEntity<List<ExistingParty>> viewExistingParties(@PathVariable int sID) {
        if (adminService.viewExistingParty(sID).size() > 0){
            return new ResponseEntity<>(adminService.viewExistingParty(sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @PostMapping("/addParty/{sID}")
    public ResponseEntity<Boolean> addParty(@ModelAttribute Party party, @PathVariable int sID) throws IOException {
        return new ResponseEntity<>(adminService.addParty(party, sID), HttpStatus.CREATED);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/viewAllParty/{sID}")
    public ResponseEntity<List<Party>> viewAllParty(@PathVariable int sID) {
        if (adminService.viewAllParty(sID).size() > 0) {
            return new ResponseEntity<>(adminService.viewAllParty(sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/getPartyLogo/{partyName}")
    public ResponseEntity<Resource> getPartyLogo(@PathVariable String partyName) throws Exception {
        return ResponseEntity.ok().contentType(MediaType.parseMediaType("image/png")).header("Content-Disposition", "filename=\"" + partyName + ".png" + "\"").body(new ByteArrayResource(adminService.getPartyLogo(partyName)));
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/getLogoURL/{partyName}/{sID}")
    public ResponseEntity<String> getLogoURL(@PathVariable String partyName, @PathVariable int sID) {
        return new ResponseEntity<>(adminService.getLogoURL(partyName, sID), HttpStatus.OK);
    }
    //    ---------------------------------------------------------------------------------------------------------------------------------------------

    @GetMapping("/getCandidateList/{partyName}/{sID}")
    public ResponseEntity<List<Party>> getCandidateList(@PathVariable String partyName, @PathVariable int sID) {
        if (adminService.getCandidateList(partyName,sID).size() > 0) {
            return new ResponseEntity<>(adminService.getCandidateList(partyName, sID), HttpStatus.OK);
        }
        return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    }
   //    ---------------------------------------------------------------------------------------------------------------------------------------------

}
