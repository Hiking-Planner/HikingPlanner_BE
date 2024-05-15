package com.hikingplanner.hikingplanner.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Request.SosRequest;
import com.hikingplanner.hikingplanner.dto.Response.SosResponse;
import com.hikingplanner.hikingplanner.service.GetNationalPointNumberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@Tag(name = "긴급 신고 기능 API")
@RequiredArgsConstructor
public class SosController {

    @Autowired
    private GetNationalPointNumberService getNationalPointNumberService;

    @PostMapping("/sos")
    @Operation(summary = "긴급신고", description = "사용자 id와 현재위치(위도,경도), 시간을 POST하면 국가지점번호를 추가하여 반환한다.")
    public SosResponse Sos(@RequestBody SosRequest request) {
        String nationalposnum = getNationalPointNumberService.getNationalPointNumber(request.getLatitude(), request.getLongitude());
        return new SosResponse(request.getUserid(),request.getLatitude(),request.getLongitude(),nationalposnum,request.getDate());
    }
}