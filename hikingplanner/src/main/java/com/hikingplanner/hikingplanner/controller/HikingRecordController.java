package com.hikingplanner.hikingplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.hikingplanner.hikingplanner.dto.Request.trail.HikingRecordRequest;
import com.hikingplanner.hikingplanner.entity.HikingRecord;
import com.hikingplanner.hikingplanner.repository.HikingRecordRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="등산기록 DB 저장API")
public class HikingRecordController {

    @Autowired
    private HikingRecordRepository hikingRecordRepository;

    @Operation(summary = "사용자 등산기록", description = "사용자들의 등산기록을 DB에 저장한다.")
    @PostMapping("/hiking_record")
    public void saveLocation(@RequestBody HikingRecordRequest recordedData) {
        HikingRecord hikingRecord = new HikingRecord();
        String trail_data = new Gson().toJson(recordedData.getHikingTrailData());
        hikingRecord.setUserid(recordedData.getUserid());
        hikingRecord.setMtid(recordedData.getMtid());
        hikingRecord.setHikingTrailData(trail_data);
        hikingRecord.setSavetime(System.currentTimeMillis());
        hikingRecordRepository.save(hikingRecord);
    }
}