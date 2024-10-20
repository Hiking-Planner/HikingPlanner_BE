/* 
package com.hikingplanner.hikingplanner.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.repository.MountainRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {
    
    @Autowired
    private MountainRepository mountainRepository;

    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    private final RestTemplate restTemplate;
    public String mountaineeringClothes(@RequestParam Long mtid) {
        Mountain mountain = mountainRepository.findById(mtid).orElse(null);

        double lat = mountain.getLatitude();
        double lon = mountain.getLongitude();
        

        String forecastUrl = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s",lat, lon, openWeatherApiKey);

    }
}
*/