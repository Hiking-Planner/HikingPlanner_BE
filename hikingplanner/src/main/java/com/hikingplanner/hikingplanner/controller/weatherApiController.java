package com.hikingplanner.hikingplanner.controller;

import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import com.hikingplanner.hikingplanner.entity.Mountain;
import com.hikingplanner.hikingplanner.repository.MountainRepository;
import com.hikingplanner.hikingplanner.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;
import java.util.Map;
import java.io.StringReader;
import org.xml.sax.InputSource;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name="날씨 정보 API")
public class weatherApiController {

    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private WeatherService weatherService;

    @Value("${open.weather.api.key}")
    private String openWeatherApiKey;

    @Value("${open.suninfo.api.key}")
    private String sunInfoApiKey;

    private final RestTemplate restTemplate;

    @GetMapping("/api/weather/{mtid}")
    @Operation(summary="날씨 정보 api",description ="산 ID를 요청파라미터에 담으면 OpenWeatherMap API를 통해 날씨 정보를 불러온다.")
    public ResponseEntity<String> getWeather(@PathVariable Long mtid) {
        Mountain mountain = mountainRepository.findById(mtid).orElse(null);
        if (mountain == null) {
            return ResponseEntity.status(HttpStatus.SC_NOT_FOUND).body("해당 산 정보를 찾을 수 없습니다.");
        }

        double lat = mountain.getLatitude();
        double lon = mountain.getLongitude();
        
        try {
            // 5일 날씨 예보 URL
            String forecastUrl = String.format("https://api.openweathermap.org/data/2.5/forecast?lat=%s&lon=%s&appid=%s&units=metric",lat, lon, openWeatherApiKey);

        
            String forecastResponse = restTemplate.getForObject(forecastUrl, String.class);
            return ResponseEntity.ok(forecastResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body("날씨 정보를 가져오는 데 실패했습니다.");
            }
        }

    @GetMapping("/api/suninfo/{mtid}")
    @Operation(summary = " 출몰정보 api", description = "산 ID를 요청파라미터에 담으면 출몰정보 API를 통해 일출일몰 정보를 불러온다.")
    public ResponseEntity<Map<String, String>> getSunInfo(@Parameter(name="dateinfo", description = "날짜(yyyyMMdd)", example="20241020") @RequestParam String dateinfo,@PathVariable Long mtid) throws UnsupportedEncodingException {
        Mountain mountain = mountainRepository.findById(mtid).orElse(null);
        double lat = mountain.getLatitude();
        double lon = mountain.getLongitude();

        String url = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo";

        //String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        try {
            // URI 빌더로 URL 생성
            URI uri = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("longitude", lon)
                    .queryParam("latitude", lat)
                    .queryParam("locdate", dateinfo)
                    .queryParam("dnYn", "Y")
                    .build(false)
                    .toUri();
            
            String uriWithServiceKey = uri.toString() + "&ServiceKey=" + sunInfoApiKey;

            
            //디버깅
            System.out.println("Final URI: " + uriWithServiceKey);
            

            String xmlResponse = restTemplate.getForObject(URI.create(uriWithServiceKey), String.class);
            
            //XML Parsing
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            // XML 응답을 UTF-8로 읽기
            InputSource is = new InputSource(new StringReader(xmlResponse));
            is.setEncoding("utf-8");  // 인코딩 명시적 설정
            Document doc = builder.parse(is);

            NodeList locdateNode = doc.getElementsByTagName("locdate");
            NodeList sunsetNode = doc.getElementsByTagName("sunset");
            NodeList sunriseNode = doc.getElementsByTagName("sunrise");
            
            Map<String, String> result = new HashMap<>();
            result.put("locdate", locdateNode.item(0).getTextContent());
            result.put("sunset", sunsetNode.item(0).getTextContent().trim());
            result.put("sunrise", sunriseNode.item(0).getTextContent().trim());
    
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace(); // 디버깅 시에 예외 메시지 확인 가능
            return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "옷차림 추천", description = "기온을 요청파라미터에 담으면 출몰정보 API를 통해 일출일몰 정보를 불러온다.")
    @GetMapping("/clothesrecommend")
    public ResponseEntity<String> wearRecommend(@Parameter(name="temp", description = "옷차림 정보를 가져올 기온", example="6.7") @RequestParam(value="temp", required= false) Float temp) throws UnsupportedEncodingException {
        
        if (temp == null) {
            return ResponseEntity.badRequest().body("temp 파라미터는 필수입니다.");
        }

        String wearInfo = weatherService.mountaineeringClothes(temp);

        return ResponseEntity.ok(wearInfo);
    }

}   
