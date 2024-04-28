package com.hikingplanner.hikingplanner.service.implement;

import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hikingplanner.hikingplanner.entity.CustomOAuth2User;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OAuth2UserServiceImplement extends DefaultOAuth2UserService{

    private final UserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        String oauthClientName = request.getClientRegistration().getClientName();

        try {
            System.out.println(new ObjectMapper().writeValueAsString(oAuth2User));
        } catch(Exception exception) {
            exception.printStackTrace();
        }

        UserEntity userEntity = null;
        String userId = null;
        String email = "email@email.com";

        if(oauthClientName.equals("kakao")) {
            userId = "kakao_" + oAuth2User.getAttributes().get("id");
            userEntity = new UserEntity(userId,email,"kakao");
        }

        userRepository.save(userEntity);
        
        return new CustomOAuth2User(userId);
    }
}
