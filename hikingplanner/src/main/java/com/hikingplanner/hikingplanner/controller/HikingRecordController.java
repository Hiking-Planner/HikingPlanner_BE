package com.hikingplanner.hikingplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hikingplanner.hikingplanner.dto.Request.trail.HikingRecordRequest;
import com.hikingplanner.hikingplanner.entity.HikingRecord;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.HikingRecordRepository;
import com.hikingplanner.hikingplanner.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="등산기록 DB 저장API")
public class HikingRecordController {

    @Autowired
    private HikingRecordRepository hikingRecordRepository;

    @Autowired
    private UserRepository userRepository;
    

    @Operation(summary = "사용자 등산기록", description = "사용자들의 등산기록을 DB에 저장한다.")
    @PostMapping("/hiking_record")
    public void saveLocation(@RequestBody HikingRecordRequest recordedData) {

        // JWT로부터 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // JWT에 저장된 사용자 이메일 혹은 userId

        // 사용자의 정보를 가져옴
        UserEntity userEntity = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("이 이메일을 가진 유저를 찾을수가 없습니다: " + email));

        HikingRecord hikingRecord = new HikingRecord();
        String trail_data = new Gson().toJson(recordedData.getHikingTrailData());

        hikingRecord.setUserid(userEntity.getUserId());
        hikingRecord.setMtid(recordedData.getMtid());
        hikingRecord.setHikingTrailData(trail_data);
        hikingRecord.setSavetime(System.currentTimeMillis());
        hikingRecordRepository.save(hikingRecord);

    }
}