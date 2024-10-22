package com.hikingplanner.hikingplanner.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{

    
    List<BoardEntity> findByUserUserId(String userId); //유저레포지토리에 같은 함수가 있으니 오류가 나서 user하나 더씀
    

}
