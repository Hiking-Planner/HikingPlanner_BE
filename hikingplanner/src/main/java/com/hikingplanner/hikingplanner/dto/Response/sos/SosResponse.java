package com.hikingplanner.hikingplanner.dto.Response.sos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
public class SosResponse {
  private String username;
  private String phone_number;
  private double latitude;
  private double longitude;
  private String nationalposnum;
  private LocalDateTime time;
}
