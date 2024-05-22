package com.hikingplanner.hikingplanner.service;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;

@Service
public class TwilioService {
    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    public Call makePhoneCall(String toPhoneNumber) throws URISyntaxException {
        Twilio.init(accountSid, authToken);
        return Call.creator(
            new PhoneNumber(toPhoneNumber),
            new PhoneNumber(fromPhoneNumber),
            new URI("http://demo.twilio.com/docs/voice.xml")  // 이 URL은 Twilio에서 제공하는 기본 응답 문서 URL입니다. 실제 사용 시 변경이 필요할 수 있습니다.
        ).create();
    }
    
}
