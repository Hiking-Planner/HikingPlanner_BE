package com.hikingplanner.hikingplanner.dto.Request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SignInRequestDto {
    @NotBlank
    @Schema(description="id",example="testuser")
    private String id;

    @NotBlank
    @Schema(description="password",example="test1234")
    private String password;
    
}
