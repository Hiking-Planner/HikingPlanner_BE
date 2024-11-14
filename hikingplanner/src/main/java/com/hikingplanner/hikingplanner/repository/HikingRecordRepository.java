package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.HikingRecord;
import java.util.List;


public interface HikingRecordRepository extends JpaRepository<HikingRecord, Long>{
  List<HikingRecord> findByMtid(Long mtid);
  List<HikingRecord> findByUserid(String userid); 
  
}
