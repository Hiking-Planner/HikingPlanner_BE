package com.hikingplanner.hikingplanner.entity;

//import org.springframework.boot.autoconfigure.security.SecurityProperties.User;

import com.hikingplanner.hikingplanner.dto.Request.auth.SignUpRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor //지정하는 필드에 대해서 모든 셍성자 
@Entity(name="user")
@Table(name="user")
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String email;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String address_detail;
    private String profile_image;
    private Boolean agreed_personal;
    private String name;
    private String role;
    private String type;

    public UserEntity(SignUpRequestDto dto) {
        this.userId = dto.getId();
        this.password=dto.getPassword();
        this.email = dto.getEmail();
        this.phoneNumber=dto.getPhoneNumber();
        this.type = "app";
        this.role = "ROLE_USER";
    }

    public UserEntity (String userId, String email,String type, String name, String phoneNumber, String profile_image) {
        this.userId = userId;
        this.password= "Password";
        this.phoneNumber=phoneNumber;
        this.name = name;
        this.email = email;
        this.profile_image=profile_image;
        this.type = type;
        this.role = "ROLE_USER";
    }

    
}
