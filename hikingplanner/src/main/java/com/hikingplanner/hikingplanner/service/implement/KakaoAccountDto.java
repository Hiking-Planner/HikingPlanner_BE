package com.hikingplanner.hikingplanner.service.implement;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoAccountDto {
    private Long id;
    private KakaoAccount kakao_account;

    public static class KakaoAccount {
        private String email;
        private String phone_number;
        private Profile profile;

        @JsonProperty("email")
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        @JsonProperty("phone_number")
        public String getPhone_number(){
            return phone_number;
        }
        public void setPhoneNumber(String phone_number){
            this.phone_number = phone_number;
        }

        @JsonProperty("profile")
        public Profile getProfile() {
            return profile;
        }

        public void setProfile(Profile profile) {
            this.profile = profile;
        }

        

    }

    public static class Profile {
        private String nickname;
        private String profile_image;
        private String phoneNumber;

        @JsonProperty("phoneNumber")
        public String getPhoneNumber() {
            return phoneNumber;
        }
        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        @JsonProperty("nickname")
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }


        @JsonProperty("profile_image_url")
        public String getProfile_image_url() {
            return profile_image;
        }

        public void setProfile_image_url(String profile_image) {
            this.profile_image = profile_image;
        }

    }
}