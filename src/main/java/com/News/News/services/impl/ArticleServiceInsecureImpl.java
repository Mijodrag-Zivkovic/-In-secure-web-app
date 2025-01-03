package com.News.News.services.impl;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.ArticleMapper;
import com.News.News.models.Article;
import com.News.News.models.UserAccount;
import com.News.News.repositories.ArticleRepository;
import com.News.News.repositories.AccountRepository;
import com.News.News.security.model.CustomAuthenticationToken;
import com.News.News.security.model.CustomUserDetails;
import com.News.News.security.services.JwtService;
import com.News.News.services.ArticleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class ArticleServiceInsecureImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final ArticleMapper articleMapper;
    private final JwtService jwtService;


//    public ArticleServiceInsecureImpl(ArticleRepository articleRepository,
//                                      AccountRepository accountRepository,
//                                      ArticleMapper articleMapper) {
//        this.articleRepository = articleRepository;
//        this.accountRepository = accountRepository;
//        this.articleMapper = articleMapper;
//    }

    @Override
    public ArticleResponse createArticle(ArticleRequest requestDTO) {
//        UserAccount user = accountRepository.findById(requestDTO.getWriterId())
//                .orElseThrow(() -> new AppException("User not found with ID: " + requestDTO.getWriterId(), ErrorCode.NOT_FOUND));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomAuthenticationToken) authentication).getUserId();
        Article article = articleMapper.toEntity(requestDTO, userId);
        article = articleRepository.save(article);
        return articleMapper.toResponseDTO(article);
    }

    @Override
    public ArticleResponse getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));

        // Fetch username
        String username = accountRepository.findById(article.getWriterId())
                .map(UserAccount::getUsername)
                .orElse("Unknown User");

        return articleMapper.toResponseDTO(article);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(article -> {
                    String username = accountRepository.findById(article.getWriterId())
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
//        UserAccount user = accountRepository.findById(writerId)
//                .orElseThrow(() -> new AppException("User not found with ID: " + writerId, ErrorCode.NOT_FOUND));

        // Update the article fields
        article.setTitle(requestDTO.getTitle());
        article.setContent(requestDTO.getContent());

        // Save the updated article
        article = articleRepository.save(article);

        // Return the updated article as a response DTO
        return articleMapper.toResponseDTO(article);
    }
}
