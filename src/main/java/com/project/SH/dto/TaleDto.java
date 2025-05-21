package com.project.SH.dto;

import lombok.Data;

@Data
public class TaleDto {
    private Long id;
    private String title;
    private String author;
    private String content;
    private int likes;
    // без likedByUsers
}
