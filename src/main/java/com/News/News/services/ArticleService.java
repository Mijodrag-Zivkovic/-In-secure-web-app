package com.News.News.services;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;

import java.util.List;

public interface ArticleService {
    ArticleResponse createArticle(ArticleRequest requestDTO);

    ArticleResponse getArticleById(Long id);

    List<ArticleResponse> getAllArticles();

    List<ArticleResponse> searchArticles(String keyword);

    void deleteArticle(Long id);

    ArticleResponse updateArticle(Long id, ArticleRequest requestDTO);
}
