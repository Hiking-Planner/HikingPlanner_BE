package com.hikingplanner.hikingplanner.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Request.user.UserUpdateRequestDto;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;




@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "마이페이지 API")
public class UserController {
    private final UserService userService;

    //이메일로 조회
    @Operation(summary = "이메일로 유저정보 조회 API")
    @GetMapping("/user/email/{email}")
    public ResponseEntity<UserEntity> getUserByEmail(@PathVariable String email) {
        UserEntity userEntity = userService.findByEmail(email);
        return ResponseEntity.ok(userEntity);
    }

    // 유저 ID로 유저 정보 조회
    @Operation(summary = "id로 유저정보 조회 API")
    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable String userId) {
        UserEntity usereEntity = userService.findByUserId(userId);
        return ResponseEntity.ok(usereEntity);
    }

    // 현재 로그인된 사용자의 정보 조회
    @Operation(summary = "현재 로그인된 사용자의 정보 조회 API")
    @GetMapping("/user-info")
    public ResponseEntity<UserEntity> getUserInfo() {
        // 인증된 사용자 정보를 JWT 토큰에서 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // JWT에 저장된 사용자 이메일 (또는 ID)
        
        // 이메일로 사용자 정보 조회
        UserEntity user = userService.findByEmail(email);
        return ResponseEntity.ok(user);
    }

    @Operation(summary = "현재 로그인한 사용자의 프로필 수정 API")
    @PutMapping("/profile")
    public ResponseEntity<UserEntity> updateUserProfile(@RequestBody UserUpdateRequestDto dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        //사용자 정보를 업데이트
        UserEntity updateUser = userService.updateUser(email, dto);
        return ResponseEntity.ok(updateUser);
    }

}
