package com.hikingplanner.hikingplanner.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikingplanner.hikingplanner.entity.UserEntity;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

    boolean existsByUserId(String userId);
    
    // Optional로 감싸서 반환
    Optional<UserEntity> findByUserId(String userId);
    
    Optional<UserEntity> findByEmail(String email);
}

