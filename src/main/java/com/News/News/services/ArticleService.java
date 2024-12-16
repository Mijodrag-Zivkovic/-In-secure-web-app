package com.News.News.services;

import com.News.News.dtos.ArticleRequestDTO;
import com.News.News.dtos.ArticleResponseDTO;

import java.util.List;

public interface ArticleService {
    ArticleResponseDTO createArticle(ArticleRequestDTO requestDTO);

    ArticleResponseDTO getArticleById(Long id);

    List<ArticleResponseDTO> getAllArticles();

    void deleteArticle(Long id);

    ArticleResponseDTO updateArticle(Long id, ArticleRequestDTO requestDTO);
}
