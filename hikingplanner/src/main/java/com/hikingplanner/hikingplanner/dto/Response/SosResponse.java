package com.hikingplanner.hikingplanner.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class SosResponse {
  private long userid;
  private double latitude;
  private double longitude;
  private String nationalposnum;
  private String time;
}
