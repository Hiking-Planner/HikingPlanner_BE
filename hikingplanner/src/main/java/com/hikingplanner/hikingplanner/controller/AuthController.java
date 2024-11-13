package com.hikingplanner.hikingplanner.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import com.hikingplanner.hikingplanner.dto.Request.auth.CheckCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.EmailCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.IdCheckRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.SignInRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.SignUpRequestDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.CheckCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.EmailCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.IdCheckResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.SignInResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.SignUpResponseDto;
import com.hikingplanner.hikingplanner.provider.JwtProvider;
import com.hikingplanner.hikingplanner.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Login API")
public class AuthController {
    private final AuthService authService;
    private final JwtProvider jwtProvider;
    


    @PostMapping("/id-check")
    public ResponseEntity<? super IdCheckResponseDto> idCheck (
        @RequestBody @Valid IdCheckRequestDto requestBody
    ) {
        ResponseEntity<? super IdCheckResponseDto> response = authService.idCheck(requestBody);
        return response;
    }

    @Operation(summary = "이메일 인증 API")
    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification (
        @RequestBody @Valid EmailCertificationRequestDto requestBody

    ){
        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertification(requestBody);
        return response;
    }

    @Operation(summary = "인증번호 체크 API")
    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification (
        @RequestBody @Valid CheckCertificationRequestDto requestBody
    ) {
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }

    @Operation(summary = "회원가입 API")
    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp (
        @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @Operation(summary = "로그인 API")
    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn (
        @RequestBody @Valid SignInRequestDto requestBody
    ) {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }

    @Operation(summary = "로그아웃 API")
    @PostMapping("/sign-out")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
    String jwt = token.replace("Bearer ", "");
    jwtProvider.invalidateToken(jwt);
    return ResponseEntity.ok().build();
}

    
}

