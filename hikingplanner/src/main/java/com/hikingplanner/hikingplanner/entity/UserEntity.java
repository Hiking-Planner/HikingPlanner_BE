package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor //지정하는 필드에 대해서 모든 셍성자 
@Entity(name="user")
@Table(name="user")
public class UserEntity {
    @Id
    private String userId;
    private String password;
    private String nickname;
    private String phoneNumber;
    private String address;
    private String address_detail;
    private String profile_image;
    private boolean agreed_personal;
    private String name;
    private String role;
    
}
