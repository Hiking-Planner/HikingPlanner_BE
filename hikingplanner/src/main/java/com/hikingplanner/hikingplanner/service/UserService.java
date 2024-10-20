package com.hikingplanner.hikingplanner.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikingplanner.hikingplanner.dto.Request.user.UserUpdateRequestDto;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
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

        // 변경된 사용자 정보를 저장
        return userRepository.save(userEntity);
    }
}
