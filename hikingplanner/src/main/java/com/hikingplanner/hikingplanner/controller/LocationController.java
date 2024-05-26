package com.hikingplanner.hikingplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.hikingplanner.hikingplanner.entity.Location;
import com.hikingplanner.hikingplanner.repository.LocationRepository;

@RestController
@RequestMapping("/api/v1/auth")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping("/locations")
    public void saveLocation(@RequestParam String userid, @RequestBody List<LocationData> locations) {
        String locationData = new Gson().toJson(locations);
        Location location = new Location();
        location.setUserid(userid);
        location.setLocationData(locationData);
        location.setTimestamp(System.currentTimeMillis());
        locationRepository.save(location);
    }

    public static class LocationData {
        private double latitude;
        private double longitude;
        private long timestamp;

        // Getters and setters
        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }
}