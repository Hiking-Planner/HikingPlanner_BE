package com.hikingplanner.hikingplanner.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikingplanner.hikingplanner.dto.Request.user.UserUpdateRequestDto;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final S3ImageService s3ImageService; 

    public UserService(UserRepository userRepository, S3ImageService s3ImageService) {
        this.userRepository = userRepository;
        this.s3ImageService = s3ImageService;
    }

    // userId로 유저를 찾는 메서드
    public UserEntity findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    // 이메일로 유저를 찾는 메서드
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Transactional
    public UserEntity updateUser(String email, UserUpdateRequestDto dto) {

        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // 사용자 정보 업데이트
        if (dto.getNickname() != null) {
            userEntity.setNickname(dto.getNickname());
        }
        if (dto.getPhoneNumber() != null) {
            userEntity.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getBirth() != null) {
            userEntity.setBirth(dto.getBirth());
        }
        if (dto.getIntroduce() != null) {
            userEntity.setIntroduce(dto.getIntroduce());
        }
        if (dto.getGender() != null) {
            userEntity.setGender(dto.getGender());
        }

        
        return userRepository.save(userEntity);
    }


     // 프로필 이미지 업데이트
    public UserEntity updateProfileImage(String email, String imageUrl) {
        UserEntity userEntity = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // 기존 프로필 이미지가 있으면 삭제
        if (userEntity.getProfile_image() != null) {
            s3ImageService.deleteImageFromS3(userEntity.getProfile_image());
        }

        
        userEntity.setProfile_image(imageUrl);
        return userRepository.save(userEntity);
    }
}
