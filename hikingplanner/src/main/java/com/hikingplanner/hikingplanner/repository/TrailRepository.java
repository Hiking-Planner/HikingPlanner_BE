package com.hikingplanner.hikingplanner.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.TrailEntity;

public interface TrailRepository extends JpaRepository<TrailEntity,Long> {
  List<TrailEntity> findByMountain_Mtid(Long mtid);
}
