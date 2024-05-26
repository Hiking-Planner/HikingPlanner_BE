package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="locations")
public class Location {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationid;

    @Column(name="userid")
    private String userid;

    @Column(name = "location_data")
    private String locationData;

    private long timestamp;

    public void setUserid(String userid) {
        this.userid=userid;
    }

    public void setLocationData(String locationData) {
        this.locationData=locationData;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}

