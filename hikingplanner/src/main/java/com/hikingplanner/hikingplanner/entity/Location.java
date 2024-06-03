package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="locations")
@Getter
@Setter
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationid;

    @Column(name="userid")
    private String userid;

    @Column(name = "location_data")
    private String locationData;

    private long timestamp;



}

