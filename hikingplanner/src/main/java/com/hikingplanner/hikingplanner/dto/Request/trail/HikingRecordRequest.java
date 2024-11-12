package com.hikingplanner.hikingplanner.dto.Request.trail;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HikingRecordRequest {
  @Schema(description = "recordid", example = "unnessasary")
  private Long recordid;
  // @Schema(description = "userid", example = "1")
  // private String userid;
  @Schema(description = "mountain_id", example = "1")
  private Long mtid;
  @Schema(description = "LocationData", example = "[\r\n" + //
        "        {\"latitude\": 37.7749, \"longitude\": -122.4194, \"timestamp\": 1657890123000},\r\n" + //
        "        {\"latitude\": 37.7750, \"longitude\": -122.4195, \"timestamp\": 1657890183000},\r\n" + //
        "        {\"latitude\": 37.7753, \"longitude\": -122.4197, \"timestamp\": 1657890184000},\r\n" + //
        "        {\"latitude\": 37.7751, \"longitude\": -122.4196, \"timestamp\": 1657890243000}\r\n" + //
        "    ]")
  private List<LocationData> hikingTrailData;
  @Schema(description = "savetime", example = "1657890243000")
  private Long savetime;

  @Getter
  @Setter
  public static class LocationData {
      private Double latitude;
      private Double longitude;
      private Long timestamp;
  }
}

