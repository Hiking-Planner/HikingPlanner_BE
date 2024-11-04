// package com.hikingplanner.hikingplanner.service.implement;

// import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
// import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
// import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
// import org.springframework.security.oauth2.core.user.OAuth2User;
// import org.springframework.stereotype.Service;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.hikingplanner.hikingplanner.entity.CustomOAuth2User;
// import com.hikingplanner.hikingplanner.entity.UserEntity;
// import com.hikingplanner.hikingplanner.repository.UserRepository;
// import lombok.RequiredArgsConstructor;
// import java.util.Map;

// @Service
// @RequiredArgsConstructor
// public class OAuth2UserServiceImplement extends DefaultOAuth2UserService {
//     private final UserRepository userRepository;

//     @Override
//     public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
//         OAuth2User oAuth2User = super.loadUser(request);
//         String oauthClientName = request.getClientRegistration().getClientName();

//         try {
//             System.out.println(new ObjectMapper().writeValueAsString(oAuth2User));
//         } catch(Exception exception) {
//             exception.printStackTrace();
//         }

//         UserEntity userEntity = null;
//         String userId = null;
//         String email = "email@email.com";
//         String name = null;
//         String phoneNumber = null;
//         String profile_image = null;

//         if (oauthClientName.equals("kakao")) {
//             @SuppressWarnings("unchecked")
//             Map<String, Object> kakaoAccount = (Map<String, Object>) oAuth2User.getAttributes().get("kakao_account");
//             @SuppressWarnings("unchecked")
//             Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
            
//             userId = "kakao_" + oAuth2User.getAttributes().get("id");
//             email = (String) kakaoAccount.get("email");
//             name = (String) profile.get("nickname");
//             phoneNumber = (String) kakaoAccount.get("phone_number");
//             profile_image = (String) profile.get("profile_image_url");

//             if (email == null) { // 이메일이 null일 경우 기본값 설정
//                 email = "no-email@example.com";
//             }
            
//             userEntity = new UserEntity(userId, email, "kakao", name, phoneNumber, profile_image);
//         }

//         userRepository.save(userEntity);
        
//         return new CustomOAuth2User(userId);
//     }
// }