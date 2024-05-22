package com.hikingplanner.hikingplanner.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.hikingplanner.hikingplanner.entity.CustomOAuth2User;
import com.hikingplanner.hikingplanner.provider.JwtProvider;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler{

    private final JwtProvider jwtProvider;
    
    @Override
	public void onAuthenticationSuccess(
        HttpServletRequest request,
         HttpServletResponse response,
			Authentication authentication
            ) throws IOException, ServletException {

                CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

                String userId = oAuth2User.getName();
                String token = jwtProvider.create(userId);

                response.sendRedirect("http://ec2-3-143-125-20.us-east-2.compute.amazonaws.com:8080/auth/oauth-response/" +token+ "/3600");;
	}
}
