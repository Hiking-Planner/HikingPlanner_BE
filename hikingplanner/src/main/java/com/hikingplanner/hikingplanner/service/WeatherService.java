
package com.hikingplanner.hikingplanner.service;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class WeatherService {
    
    public String mountaineeringClothes(float temp) {
        if (temp < -7){
            return "한파 속 등산은 위험해요! 등산 계획을 미루는 건 어떨까요?";
        }

        else if (temp >= -7 && temp < 5){
            return "겨울에는 강한 바람과 기온의 급강하, 폭설 등 위험 요소들이 많으니 조심하세요! 윈드블럭 소재의 긴바지, 보온기능성 내의를 착용하고 방수 소재의 방한패딩을 준비하셔야 해요!";
        }
        
        else if (temp >= 5 && temp < 12){
            return "꽃샘 추위와 바람을 막아 줄 바람막이, 스트레치 소재의 긴바지를 추천해요. 낮과 밤의 일교차가 심하고 날씨가 변덕스러우니 얇은 옷을 여러 겹 입으세요!";
        }

        else if (temp >= 12 && temp < 22){
            return "비교적 온화하지만 여전히 일교차가 커서 얇은 옷을 여러 겹 입는 것이 좋을 것 같아요. 이 기온에는 바람막이와 함께, 편안한 스트레치 소재의 긴바지와 기능성 티셔츠를 입는 게 좋아요! 날씨가 변덕스러울 가능성이 높으니 입고 벗기 편한 옷이 좋겠어요~";
        }

        else if(temp >= 22 && temp < 30){
            return "햇빛을 가려줄 모자, 스포츠 반팔과 바지를 추천드려요~ 무더위 속에서 산행을 할 때에는 그늘진 산길을 이용하세요! 무더위 속 산행은 되도록 피하세요!";
        }

        else{
            return "무더위 속 산행은 위험해요~ 등산 계획을 다시 정해보는 건 어때요?";
        }
    }
}
