package com.hikingplanner.hikingplanner.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikingplanner.hikingplanner.dto.Request.CommentRequestDto;
import com.hikingplanner.hikingplanner.dto.Response.CommentResponseDto;
import com.hikingplanner.hikingplanner.entity.BoardEntity;
import com.hikingplanner.hikingplanner.entity.CommentEntity;
import com.hikingplanner.hikingplanner.entity.UserEntity;
import com.hikingplanner.hikingplanner.repository.BoardRepository;
import com.hikingplanner.hikingplanner.repository.CommentRepository;
import com.hikingplanner.hikingplanner.repository.UserRepository;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponseDto createComment(String email, CommentRequestDto dto) {
        // 사용자 이메일로 UserEntity 찾기
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        // 게시물 ID로 BoardEntity 찾기
        BoardEntity board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new RuntimeException("Board not found with id: " + dto.getBoardId()));

        // 새로운 댓글 생성
        CommentEntity comment = CommentEntity.builder()
                .content(dto.getContent())
                .writeDatetime(LocalDateTime.now())
                .user(user)
                .board(board)
                .build();

        // 댓글 저장
        CommentEntity savedComment = commentRepository.save(comment);

        // 게시물의 comment_count 증가 및 저장
        board.setCommentCount(board.getCommentCount() + 1);
        boardRepository.save(board);

        // 댓글 응답 DTO 반환
        return new CommentResponseDto(
                savedComment.getCommentId(),
                savedComment.getContent(),
                savedComment.getWriteDatetime(),
                user
        );
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getCommentsByBoard(Long boardId) {
        // 게시물 ID로 BoardEntity 찾기
        BoardEntity board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found with id: " + boardId));

        // 해당 게시물의 모든 댓글 가져오기
        List<CommentEntity> comments = commentRepository.findAllByBoard(board);

        // 댓글 목록을 응답 DTO로 변환하여 반환
        return comments.stream()
                .map(comment -> new CommentResponseDto(
                        comment.getCommentId(),
                        comment.getContent(),
                        comment.getWriteDatetime(),
                        comment.getUser()
                ))
                .collect(Collectors.toList());
    }

    // 댓글 삭제 로직
    public void deleteComment(Long commentId, String email) {
        CommentEntity comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new IllegalArgumentException("Comment not found"));

        // 댓글 작성자와 현재 인증된 사용자가 같은지 확인
        if (!comment.getUser().getEmail().equals(email)) {
            throw new IllegalArgumentException("You are not the owner of this comment");
        }

        commentRepository.delete(comment);
    }
}
