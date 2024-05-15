package com.hikingplanner.hikingplanner.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrailInfoDto {
  private Long trail_id;
  private Long mountain_id;
  private String trail_name;
  private Double total_length;
  private Long up_time;
  private Long down_time;
  private String start_point;
  private String end_point;
  private String trail_comment;
  private String difficulty;

}
