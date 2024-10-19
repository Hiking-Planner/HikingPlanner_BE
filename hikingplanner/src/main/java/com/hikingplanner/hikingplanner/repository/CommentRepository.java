package com.hikingplanner.hikingplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>{
    List<CommentEntity> findAllByBoard(BoardEntity board);
    

    
}
