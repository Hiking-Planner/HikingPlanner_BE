package com.hikingplanner.hikingplanner.dto.Response.trail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class UpdateTrailResponse {
  private String update_time;
  private Long mountain_id;
  private List<List<Double>> new_path;
  private List<Double> start_point;
  private List<Double> end_point;

}
