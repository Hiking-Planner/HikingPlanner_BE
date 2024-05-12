package com.hikingplanner.hikingplanner.controller;


import java.io.File;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

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
import com.hikingplanner.hikingplanner.service.AuthService;

// import io.jsonwebtoken.io.IOException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.io.IOException;


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

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn (
        @RequestBody @Valid SignInRequestDto requestBody
    ) {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }


    @GetMapping("/1")
    public ResponseEntity<Object> readJsonData() {
        StringBuilder result = new StringBuilder();

        JsonFactory factory = JsonFactory.builder().build();
        File file = new File(System.getProperty("user.dir") + "/법화산등산로.geojson");

        try (JsonParser parser = factory.createParser(file)) {
            if (parser.nextToken() != JsonToken.START_OBJECT) {
                throw new IOException("Error: Expected data to start with an Object");
            }

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                if ("features".equals(parser.getCurrentName())) {
                    parser.nextToken(); // 배열의 시작으로 이동합니다.
                    if (parser.currentToken() != JsonToken.START_ARRAY) {
                        throw new IOException("Error: Expected features to be an array");
                    }
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        if (parser.currentToken() == JsonToken.START_OBJECT) {
                            while (parser.nextToken() != JsonToken.END_OBJECT) {
                                String fieldName = parser.getCurrentName(); // 현재 필드 이름을 읽습니다.
                                parser.nextToken(); // 해당 필드의 값을 읽기 위해 다음 토큰으로 이동합니다.
                                if ("PMNTN_SN".equals(fieldName)) {
                                    String PMNTN_SN = parser.getValueAsString();
                                    result.append("user_id: ").append(PMNTN_SN).append("\n");
                                }
                                else if ("DATA_STDR".equals(fieldName)) {
                                    String dataStdr = parser.getValueAsString();
                                    result.append("datetime: ").append(dataStdr).append("\n");
                                }
                                else if ("PMNTN_DFFL".equals(fieldName)) {
                                    String PMNTN_DFFL = parser.getValueAsString();
                                    result.append("difficulty: ").append(PMNTN_DFFL).append("\n");
                                }
                                else if ("MNTN_CODE".equals(fieldName)) {
                                    String MNTN_CODE = parser.getValueAsString();
                                    result.append("mountain_id: ").append(MNTN_CODE).append("\n");
                                }
                                else if ("coordinates".equals(fieldName)) {
                                    // coordinates 필드는 배열이므로, 배열 내용을 읽어서 문자열로 처리합니다.
                                    if (parser.currentToken() == JsonToken.START_ARRAY) {
                                        result.append("lng-lat: [");
                                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                                            if (parser.currentToken() == JsonToken.START_ARRAY) {
                                                result.append("[");
                                                while (parser.nextToken() != JsonToken.END_ARRAY) {
                                                    if (parser.currentToken() == JsonToken.VALUE_NUMBER_FLOAT) {
                                                        double coordinate = parser.getDoubleValue();
                                                        result.append(String.format("%.5f", coordinate)).append(", ");
                                                    }
                                                }
                                                // 마지막 콤마 제거와 배열 닫기
                                                result.setLength(result.length() - 2); // 마지막 콤마와 공백 제거
                                                result.append("], ");
                                            }
                                        }
                                        // 마지막 콤마 제거와 배열 닫기
                                        result.setLength(result.length() - 2); // 마지막 콤마와 공백 제거
                                        result.append("]");
                                    }
                                    result.append("\n");
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error reading JSON file: " + e.getMessage());
        }

        return ResponseEntity.ok(result.toString());
    }
    }

