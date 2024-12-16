package com.News.News.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Long writerId;
}
