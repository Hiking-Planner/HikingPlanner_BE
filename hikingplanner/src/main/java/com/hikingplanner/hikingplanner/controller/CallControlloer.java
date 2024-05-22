package com.hikingplanner.hikingplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.net.URI;
import java.net.URISyntaxException;

import com.hikingplanner.hikingplanner.service.TwilioService;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class CallControlloer {
    @Autowired
    private final TwilioService twilioService;

    @GetMapping("/call")
    public ResponseEntity<String> makeCall(@RequestParam String toPhoneNumber) {
        try {
            Call call = twilioService.makePhoneCall(toPhoneNumber);
            return ResponseEntity.ok("Call initiaged with SID: "+call.getSid());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Failed to initiate call: " + e.getMessage());
    }
}
    
}
