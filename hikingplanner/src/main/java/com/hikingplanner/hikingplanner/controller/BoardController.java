package com.hikingplanner.hikingplanner.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.service.BoardService;
import com.hikingplanner.hikingplanner.service.UserService;

import lombok.Getter;
import lombok.Setter;

@RestController
@RequestMapping("/api/v1/auth")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;

     public BoardController(BoardService boardService, UserService userService) {
        this.boardService = boardService;
        this.userService = userService;
    }

    @PostMapping("boards")
    public ResponseEntity<BoardEntity> createBoard(@RequestBody CreateBoardRequest request) {
        // JWT로부터 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // JWT에 저장된 사용자 이메일 혹은 userId
        
        // UserService를 통해 사용자를 데이터베이스에서 찾음
        UserEntity user = userService.findByEmail(email);

        // 게시물 생성
        BoardEntity board = boardService.createBoard(request.getTitle(), request.getContent(), user, request.getMountainName());
        return ResponseEntity.ok(board);
    }

    // 단일 게시물 조회
    @GetMapping("boards/{boardId}")
    public ResponseEntity<BoardEntity> getBoardById(@PathVariable("boardId") Long boardId) {
    BoardEntity board = boardService.findBoardById(boardId);
    return ResponseEntity.ok(board);
    }

    
    // 요청 데이터 DTO
    @Getter
    @Setter
    static class CreateBoardRequest {
        private String title;
        private String content;
        private String mountainName;
        
        // Getters and Setters
    }
}
