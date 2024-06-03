package com.hikingplanner.hikingplanner.dto.Response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hikingplanner.hikingplanner.common.ResponseCode;
import com.hikingplanner.hikingplanner.common.ResponseMessage;
import com.hikingplanner.hikingplanner.dto.Response.ResponseDto;

import lombok.Getter;

@Getter
public class SignInResponseDto extends ResponseDto{

    private boolean success;
    private String token;
    private int expirationTime;

    private SignInResponseDto(String token) {
        super();
        this.token = token;
        this.expirationTime=3600;
        this.success = true;
    }
    public static ResponseEntity<SignInResponseDto> success(String token) {
        SignInResponseDto responseBody = new SignInResponseDto(token);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> signInFail () {
        ResponseDto responseBody = new ResponseDto(ResponseCode.SIGN_IN_FAIL,ResponseMessage.SIGN_IN_FAIL);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

}
