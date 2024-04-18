package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hikingplanner.hikingplanner.entity.UserEntity;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{
    UserEntity findByUserId(String userId);
}
