package com.hikingplanner.hikingplanner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;

import org.springframework.web.multipart.MultipartFile;
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name="trail_report")
@Table(name="trail_report")
public class TrailReportEntity {
    @Id
    private int report_id;

    private String report;

    private String latitude;

    private String longitude;

    private String trail_image;

    @Column(name="TIMESTAMP")
    private String timestamp;



}
