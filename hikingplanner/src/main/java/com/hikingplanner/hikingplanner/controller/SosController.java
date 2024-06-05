package com.hikingplanner.hikingplanner.controller;



import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Request.SosMessageRequest;
import com.hikingplanner.hikingplanner.dto.Request.SosRequest;
import com.hikingplanner.hikingplanner.dto.Response.SosResponse;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.UserRepository;
import com.hikingplanner.hikingplanner.service.GetNationalPointNumberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.service.DefaultMessageService;




@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "긴급 신고 기능 API")
@RequiredArgsConstructor
public class SosController {
    final DefaultMessageService messageService;

    @Autowired
    private GetNationalPointNumberService getNationalPointNumberService;
    @Autowired
    private UserRepository userRepository;

    private String sosto;
    private String sosfrom;

    //Autowired 안쓰면 에러남
    @Autowired
    public SosController(@Value("${nurigo.api.key}") String apiKey,
                        @Value("${nurigo.api.secret}") String apiSecret,
                        @Value("${sos.number}") String sosnumber,
                        @Value("${nurigo.api.allownumber}") String allownumber) {
      // 반드시 계정 내 등록된 유효한 API 키, API Secret Key를 입력해주셔야 합니다!
      this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
      this.sosto = sosnumber;
      this.sosfrom = allownumber;                     
  }
    @PostMapping("/sos")
    @Operation(summary = "긴급신고", description = "사용자 id와 현재위치(위도,경도), 시간을 POST하면 국가지점번호를 추가하여 반환한다.")
    public SosResponse Sos(@RequestBody SosRequest request) {
        String nationalposnum = getNationalPointNumberService.getNationalPointNumber(request.getLatitude(), request.getLongitude());
        String userid = request.getUserid();
        if (userid == null || userid.isEmpty()) {
            // userid가 없으면 예외를 던지거나 적절한 처리를 합니다.
            throw new IllegalArgumentException("userid는 필수입니다.");
        }

        Optional<UserEntity> userinfoOpt = userRepository.findById(userid);
        if (!userinfoOpt.isPresent()) {
            // 사용자가 존재하지 않으면 예외를 던지거나 적절한 처리를 합니다.
            throw new IllegalArgumentException("사용자를 찾을 수 없습니다.");
        }

        UserEntity userinfo = userinfoOpt.get();
        String username = userinfo.getName();
        String phonenumber = userinfo.getPhoneNumber(); 
        
        return new SosResponse(username,phonenumber,request.getLatitude(),request.getLongitude(),nationalposnum,request.getDate());
    }

    @PostMapping("/sendsosmessage")
    @Operation(summary = "긴급신고문자전송", description = "api 요청 시 긴급센터로 위급상황임을 알리는 신고문자를 전송")
    public SingleMessageSentResponse SosMessage(@RequestBody SosMessageRequest request) {
        Message message = new Message();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = request.getTime().format(formatter);
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        String messsageform = String.format(
        """
        산악 사고가 발생하였습니다. 긴급 상황이니 출동바랍니다.\n사고자는 %s, 연락처는 %s 입니다.\n
        사고 발생 위치의 국가지점번호는 %s 입니다.\n
        신고시각 : %s \n
        *본 문자는 하이킹플래너 어플에서 발송된 긴급신고입니다.
         """,request.getUsername(),request.getPhone_number(),request.getNationalposnum(),formattedTime);
        message.setFrom(sosfrom);
        message.setTo(sosto);
        message.setText(messsageform);

        SingleMessageSentResponse response = this.messageService.sendOne(new SingleMessageSendingRequest(message));
        System.out.println(response);

        return response;
    }
}
