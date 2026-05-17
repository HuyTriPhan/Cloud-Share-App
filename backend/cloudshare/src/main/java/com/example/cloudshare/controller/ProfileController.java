package com.example.cloudshare.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cloudshare.dto.ProfileDTO;
import com.example.cloudshare.service.ProfileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<?> registerProfile(@RequestBody ProfileDTO profileDTO) {
        HttpStatus status = profileService.existsByClerkId(profileDTO.getClerkId()) ? HttpStatus.OK : HttpStatus.CREATED;
        ProfileDTO savedProfile = profileService.createProfile(profileDTO);
        
        return ResponseEntity.status(status).body(savedProfile);
    }
}
