package com.hikingplanner.hikingplanner.service.implement;

import com.hikingplanner.hikingplanner.entity.UserEntity;

import lombok.Data;

@Data
public class LoginResponseDto {
    public boolean loginSuccess;
    public UserEntity userEntity;
}
