package com.hikingplanner.hikingplanner.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Response.trail.TrailDataDto;
import com.hikingplanner.hikingplanner.dto.Response.trail.TrailInfoDto;
import com.hikingplanner.hikingplanner.entity.TrailEntity;
import com.hikingplanner.hikingplanner.repository.TrailRepository;
import com.hikingplanner.hikingplanner.service.MtInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="등산로 정보 API")
@RequiredArgsConstructor
public class TrailController {
  @Autowired
  private TrailRepository trailRepository;

  @Autowired
  private MtInfoService mtInfoService;

  @Operation(summary="등산로 전체 가져오기 API",description ="mountain_id 에 따른 등산로 모두 가져오기")
  @GetMapping("/getAllTrails/{mtid}")
  public ResponseEntity<List<TrailInfoDto>> getallTrailInfo(@PathVariable Long mtid) {
    List<TrailEntity> trails = trailRepository.findByMountain_Mtid(mtid);
    List<TrailInfoDto> trailInfoDtoList = new ArrayList<>();
    for (TrailEntity trailEntity : trails){
      TrailInfoDto trailInfoDto = mtInfoService.getTrailInfoDto(trailEntity);
      trailInfoDtoList.add(trailInfoDto);
    }
    return ResponseEntity.ok(trailInfoDtoList);
  }
  
  @Operation(summary = "등산로 정보 API",description ="등산로 ID를 통해 등산로 정보 가져오기")
  @GetMapping("getTrail/{trail_id}")
  public ResponseEntity <TrailInfoDto> getTrailInfo(@PathVariable Long trail_id){
    TrailEntity trailEntity = trailRepository.findById(trail_id).orElse(null);
    TrailInfoDto trailInfoDto = mtInfoService.getTrailInfoDto(trailEntity);

    return ResponseEntity.ok(trailInfoDto);
  } 

  @Operation(summary = "등산 경로 정보 API",description ="지도에 표시할 등산로 경로 (위도경도) 정보 GET")
  @GetMapping("getTrailData/{trail_id}")
  public ResponseEntity <TrailDataDto> getTrailData(@PathVariable Long trail_id){
    TrailEntity trailEntity = trailRepository.findById(trail_id).orElse(null);
    TrailDataDto trailDataDto = new TrailDataDto();
    trailDataDto.setTrailData(trailEntity.getTraildata());

    return ResponseEntity.ok(trailDataDto);
  }

}
