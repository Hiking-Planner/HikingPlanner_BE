package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Long>{

    

}
