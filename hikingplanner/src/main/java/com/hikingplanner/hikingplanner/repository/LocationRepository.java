package com.hikingplanner.hikingplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hikingplanner.hikingplanner.entity.Location;



public interface LocationRepository extends JpaRepository<Location, Long>{

    

}