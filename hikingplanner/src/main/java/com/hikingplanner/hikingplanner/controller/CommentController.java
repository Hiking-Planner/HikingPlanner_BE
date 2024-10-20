package com.hikingplanner.hikingplanner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.dto.Request.CommentRequestDto;
import com.hikingplanner.hikingplanner.dto.Response.CommentResponseDto;
import com.hikingplanner.hikingplanner.service.CommentService;
import com.twilio.http.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto dto) {
        // JWT로부터 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // JWT에 저장된 사용자 이메일
        
        CommentResponseDto response = commentService.createComment(email, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam Long boardId) {

        // 특정 게시물의 댓글 목록을 조회
        List<CommentResponseDto> comments = commentService.getCommentsByBoard(boardId);
        return ResponseEntity.ok(comments);
    }
    
}
