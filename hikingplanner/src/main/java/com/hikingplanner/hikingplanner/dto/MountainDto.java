package com.hikingplanner.hikingplanner.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MountainDto {
  private Long mountain_id;
  private String mountain_name;
  private String location;
  private Double elevation;
  private List<MountainImgDto> image_info;
  private String mountain_comment;
  private Double[] Centerlatlon;

}