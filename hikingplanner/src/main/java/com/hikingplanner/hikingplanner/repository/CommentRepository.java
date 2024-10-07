package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{

    

    
}
