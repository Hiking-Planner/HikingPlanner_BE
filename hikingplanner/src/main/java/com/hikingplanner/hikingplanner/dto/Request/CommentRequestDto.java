package com.hikingplanner.hikingplanner.dto.Request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentRequestDto {
    private String content;
    private Long boardId; // 댓글이 달릴 게시물 ID
}
