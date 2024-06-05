package com.hikingplanner.hikingplanner.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Response.mountain.MountainDto;
import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.repository.MountainRepository;
import com.hikingplanner.hikingplanner.service.MtInfoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;




@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "산 정보 조회 API")
@RequiredArgsConstructor
public class MountainInfoController {
  @Autowired
  private MountainRepository mountainRepository;

  @Autowired
  private MtInfoService mtInfoService;
  
  @Operation(summary = "산 전체보기 API")
  @GetMapping("/mountains")
  public ResponseEntity<List<MountainDto>> getAllMountains() {
      List<Mountain> mountains = mountainRepository.findAll();
      List<MountainDto> mountainDTOList = new ArrayList<>();
      for (Mountain mountain : mountains) {
          MountainDto mountainDTO = mtInfoService.getMountainDto(mountain);
          mountainDTOList.add(mountainDTO);
      }
      return ResponseEntity.ok(mountainDTOList);
  }

@Operation(summary = "산 정보 API", description = "mountain_id를 통해 특정 산 정보 가져오기")
@GetMapping("/mountain/{id}")
public ResponseEntity<MountainDto> getMountain(@PathVariable Long id){
  Mountain mountain = mountainRepository.findById(id).orElse(null);
  MountainDto mountainDTO = mtInfoService.getMountainDto(mountain);

  return ResponseEntity.ok(mountainDTO);

  }
}
