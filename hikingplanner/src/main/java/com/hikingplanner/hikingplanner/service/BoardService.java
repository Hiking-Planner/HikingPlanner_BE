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
    List<BoardEntity> boards = boardRepository.findAll();
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
        dto.setUserNickname(board.getUser().getNickname()); // 작성자 닉네임 설정
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
}
