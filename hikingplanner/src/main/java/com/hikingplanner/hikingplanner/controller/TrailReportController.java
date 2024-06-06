package com.hikingplanner.hikingplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.hikingplanner.hikingplanner.dto.Response.trail.TrailReportDto;
import com.hikingplanner.hikingplanner.entity.TrailReportEntity;
import com.hikingplanner.hikingplanner.service.TrailReportService;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "trail_report API")
public class TrailReportController {
    
    @Autowired
    private TrailReportService trailReportService;

    @PostMapping("/trailReport")
    public ResponseEntity<TrailReportEntity> createTrailReport(@RequestBody TrailReportDto trailReportDto) {
        TrailReportEntity trailReportEntity = trailReportService.createTrailReport(trailReportDto);
        return ResponseEntity.ok(trailReportEntity);
    }

    @GetMapping("/trailReport/{report_id}")
    public ResponseEntity<TrailReportEntity> getTrailReportById(@PathVariable int report_id){
        return trailReportService.getTrailReportById(report_id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
   

}