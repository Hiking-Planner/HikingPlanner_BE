package com.hikingplanner.hikingplanner.service.implement;

import com.hikingplanner.hikingplanner.entity.UserEntity;

import lombok.Data;

@Data
public class LoginResponseDto {
    public boolean loginSuccess;
    public UserEntity userEntity;
    private String token;

    public boolean isLoginSuccess() {
        return loginSuccess;
    }
    public void setLoginSuccess(boolean loginSuccess) {
        this.loginSuccess = loginSuccess;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
