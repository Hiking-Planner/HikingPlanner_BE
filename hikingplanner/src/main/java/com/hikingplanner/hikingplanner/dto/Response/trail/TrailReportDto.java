package com.hikingplanner.hikingplanner.dto.Response.trail;

import com.hikingplanner.hikingplanner.entity.TrailReportEntity;
import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;

public class TrailReportDto {
    private TrailReportEntity trailReportEntity;

    private String report;
    private String latitude;
    private String longitude;
    private String trail_image;
    private Instant timestamp;
    public String getReport() {
        return report;
        
    }
    public String getLatitude() {
        return latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public String getTrail_image() {
        return trail_image;
    }
    public Instant getTimestamp() {
        return timestamp;
    }
    
}
