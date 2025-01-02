package com.News.News.services.impl;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.ArticleMapper;
import com.News.News.models.Article;
import com.News.News.models.UserAccount;
import com.News.News.repositories.ArticleRepository;
import com.News.News.repositories.UserAccountRepository;
import com.News.News.services.ArticleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceInsecureImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserAccountRepository userAccountRepository;
    private final ArticleMapper articleMapper;

    public ArticleServiceInsecureImpl(ArticleRepository articleRepository,
                                      UserAccountRepository userAccountRepository,
                                      ArticleMapper articleMapper) {
        this.articleRepository = articleRepository;
        this.userAccountRepository = userAccountRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public ArticleResponse createArticle(ArticleRequest requestDTO) {
        // Validate the user
        UserAccount user = userAccountRepository.findById(requestDTO.getWriterId())
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

        // Fetch username
        String username = userAccountRepository.findById(article.getWriterId())
                .map(UserAccount::getUsername)
                .orElse("Unknown User");

        return articleMapper.toResponseDTO(article);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> {
                    String username = userAccountRepository.findById(article.getWriterId())
                            .map(UserAccount::getUsername)
                            .orElse("Unknown User");
                    return articleMapper.toResponseDTO(article);
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deleteArticle(Long id) {
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article not found with ID: " + id);
        }
        articleRepository.deleteById(id);
    }

    public ArticleResponse updateArticle(Long id, ArticleRequest requestDTO) {
        // Find the existing article
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));

        // Validate the user
        UserAccount user = userAccountRepository.findById(requestDTO.getWriterId())
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
