package com.News.News.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminArticleRequest {

    private String title;
    private String content;
    private Long writerId;

}
