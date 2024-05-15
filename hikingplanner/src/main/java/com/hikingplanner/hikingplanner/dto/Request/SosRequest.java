package com.hikingplanner.hikingplanner.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class SosRequest {
  private long userid;
  private double latitude;
  private double longitude;
  private String date;

}
