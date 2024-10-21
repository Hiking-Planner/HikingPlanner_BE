package com.hikingplanner.hikingplanner.service.implement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;




import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.hikingplanner.hikingplanner.common.CertificationNumber;
import com.hikingplanner.hikingplanner.dto.Request.auth.CheckCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.EmailCertificationRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.IdCheckRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.SignInRequestDto;
import com.hikingplanner.hikingplanner.dto.Request.auth.SignUpRequestDto;
import com.hikingplanner.hikingplanner.dto.Response.ResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.CheckCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.EmailCertificationResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.IdCheckResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.SignInResponseDto;
import com.hikingplanner.hikingplanner.dto.Response.auth.SignUpResponseDto;
import com.hikingplanner.hikingplanner.entity.CertificationEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.provider.EmailProvider;
import com.hikingplanner.hikingplanner.provider.JwtProvider;
import com.hikingplanner.hikingplanner.repository.CertificationRepository;
import com.hikingplanner.hikingplanner.repository.UserRepository;
import com.hikingplanner.hikingplanner.service.AuthService;


import lombok.RequiredArgsConstructor;



@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{
    
    private final CertificationRepository certificationRepository;
    private final UserRepository userRepository;
    private final EmailProvider emailProvider;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtProvider jwtProvider;

    // @Value("${KAKAO_CLIENT_ID}") 
    // private String kakao_client_id;

    // @Value("${KAKAO_CLIENT_SECRET}") 
    // private String kakao_client_secret;

    // @Value("${KAKAO_REDIRECT_URI}")
    // private String kako_redirect_uri;

    // private static final String KAKAO_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    // private static final String KAKAO_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    
    @Override
    public ResponseEntity<? super IdCheckResponseDto> idCheck(IdCheckRequestDto dto) {
        try {
            String userId = dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return IdCheckResponseDto.duplicateId();
            
        } catch (Exception exception) {
            exception.printStackTrace();
            // return ResponseDto.dataseError();
        }
        return IdCheckResponseDto.succcess();
    }
    
    @Override
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(EmailCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();

            boolean isExistId=userRepository.existsByUserId(userId);
            if(isExistId) return EmailCertificationResponseDto.duplicateId();

            String certificationNumber = CertificationNumber.getCertificationNumber();

            boolean isSuccessed = emailProvider.sendCertificationMail(email, certificationNumber);
            if(!isSuccessed) return EmailCertificationResponseDto.mailSendFail();

            CertificationEntity ccerCertificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(ccerCertificationEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            // return ResponseDto.dataseError();
        }
        return EmailCertificationResponseDto.success();
    }
    
    @Override
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto) {
        try {
            String userId = dto.getId();
            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();

            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if(certificationEntity==null) return CheckCertificationResponseDto.certificationFail();

            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if (!isMatched) return CheckCertificationResponseDto.certificationFail();
        } catch (Exception exception) {
            exception.printStackTrace();
            // return ResponseDto.dataseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String userId =dto.getId();
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return SignUpResponseDto.duplicateId();

            String email = dto.getEmail();
            String certificationNumber = dto.getCertificationNumber();
            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);

            boolean isMatched = certificationEntity.getEmail().equals(email) && certificationEntity.getCertificationNumber().equals(certificationNumber);
            if(!isMatched) return SignUpResponseDto.certificationFail();

            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            certificationRepository.deleteByUserId(userId);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            // return ResponseDto.dataseError();
        }
        return SignUpResponseDto.success();
    }

    @Override
public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
    String token = null;

    try {
        String userId = dto.getId();

        // Optional로 유저 정보를 가져오고 처리
        UserEntity userEntity = userRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        String password = dto.getPassword();
        String encodedPassword = userEntity.getPassword();

        // 비밀번호 검증
        boolean isMatched = passwordEncoder.matches(password, encodedPassword);
        if (!isMatched) {
            return SignInResponseDto.signInFail();  // 비밀번호 불일치
        }

        // JWT 토큰 생성
        token = jwtProvider.create(userId);

    } catch (Exception exception) {
        exception.printStackTrace();
        // return ResponseDto.dataseError();  // 데이터베이스 에러 처리
    }

    return SignInResponseDto.success(token);  // 성공 시 토큰 반환
}

   
//     @Transactional
//     public String getKakaoAccessToken(String code) {

//     System.out.println("Authorization Code: " + code);  // 로그 추가

//     HttpHeaders headers = new HttpHeaders();
//     headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

//     // Http Response Body 객체 생성
//     MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//     params.add("grant_type", "authorization_code"); //카카오 공식문서 기준 authorization_code 로 고정
//     params.add("client_id", kakao_client_id); // 카카오 Dev 앱 REST API 키
//     params.add("redirect_uri", kako_redirect_uri); // 카카오 Dev redirect uri
//     params.add("code", code); // 프론트에서 인가 코드 요청시 받은 인가 코드값
//     params.add("client_secret", kakao_client_secret); // 카카오 Dev 카카오 로그인 Client Secret

//     // 헤더와 바디 합치기 위해 Http Entity 객체 생성
//     HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

//     // 카카오로부터 Access token 받아오기
//     RestTemplate rt = new RestTemplate();
//     ResponseEntity<String> accessTokenResponse = rt.exchange(
//             KAKAO_TOKEN_URI, // "https://kauth.kakao.com/oauth/token"
//             HttpMethod.POST,
//             kakaoTokenRequest,
//             String.class
//     );

//     // JSON Parsing (-> KakaoTokenDto)
//     ObjectMapper objectMapper = new ObjectMapper();
//     objectMapper.registerModule(new JavaTimeModule());
//     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//     KakaoTokenDto kakaoTokenDto = null;
//     try {
//         kakaoTokenDto = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenDto.class);
//     } catch (JsonProcessingException e) {
//         e.printStackTrace();
//     }

//     return kakaoTokenDto.getAccess_token();
// }

// public ResponseEntity<LoginResponseDto> kakaoLogin(String kakaoAccessToken) {
//     UserEntity userEntity = getKakaoInfo(kakaoAccessToken);
// // JWT 토큰 생성
//     String token = jwtProvider.create(userEntity.getUserId());
//     System.out.println("token: " + token);
//     LoginResponseDto loginResponseDto = new LoginResponseDto();
//     loginResponseDto.setLoginSuccess(true);
//     loginResponseDto.setToken(token);
//     loginResponseDto.setUserEntity(userEntity);

//     HttpHeaders headers = new HttpHeaders();
//     // 예시 헤더 추가
//     headers.add("Custom-Header", "Value");

//     UserEntity existOwner = userRepository.findById(userEntity.getUserId()).orElse(null);
//     try {
//         if (existOwner == null) {
//             System.out.println("처음 로그인 하는 회원입니다.");
//             userRepository.save(userEntity);
//         }

//         // // JWT 토큰 생성
//         // String token = jwtProvider.create(userEntity.getUserId());
//         // System.out.println("token: " + token);

//         LoginResponseDto loginResponseDto2 = new LoginResponseDto();
//         loginResponseDto2.setLoginSuccess(true);
//         loginResponseDto2.setUserEntity(userEntity);
//         loginResponseDto2.setToken(token);


//         return ResponseEntity.ok().headers(headers).body(loginResponseDto);

//     } catch (Exception e) {
//         loginResponseDto.setLoginSuccess(false);
//         return ResponseEntity.badRequest().body(loginResponseDto);
//     }
// }

// public UserEntity getKakaoInfo(String kakaoAccessToken) {
//     RestTemplate rt = new RestTemplate();

//     HttpHeaders headers = new HttpHeaders();
//     headers.add("Authorization", "Bearer " + kakaoAccessToken);
//     headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

//     HttpEntity<MultiValueMap<String, String>> accountInfoRequest = new HttpEntity<>(headers);

//     // POST 방식으로 API 서버에 요청 후 response 받아옴
//     ResponseEntity<String> accountInfoResponse = rt.exchange(
//             KAKAO_USER_INFO_URI, // "https://kapi.kakao.com/v2/user/me"
//             HttpMethod.POST,
//             accountInfoRequest,
//             String.class
//     );
    
//     // JSON Parsing (-> kakaoAccountDto)
//     ObjectMapper objectMapper = new ObjectMapper();
//     objectMapper.registerModule(new JavaTimeModule());
//     objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//     KakaoAccountDto kakaoAccountDto = null;
//     try {
//         kakaoAccountDto = objectMapper.readValue(accountInfoResponse.getBody(), KakaoAccountDto.class);
//     } catch (JsonProcessingException e) {
//         e.printStackTrace();
//     }

//     if (kakaoAccountDto == null || kakaoAccountDto.getKakao_account() == null || kakaoAccountDto.getKakao_account().getProfile() == null) {
//         throw new IllegalArgumentException("필수 정보가 누락되었습니다.");
//     }

    
	// // 회원가입 처리하기
    // String kakaoId = String.valueOf(kakaoAccountDto.getId());
    // UserEntity existOwner = userRepository.findById(kakaoId).orElse(null);
    // // 처음 로그인이 아닌 경우
    // if (existOwner != null) {
    //     return UserEntity.builder()
    //             .userId(kakaoId)
    //             .email(kakaoAccountDto.getKakao_account().getEmail())
    //             .name(kakaoAccountDto.getKakao_account().getProfile().getNickname())
    //             .phoneNumber(kakaoAccountDto.getKakao_account().getPhone_number())
    //             .profile_image(kakaoAccountDto.getKakao_account().getProfile().getProfile_image_url())
    //             .role("USER")
    //             .type("kakao")
    //             .build();
    // }
    // // 처음 로그인 하는 경우
    // else {
    //     return UserEntity.builder()
    //             .userId(kakaoId)
    //             .email(kakaoAccountDto.getKakao_account().getEmail())
    //             .name(kakaoAccountDto.getKakao_account().getProfile().getNickname())
    //             .phoneNumber(kakaoAccountDto.getKakao_account().getPhone_number())
    //             .profile_image(kakaoAccountDto.getKakao_account().getProfile().getProfile_image_url())
    //             .role("USER")
    //             .type("kakao")
    //             .build();
    // }
}





