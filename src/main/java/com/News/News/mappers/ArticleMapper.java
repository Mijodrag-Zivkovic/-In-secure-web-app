package com.News.News.mappers;

import com.News.News.dtos.AdminArticleRequest;
import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.models.Article;
import org.springframework.stereotype.Component;

@Component
public class ArticleMapper {

    public Article toEntity(ArticleRequest dto) {
        return new Article(dto.getTitle(), dto.getContent(), dto.getWriterId());
    }

    // Convert Article entity to ArticleResponseDTO
    public ArticleResponse toResponseDTO(Article article) {
        return new ArticleResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getWriterId()
        );
    }

    public Article toEntity(AdminArticleRequest dto) {
        return new Article(dto.getTitle(), dto.getContent(), dto.getWriterId());
    }
}
