package com.hikingplanner.hikingplanner.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MountainImgDto {
  private Long mtimg_id;
  private String img_url;
  private String img_name;

}
