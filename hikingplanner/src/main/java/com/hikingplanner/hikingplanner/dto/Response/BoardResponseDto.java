package com.hikingplanner.hikingplanner.dto.Response;

import java.time.LocalDateTime;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardResponseDto {
    
    private Long boardId;
    private String title;
    private String content;
    private LocalDateTime writeDatetime;
    private int favoriteCount;
    private int commentCount;
    private int viewCount;
    private String mountainName;
    private String imageUrl;
    private String nickname; // 작성자 닉네임
}
