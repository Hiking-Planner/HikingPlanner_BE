package com.hikingplanner.hikingplanner.dto.Response;

import java.time.LocalDateTime;

import com.hikingplanner.hikingplanner.entity.UserEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentResponseDto {
    private Long commentId;
    private String content;
    private LocalDateTime writeDatetime;
    private String userName;

    public CommentResponseDto(Long commentId, String content, LocalDateTime writeDatetime, UserEntity user) {
        this.commentId = commentId;
        this.content = content;
        this.writeDatetime = writeDatetime;
        this.userName = user.getName(); // 사용자 이름 가져오기
    }
}
