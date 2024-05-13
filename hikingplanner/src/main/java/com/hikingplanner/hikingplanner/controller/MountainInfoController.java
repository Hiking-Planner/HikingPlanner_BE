package com.hikingplanner.hikingplanner.controller;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.MountainDto;
import com.hikingplanner.hikingplanner.dto.MountainImgDto;
import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.entity.MountainImgEntity;
import com.hikingplanner.hikingplanner.repository.MountainRepository;

import lombok.RequiredArgsConstructor;



@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class MountainInfoController {
  @Autowired
  private MountainRepository mountainRepository;

  @GetMapping("/test2")
  public String test(){
    return "Hello World!";
  }
  @GetMapping("/mountains")
  public ResponseEntity<List<MountainDto>> getAllMountains() {
      List<Mountain> mountains = mountainRepository.findAll();
      List<MountainDto> mountainDTOList = new ArrayList<>();
      for (Mountain mountain : mountains) {
          MountainDto mountainDTO = new MountainDto();
          mountainDTO.setMountain_id(mountain.getMountain_id());
          mountainDTO.setMountain_name(mountain.getMountain_name());
          mountainDTO.setLocation(mountain.getLocation());
          mountainDTO.setElevation(mountain.getElevation());
          mountainDTO.setMountain_comment(mountain.getMountain_comment());

          Double[] centerlatlon = new Double[2];
          centerlatlon[0] = mountain.getLatitude();
          centerlatlon[1] = mountain.getLongitude();
          mountainDTO.setCenterlatlon(centerlatlon);

          List<MountainImgDto> imageInfoDTOList = new ArrayList<>();
          for (MountainImgEntity image : mountain.getImages()) {
              MountainImgDto imageInfoDTO = new MountainImgDto();
              imageInfoDTO.setMtimg_id(image.getMtimg_id());
              imageInfoDTO.setImg_url(image.getImg_url());
              imageInfoDTO.setImg_name(image.getImg_name());
              imageInfoDTOList.add(imageInfoDTO);
          }
          mountainDTO.setImage_info(imageInfoDTOList);

          mountainDTOList.add(mountainDTO);
      }
      return ResponseEntity.ok(mountainDTOList);
  }

@GetMapping("/mountain/{id}")
public ResponseEntity<MountainDto> getMountain(@PathVariable Long id){
  Mountain mountain = mountainRepository.findById(id).orElse(null);
      MountainDto mountainDTO = new MountainDto();
      mountainDTO.setMountain_id(mountain.getMountain_id());
      mountainDTO.setMountain_name(mountain.getMountain_name());
      mountainDTO.setLocation(mountain.getLocation());
      mountainDTO.setElevation(mountain.getElevation());
      mountainDTO.setMountain_comment(mountain.getMountain_comment());

      Double[] centerlatlon = new Double[2];
      centerlatlon[0] = mountain.getLatitude();
      centerlatlon[1] = mountain.getLongitude();
      mountainDTO.setCenterlatlon(centerlatlon);

      List<MountainImgDto> imageInfoDTOList = new ArrayList<>();
      for (MountainImgEntity image : mountain.getImages()) {
          MountainImgDto imageInfoDTO = new MountainImgDto();
          imageInfoDTO.setMtimg_id(image.getMtimg_id());
          imageInfoDTO.setImg_url(image.getImg_url());
          imageInfoDTO.setImg_name(image.getImg_name());
          imageInfoDTOList.add(imageInfoDTO);
      }
      mountainDTO.setImage_info(imageInfoDTOList);
  
  return ResponseEntity.ok(mountainDTO);

  }
}
