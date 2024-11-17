package com.hikingplanner.hikingplanner.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikingplanner.hikingplanner.dto.Request.trail.ChangeRecordDto;
import com.hikingplanner.hikingplanner.dto.Response.trail.UpdateTrailResponse;
import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.entity.TrailEntity;
import com.hikingplanner.hikingplanner.repository.MountainRepository;
import com.hikingplanner.hikingplanner.repository.TrailRepository;
import com.hikingplanner.hikingplanner.service.UpdateTrailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name="많이 간 경로 추가(업데이트) API")
public class UpdateTrailController {
  private static final Logger logger = LoggerFactory.getLogger(UpdateTrailController.class);

  @Autowired
  UpdateTrailService updateTrailService;

  @Autowired
  TrailRepository trailRepository;

  @Autowired
  MountainRepository mountainRepository;

  @Value("${PYTHON_SERVER_URL}") 
  private String python_server_url;

  @Operation(summary="많이 이용한 경로 업데이트 api",description ="산 별로 현재 사용자들이 가장 많이 이용하는 경로를 찾아서 traildata를 업데이트한다.")
  @GetMapping("/updatetrail")
  public void createOrUpdateTrail(@RequestParam Long mountainid) throws JsonMappingException, JsonProcessingException {
      List<ChangeRecordDto> request = updateTrailService.getRecordRequest(mountainid);
      //파이썬 서버로 post 요청 보냄
      String url = python_server_url+"get_frequent_path/"+mountainid;
      RestTemplate restTemplate = new RestTemplate();
      
      ObjectMapper objectMapper = new ObjectMapper();
      String jsonRequest = objectMapper.writeValueAsString(request);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      
      HttpEntity<String> entity = new HttpEntity<>(jsonRequest,headers);
      
      //POST 요청 및 응답 받기
      
      //응답 받아서 TrailEntity에 저장
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
      logger.info("파이썬에서 온 응답 : {}",response.toString());
      
      if(response.getStatusCode() == HttpStatus.OK){
        UpdateTrailResponse trailResponse = objectMapper.readValue(response.getBody(), UpdateTrailResponse.class);
        
        //Mountain 엔티티 가져오기
        Optional<Mountain> optionalMountain = mountainRepository.findById(mountainid);
        if (optionalMountain.isPresent()) {
          
          Mountain mountain = optionalMountain.get();

          Long hptrail_id = 10000L+mountainid; //고유번호!!!
          String hptrail_name = "유저들이 많이 다닌 등산로";
          String hptrail_comment = "하플 유저들의 등산기록을 바탕으로 가장 많이 다니는 등산로를 안내해드려요!";
          //trail_data, start_point, end_point는 response에서 받아서 저장

          Optional<TrailEntity> existingTrailEntity = trailRepository.findById(hptrail_id);
          
          if (existingTrailEntity.isPresent()) {
            //기존에 <유저들이 많이 다닌 등산로> 가 존재하면 업데이트
            TrailEntity trailEntity = existingTrailEntity.get();
            //trailEntity.setMountain(mountain);
            //trailEntity.setTrail_name(trail_name);
            //trailEntity.setTrail_comment(trail_comment);
            trailEntity.setStart_point(trailResponse.getStart_point().toString());
            trailEntity.setEnd_point(trailResponse.getEnd_point().toString());
            trailEntity.setTraildata(trailResponse.getNew_path().toString());
            trailEntity.setTotal_length(trailResponse.getTotal_length());
            trailEntity.setUp_time(trailResponse.getUp_time());
            trailRepository.save(trailEntity);
            logger.info("Updating trail ID");
          }
          else{
            TrailEntity trailEntity = new TrailEntity(
              hptrail_id, // 수동으로 설정된 ID
              mountain,
              hptrail_name,
              hptrail_comment,
              trailResponse.getStart_point().toString(),
              trailResponse.getEnd_point().toString(),
              trailResponse.getNew_path().toString(),
              trailResponse.getTotal_length(),
              trailResponse.getUp_time()
          );
          trailRepository.save(trailEntity);
          logger.info("Creating trail ID");

        }
      
      }  
      else {
          // Mountain 엔티티를 찾지 못하면 로깅
          logger.error("Mountain with ID " + mountainid+ " not found.");
        } 
    }else {
    // 응답이 성공적이지 않을 경우 로깅
    logger.error("Failed to get response from Python server: " + response.getStatusCode());
    }
    
  }
}
