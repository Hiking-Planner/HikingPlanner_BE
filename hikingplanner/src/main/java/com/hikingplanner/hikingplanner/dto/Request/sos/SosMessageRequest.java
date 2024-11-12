package com.hikingplanner.hikingplanner.dto.Request.sos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
@NoArgsConstructor
public class SosMessageRequest {
  // @Schema(description = "username", example = "김하플")
  // private String username;
  // @Schema(description = "user_phone_number", example = "010-1234-1234")
  // private String phone_number;
  @Schema(description = "nationalposnum", example = "다사 6668 2483")
  private String nationalposnum;
  @Schema(description = "time", example = "2024-05-16T00:32:00")
  private LocalDateTime time;
}
