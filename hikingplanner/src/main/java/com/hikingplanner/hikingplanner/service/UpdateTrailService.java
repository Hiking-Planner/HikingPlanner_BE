package com.hikingplanner.hikingplanner.service;

import java.util.List;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikingplanner.hikingplanner.dto.Request.trail.ChangeRecordDto;
import com.hikingplanner.hikingplanner.dto.Request.trail.ChangeRecordDto.LocationData;
import com.hikingplanner.hikingplanner.entity.HikingRecord;
import com.hikingplanner.hikingplanner.repository.HikingRecordRepository;


@Service
public class UpdateTrailService {

 // private static final Logger logger = LoggerFactory.getLogger(UpdateTrailService.class);

  @Autowired
  private HikingRecordRepository hikingRecordRepository;

  @Autowired
  private ObjectMapper objectMapper;


  public List<ChangeRecordDto> getRecordRequest(long mtid) throws JsonMappingException, JsonProcessingException{
    //1. HikingRecord 테이블에서 mountainid로 데이터를 추출한다(userid, hikingTrailData).
    List<HikingRecord> hikingRecords = hikingRecordRepository.findByMtid(mtid);
    //2. ChangeRecordDto로 userid와 경로데이터를 가져온다. 이때 경로데이터를 string에서 latitude, longitude, timestamp를 가진 데이터로 형변환한다.
    List<ChangeRecordDto> recordDtoList = new ArrayList<>();
    for(HikingRecord hikingRecord : hikingRecords){
      ChangeRecordDto changeRecordDto = getRecord(hikingRecord);
      recordDtoList.add(changeRecordDto);
    }

    //3. List<ChangRecordDto> 형식으로 반환한다.
    return recordDtoList;
    

  }

  public ChangeRecordDto getRecord(HikingRecord hikingRecord) throws JsonMappingException, JsonProcessingException{
    ChangeRecordDto changeRecordDto = new ChangeRecordDto();
    changeRecordDto.setUserid(hikingRecord.getUserid());
    
    // locationData를 string -> json으로 형변환
    String locationstring = hikingRecord.getHikingTrailData();
    List<LocationData> locationDataList = objectMapper.readValue(locationstring, new TypeReference<List<LocationData>>(){});
    
    changeRecordDto.setLocations(locationDataList);

    return changeRecordDto;
  }
}
