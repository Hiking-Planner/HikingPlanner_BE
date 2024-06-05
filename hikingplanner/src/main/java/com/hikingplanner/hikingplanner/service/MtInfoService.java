package com.hikingplanner.hikingplanner.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hikingplanner.hikingplanner.dto.Response.mountain.MountainDto;
import com.hikingplanner.hikingplanner.dto.Response.mountain.MountainImgDto;
import com.hikingplanner.hikingplanner.dto.Response.trail.TrailInfoDto;
import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.entity.MountainImgEntity;
import com.hikingplanner.hikingplanner.entity.TrailEntity;

@Service
public class MtInfoService {
  
    public MountainDto getMountainDto(Mountain mountain){
      MountainDto mountainDTO = new MountainDto();
          mountainDTO.setMountain_id(mountain.getMtid());
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
            MountainImgDto imageInfoDTO = geMountainImgDto(image);
            imageInfoDTOList.add(imageInfoDTO);
          }
          mountainDTO.setImage_info(imageInfoDTOList);
          return mountainDTO;
    }

    public MountainImgDto geMountainImgDto(MountainImgEntity image){
      MountainImgDto imageInfoDTO = new MountainImgDto();
      imageInfoDTO.setMtimg_id(image.getMtimg_id());
      imageInfoDTO.setImg_url(image.getImg_url());
      imageInfoDTO.setImg_name(image.getImg_name());
      
      return imageInfoDTO;
    }
    public TrailInfoDto getTrailInfoDto(TrailEntity trailEntity){
    TrailInfoDto trailInfoDto = new TrailInfoDto();
    trailInfoDto.setTrail_id(trailEntity.getTrail_id());
    trailInfoDto.setMountain_id(trailEntity.getMountainId());
    trailInfoDto.setTrail_name(trailEntity.getTrail_name());
    trailInfoDto.setTotal_length(trailEntity.getTotal_length());
    trailInfoDto.setUp_time(trailEntity.getUp_time());
    trailInfoDto.setDown_time(trailEntity.getDown_time());
    trailInfoDto.setStart_point(trailEntity.getStart_point());
    trailInfoDto.setEnd_point(trailEntity.getEnd_point());
    trailInfoDto.setTrail_comment(trailEntity.getTrail_comment());
    trailInfoDto.setDifficulty(trailEntity.getDifficulty());

    return trailInfoDto;
  }
}
