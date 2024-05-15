package com.hikingplanner.hikingplanner.dto.Request;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Getter
@Setter
public class SosRequest {
  @Schema(description = "userid", example = "1")
  private long userid;
  @Schema(description = "위도", example = "37.321908")
  private double latitude;
  @Schema(description = "경도", example = "127.124")
  private double longitude;
  @Schema(description = "date", example = "2024-05-16T00:32:00Z")
  private LocalDateTime date;

}
