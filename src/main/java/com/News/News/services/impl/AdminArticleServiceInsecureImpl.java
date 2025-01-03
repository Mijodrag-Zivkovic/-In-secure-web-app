package com.News.News.services.impl;

import com.News.News.dtos.AdminArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.ArticleMapper;
import com.News.News.models.Article;
import com.News.News.models.UserAccount;
import com.News.News.repositories.ArticleRepository;
import com.News.News.repositories.AccountRepository;
import com.News.News.services.AdminArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminArticleServiceInsecureImpl implements AdminArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final ArticleMapper articleMapper;

    public AdminArticleServiceInsecureImpl(ArticleRepository articleRepository, AccountRepository accountRepository, ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.accountRepository = accountRepository;
        this.articleMapper = articleMapper;
    }

    public ArticleResponse createArticle(AdminArticleRequest requestDTO) {
        // Validate the user
        UserAccount user = accountRepository.findById(requestDTO.getWriterId())
                .orElseThrow(() -> new AppException("User not found with ID: " + requestDTO.getWriterId(), ErrorCode.NOT_FOUND));

        // Map DTO to entity and save
        Article article = articleMapper.toEntity(requestDTO);
        article = articleRepository.save(article);

        // Map entity to response DTO
        return articleMapper.toResponseDTO(article);
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));

        return articleMapper.toResponseDTO(article);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        return articles.stream().map(articleMapper::toResponseDTO).collect(Collectors.toList());
    }

    @Override
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article not found with ID: " + id);
        }
        articleRepository.deleteById(id);
    }
    @Override
    public ArticleResponse updateArticle(Long id, AdminArticleRequest requestDTO) {
        // Find the existing article
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));

        // Validate the user
        UserAccount user = accountRepository.findById(requestDTO.getWriterId())
                .orElseThrow(() -> new AppException("User not found with ID: " + requestDTO.getWriterId(), ErrorCode.NOT_FOUND));

        // Update the article fields
        article.setTitle(requestDTO.getTitle());
        article.setContent(requestDTO.getContent());
        article.setWriterId(requestDTO.getWriterId());

        // Save the updated article
        article = articleRepository.save(article);

        // Return the updated article as a response DTO
        return articleMapper.toResponseDTO(article);
    }
}
