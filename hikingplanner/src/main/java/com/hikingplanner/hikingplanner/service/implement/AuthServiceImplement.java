package com.hikingplanner.hikingplanner.service.implement;


import org.springframework.http.ResponseEntity;




import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

            CertificationEntity cerCertificationEntity = new CertificationEntity(userId, email, certificationNumber);
            certificationRepository.save(cerCertificationEntity);
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.dataseError();
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

             // 인증 성공 시 isCertified 플래그 설정
        certificationEntity.markAsCertified();
        certificationRepository.save(certificationEntity);


        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.dataseError();
        }

        return CheckCertificationResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {
        try {
            String userId =dto.getId();

            //아이디 중복을 체크
            boolean isExistId = userRepository.existsByUserId(userId);
            if(isExistId) return SignUpResponseDto.duplicateId();

            // 이메일 인증 여부 확인
            CertificationEntity certificationEntity = certificationRepository.findByUserId(userId);
            if (certificationEntity == null || !certificationEntity.isCertified()) {
                return SignUpResponseDto.certificationFail();
            }

            //비밀번호 암호화 작업
            String password = dto.getPassword();
            String encodedPassword = passwordEncoder.encode(password);
            dto.setPassword(encodedPassword);

            //새로운 사용자 생성
            UserEntity userEntity = new UserEntity(dto);
            userRepository.save(userEntity);

            //인증정보 삭제
            certificationRepository.deleteByUserId(userId);
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return ResponseDto.dataseError();
        }
        return SignUpResponseDto.success();
    }

    @Override
    public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
    String token = null;
    String nickname =null;
    String userId = dto.getId();

    try {
        // String userId = dto.getId();

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
        nickname = userEntity.getNickname();

    } catch (Exception exception) {
        exception.printStackTrace();
        return ResponseDto.dataseError();  // 데이터베이스 에러 처리
    }

    return SignInResponseDto.success(token, userId, nickname);  // 성공 시 토큰 반환
}

}





