package com.News.News.services;


import com.News.News.dtos.AdminArticleRequest;
import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;

import java.util.List;

public interface AdminArticleService {

    ArticleResponse createArticle(AdminArticleRequest requestDTO);

    ArticleResponse getArticleById(Long id);

    List<ArticleResponse> getAllArticles();

    void deleteArticle(Long id);

    ArticleResponse updateArticle(Long id, AdminArticleRequest requestDTO);
}
