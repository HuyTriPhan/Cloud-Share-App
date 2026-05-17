package com.example.cloudshare.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.cloudshare.domain.UserCredits;
import com.example.cloudshare.dto.UserCreditsDTO;
import com.example.cloudshare.service.UserCreditsService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserCreditsController {
    private final UserCreditsService userCreditsService;

    @GetMapping("/credits")
    public ResponseEntity<?> getUserCredits() {
        UserCredits userCredits = userCreditsService.getUserCredits();
        UserCreditsDTO response = UserCreditsDTO.builder()
                .credits(userCredits.getCredits())
                .plan(userCredits.getPlan())
                .build();

        return ResponseEntity.ok(response);
    }
}
