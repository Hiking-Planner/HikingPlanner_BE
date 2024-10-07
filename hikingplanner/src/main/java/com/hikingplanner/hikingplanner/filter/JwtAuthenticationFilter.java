package com.hikingplanner.hikingplanner.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.provider.JwtProvider;
import com.hikingplanner.hikingplanner.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            // JWT 토큰을 파싱하여 가져옴
            String token = parseBearerToken(request);
            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // JWT 토큰을 검증하여 userId 추출
            String userId = jwtProvider.validate(token);  // validate 메서드 오타 수정
            if (userId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 유저 정보를 데이터베이스에서 조회
            UserEntity userEntity = userRepository.findByUserId(userId)
                    .orElse(null);
            if (userEntity == null) {
                filterChain.doFilter(request, response); // 유저를 찾지 못하면 필터 체인을 계속 실행
                return;
            }

            // 유저의 역할(Role)을 가져옴
            String role = "ROLE_" + userEntity.getRole(); // ROLE_USER 또는 ROLE_ADMIN 등

            // Spring Security 인증 객체 생성 및 설정
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(role));

            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userEntity.getEmail(), null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception exception) {
            // 예외 발생 시 로그를 남기고 401 상태 코드 반환
            logger.error("JWT 인증 실패: ", exception);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT Token");
            return;
        }

        // 필터 체인을 계속 실행
        filterChain.doFilter(request, response);
    }

    // Bearer 토큰을 파싱하는 메서드
    private String parseBearerToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (StringUtils.hasText(authorization) && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }
}


