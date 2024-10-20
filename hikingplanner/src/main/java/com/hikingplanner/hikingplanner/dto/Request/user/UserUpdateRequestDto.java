package com.hikingplanner.hikingplanner.dto.Request.user;

import com.hikingplanner.hikingplanner.entity.UserEntity.Gender;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequestDto {
    private String nickname;
    private String phoneNumber;
    private String birth;
    private String introduce;
    private Gender gender;
}
