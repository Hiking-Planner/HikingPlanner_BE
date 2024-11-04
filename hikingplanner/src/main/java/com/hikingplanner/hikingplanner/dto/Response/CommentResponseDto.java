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
    private String nickname;

    public CommentResponseDto(Long commentId, String content, LocalDateTime writeDatetime, UserEntity user) {
        this.commentId = commentId;
        this.content = content;
        this.writeDatetime = writeDatetime;
        this.nickname = user.getNickname();
    }
}
