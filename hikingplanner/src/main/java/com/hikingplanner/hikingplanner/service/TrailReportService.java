package com.hikingplanner.hikingplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import com.hikingplanner.hikingplanner.dto.Response.trail.TrailReportDto;
import com.hikingplanner.hikingplanner.entity.TrailReportEntity;
import com.hikingplanner.hikingplanner.repository.TrailReportRepository;

@Service
public class TrailReportService {
    
    @Autowired
    private TrailReportRepository trailReportRepository;

    public TrailReportEntity createTrailReport(TrailReportDto trailReportDto) {
        TrailReportEntity trailReportEntity = new TrailReportEntity();
        trailReportEntity.setReport(trailReportDto.getReport());
        trailReportEntity.setLatitude(trailReportDto.getLatitude());
        trailReportEntity.setLongitude(trailReportDto.getLongitude());
        trailReportEntity.setTrail_image(trailReportDto.getTrail_image());
        trailReportEntity.setTimestamp(trailReportDto.getTimestamp());
        return trailReportRepository.save(trailReportEntity);
        
    }


    public Optional<TrailReportEntity> getTrailReportById(int id) {
        return trailReportRepository.findById(id);
    }

     // 전체 신고 조회
     public List<TrailReportEntity> getAllTrailReports() {
        return trailReportRepository.findAll();
    }

}