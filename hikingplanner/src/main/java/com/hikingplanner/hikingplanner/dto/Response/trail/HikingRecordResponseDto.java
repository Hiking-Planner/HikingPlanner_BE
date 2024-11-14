package com.hikingplanner.hikingplanner.dto.Response.trail;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class HikingRecordResponseDto {
    private String userid;
    private Long mtid;
    private String hikingTrailData;
    private long savetime;

    
    public HikingRecordResponseDto() {}

    public HikingRecordResponseDto(String userid, Long mtid, String hikingTrailData, Long savetime) {
        this.userid = userid;
        this.mtid = mtid;
        this.hikingTrailData = hikingTrailData;
        this.savetime = savetime;
    }
}
