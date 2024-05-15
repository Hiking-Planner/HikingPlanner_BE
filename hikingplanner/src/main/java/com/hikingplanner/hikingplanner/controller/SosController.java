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

@RestController
@Slf4j
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class SosController {

    @Autowired
    private GetNationalPointNumberService getNationalPointNumberService;

    @PostMapping("/sos")
    public SosResponse Sos(@RequestBody SosRequest request) {
        log.info(request.toString());
        String nationalposnum = getNationalPointNumberService.getNationalPointNumber(request.getLatitude(), request.getLongitude());
        return new SosResponse(request.getUserid(),request.getLatitude(),request.getLongitude(),nationalposnum,request.getDate());
    }
}