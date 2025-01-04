package com.News.News.services.impl.secure;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.ArticleMapper;
import com.News.News.models.Article;
import com.News.News.models.UserAccount;
import com.News.News.repositories.AccountRepository;
import com.News.News.repositories.ArticleRepository;
import com.News.News.security.model.CustomAuthenticationToken;
import com.News.News.services.ArticleService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Profile("secure")
public class ArticleServiceSecureImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final ArticleMapper articleMapper;

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
        return articleMapper.toResponseDTO(article);
    }

    @Override
    public List<ArticleResponse> getAllArticles() {
        return articleRepository.findAll().stream()
                .map(articleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleResponse> searchArticles(String keyword) {
        return new ArrayList<>();
    }

    @Override
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));
        if(isUserOwnerOfArticle(article)) {
            articleRepository.delete(article);
        }
        else throw new AppException("You are not the owner of Article with ID: " + id, ErrorCode.FORBIDDEN);
    }

    public ArticleResponse updateArticle(Long id, ArticleRequest requestDTO) {
        // Find the existing article
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));

        if(isUserOwnerOfArticle(article)) {
            article.setTitle(requestDTO.getTitle());
            article.setContent(requestDTO.getContent());

            // Save the updated article
            article = articleRepository.save(article);

            // Return the updated article as a response DTO
            return articleMapper.toResponseDTO(article);
        }
        else throw new AppException("You are not the owner of Article with ID: " + id, ErrorCode.FORBIDDEN);

        // Update the article fields

    }

    boolean isUserOwnerOfArticle(Article article) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = ((CustomAuthenticationToken) authentication).getUserId();
        return article.getWriterId().equals(userId);
    }
}
