package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.Mountain;

public interface MountainRepository extends JpaRepository<Mountain,Long> {
  
}
