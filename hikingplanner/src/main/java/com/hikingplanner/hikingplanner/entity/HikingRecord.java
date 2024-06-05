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
@Table(name="hiking_record")
@Getter
@Setter
public class HikingRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    private Long recordid;

    @Column(name="userid")
    private String userid;

    @Column(name="mountain_id")
    private Long mtid;

    @Column(name = "hiking_trail_data")
    private String hikingTrailData;

    private Long savetime;

}

