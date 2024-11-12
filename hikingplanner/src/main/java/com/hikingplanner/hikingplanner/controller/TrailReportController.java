package com.hikingplanner.hikingplanner.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikingplanner.hikingplanner.dto.Response.trail.TrailReportDto;
import com.hikingplanner.hikingplanner.entity.TrailReportEntity;
import com.hikingplanner.hikingplanner.service.S3ImageService;
import com.hikingplanner.hikingplanner.service.TrailReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "등산 중 이상한 길 기록 API")
public class TrailReportController {
    
  @Autowired
  private TrailReportService trailReportService;
  private final S3ImageService s3ImageService; // S3 이미지 서비스 추가



  @Operation(summary = "이미지와 함께 등산 중 이상한 길 신고 API")
  @PostMapping("/trailReport")
  public ResponseEntity<?> createTrailReport(
          @RequestParam("trailReport") String trailReportJson,
          @RequestParam("image") MultipartFile image) {
      try {
          ObjectMapper objectMapper = new ObjectMapper();
          TrailReportDto trailReportDto = objectMapper.readValue(trailReportJson, TrailReportDto.class);

          if (!image.isEmpty()) {
              String imageUrl = s3ImageService.upload(image);
              trailReportDto.setTrail_image(imageUrl);
          }

          TrailReportEntity trailReportEntity = trailReportService.createTrailReport(trailReportDto);
          return ResponseEntity.ok(trailReportEntity);
      } catch (Exception e) {
          return ResponseEntity.badRequest().body("Error parsing request: " + e.getMessage());
      }
  }

  @GetMapping("/trailReport/{report_id}")
  public ResponseEntity<TrailReportEntity> getTrailReportById(@PathVariable int report_id){
      return trailReportService.getTrailReportById(report_id)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
  }

}