package com.hikingplanner.hikingplanner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Request.auth.CheckCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.EmailCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.IdCheckRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.SignUpRequestDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.CheckCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.EmailCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.IdCheckResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.SignUpResponseDto;
import com.hikingplanner.hikingplanner.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck (
        @RequestBody @Valid IdCheckRequestDto requestBody
    ) {
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification (
        @RequestBody @Valid EmailCertificationRequestDto requestBody

    ){
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification (
        @RequestBody @Valid CheckCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp (
        @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }
}


