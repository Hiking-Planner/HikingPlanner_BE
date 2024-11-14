package com.hikingplanner.hikingplanner.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.hikingplanner.hikingplanner.dto.Response.BoardResponseDto;
import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final S3ImageService s3ImageService;

    public BoardService(BoardRepository boardRepository, S3ImageService s3ImageService) {
        this.boardRepository = boardRepository;
        this.s3ImageService = s3ImageService;
    }

    public BoardEntity createBoard(String title, String content, UserEntity user, String mountainName, String imageUrl) {
        BoardEntity board = BoardEntity.builder()
            .title(title)
            .content(content)
            .user(user)
            .mountainName(mountainName)
            .imageUrl(imageUrl)  // 이미지 URL 저장
            .writeDatetime(LocalDateTime.now())
            .favoriteCount(0)
            .commentCount(0)
            .viewCount(0)
            .build();

        return boardRepository.save(board);
    }

    // 단일 게시물 조회
    public BoardEntity findBoardById(Long boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found with id: " + boardId));
    }

    public List<BoardResponseDto> getAllBoards() {
    List<BoardEntity> boards = boardRepository.findAllByOrderByWriteDatetimeDesc();
    return boards.stream().map(board -> {
        BoardResponseDto dto = new BoardResponseDto();
        dto.setBoardId(board.getBoardId());
        dto.setTitle(board.getTitle());
        dto.setContent(board.getContent());
        dto.setWriteDatetime(board.getWriteDatetime());
        dto.setFavoriteCount(board.getFavoriteCount());
        dto.setCommentCount(board.getCommentCount());
        dto.setViewCount(board.getViewCount());
        dto.setMountainName(board.getMountainName());
        dto.setImageUrl(board.getImageUrl());
        dto.setNickname(board.getUser().getNickname()); // 작성자 닉네임 설정
        return dto;
    }).collect(Collectors.toList());
}

    public void deleteBoard(Long boardId) {
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));

        // 게시물에 이미지가 있는 경우 S3에서 이미지 삭제
        if (board.getImageUrl() != null) {
            s3ImageService.deleteImageFromS3(board.getImageUrl());
        }

        // 게시물 삭제
        boardRepository.delete(board);
    }

    // userId로 게시물 조회
    public List<BoardEntity> getBoardsByUserId(String userId) {
        return boardRepository.findByUserUserId(userId);
    }

    public BoardEntity updateBoard(Long boardId, String title, String content, String mountainName, String imageUrl, UserEntity user) {
        // 게시물 조회
        BoardEntity board = boardRepository.findById(boardId)
            .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다: " + boardId));
    
        // 게시물 작성자와 수정자가 같아야됨
        if (!board.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("게시물을 수정할 권한이 없습니다.");
        }
    
        // 입력된 값만 업데이트 
        if (title != null && !title.trim().isEmpty()) {
            board.setTitle(title);
        }
        if (content != null && !content.trim().isEmpty()) {
            board.setContent(content);
        }
        if (mountainName != null && !mountainName.trim().isEmpty()) {
            board.setMountainName(mountainName);
        }
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            board.setImageUrl(imageUrl);
        }
    
       
        return boardRepository.save(board);
    }
    
}
