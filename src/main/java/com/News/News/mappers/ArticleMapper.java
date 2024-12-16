package com.News.News.mappers;

import com.News.News.dtos.ArticleRequestDTO;
import com.News.News.dtos.ArticleResponseDTO;
import com.News.News.models.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public Article toEntity(ArticleRequestDTO dto) {
        return new Article(dto.getTitle(), dto.getContent(), dto.getWriterId());
    }

    // Convert Article entity to ArticleResponseDTO
    public ArticleResponseDTO toResponseDTO(Article article) {
        return new ArticleResponseDTO(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getWriterId()
        );
    }
}
