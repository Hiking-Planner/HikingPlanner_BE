package com.hikingplanner.hikingplanner.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.BoardRepository;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public BoardEntity createBoard(String title, String content, UserEntity user, String mountainName) {
        // 새로운 게시물 엔티티 생성
        BoardEntity board = BoardEntity.builder()
            .title(title)
            .content(content)
            .user(user)
            .mountainName(mountainName)
            .writeDatetime(LocalDateTime.now())  // 현재 시간으로 작성 시간 설정
            .favoriteCount(0)  // 기본 값 설정
            .commentCount(0)   // 기본 값 설정
            .viewCount(0)      // 기본 값 설정
            .build();

        // 데이터베이스에 게시물 저장
        return boardRepository.save(board);
    }
}
