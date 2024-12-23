package com.hikingplanner.hikingplanner.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hikingplanner.hikingplanner.dto.Response.BoardResponseDto;
import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.service.BoardService;
import com.hikingplanner.hikingplanner.service.S3ImageService;
import com.hikingplanner.hikingplanner.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "게시물 조회/작성/수정/삭제 API")
public class BoardController {
    private final BoardService boardService;
    private final UserService userService;
    private final S3ImageService s3ImageService;

     public BoardController(BoardService boardService, UserService userService, S3ImageService s3ImageService) {
        this.s3ImageService = s3ImageService;
        this.boardService = boardService;
        this.userService = userService;
    }

    // 게시물 생성 (이미지 업로드 포함)
    @Operation(summary = "게시물 생성 API")
    @PostMapping("boards")
    public ResponseEntity<BoardEntity> createBoard(@RequestParam("title") String title,
                                                   @RequestParam("content") String content,
                                                   @RequestParam("mountainName") String mountainName,
                                                   @RequestParam(value = "image", required = false) MultipartFile image) {
        // JWT로부터 인증된 사용자 정보를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();  // JWT에 저장된 사용자 이메일 혹은 userId
        
        
        UserEntity user = userService.findByEmail(email);

        
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3ImageService.upload(image); 
        }

        
        BoardEntity board = boardService.createBoard(title, content, user, mountainName, imageUrl);
        return ResponseEntity.ok(board);
    }

    // 단일 게시물 조회
    @GetMapping("boards/{boardId}") //프론트에서 쓸일이 있을까 싶음
    public ResponseEntity<BoardEntity> getBoardById(@PathVariable("boardId") Long boardId) {
    BoardEntity board = boardService.findBoardById(boardId);
    return ResponseEntity.ok(board);
    }

    @Operation(summary = "특정 사용자가 작성한 게시물 조회 API")
    @GetMapping("boards/user/{userId}")
    public ResponseEntity<List<BoardEntity>> getBoardsByUserId(@PathVariable("userId") String userId) {
    List<BoardEntity> boards = boardService.getBoardsByUserId(userId);
    return ResponseEntity.ok(boards);
    }

    // 게시물 전체 조회
    @Operation(summary = "게시물 전체 조회 API")
    @GetMapping("boards")
    public ResponseEntity<List<BoardResponseDto>> getAllBoards() {
    List<BoardResponseDto> boards = boardService.getAllBoards();
    return ResponseEntity.ok(boards);
}

    @Operation(summary = "게시물 삭제 API")
    @DeleteMapping("/boards/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.noContent().build();
    }
    

    @Operation(summary = "게시물 수정 API")
    @PutMapping("boards/{boardId}")
    public ResponseEntity<BoardEntity> updateBoard(
    @PathVariable(value ="boardId", required = false) Long boardId,
    @RequestParam(value ="title", required = false) String title,
    @RequestParam(value ="content", required = false) String content,
    @RequestParam(value ="mountainName", required = false ) String mountainName,
    @RequestParam(value = "image", required = false) MultipartFile image) {

    // JWT로부터 인증된 사용자 정보를 가져옴
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String email = authentication.getName(); //사용자 이메일 가져오기
    
    
    UserEntity user = userService.findByEmail(email);

    // 이미지 업로드 처리
    String imageUrl = null;
    if (image != null && !image.isEmpty()) {
        imageUrl = s3ImageService.upload(image); // 새로운 이미지 업로드
    }

    
    BoardEntity updatedBoard = boardService.updateBoard(boardId, title, content, mountainName, imageUrl, user);
    return ResponseEntity.ok(updatedBoard);
}
}
