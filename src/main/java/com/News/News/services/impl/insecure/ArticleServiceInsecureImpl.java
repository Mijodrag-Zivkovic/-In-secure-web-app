package com.News.News.services.impl.insecure;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.exceptions.AppException;
import com.News.News.exceptions.ErrorCode;
import com.News.News.mappers.AccountMapper;
import com.News.News.mappers.ArticleMapper;
import com.News.News.models.Article;
import com.News.News.models.UserAccount;
import com.News.News.repositories.ArticleRepository;
import com.News.News.repositories.AccountRepository;
import com.News.News.security.model.CustomAuthenticationToken;
import com.News.News.security.services.JwtService;
import com.News.News.services.ArticleService;
import jakarta.persistence.EntityManager;
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
@Profile("insecure")
public class ArticleServiceInsecureImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final AccountRepository accountRepository;
    private final ArticleMapper articleMapper;
    private final EntityManager entityManager;


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
                .map(articleMapper::toResponseDTO)
                .collect(Collectors.toList());
    }


    public List<ArticleResponse> searchArticles(String keyword) {
//        String query = "SELECT * FROM newsapp.articles WHERE title LIKE \'%" + keyword + "%\'";
//        System.out.println(keyword);
//        System.out.println(query);
//        List<Article> foundArticles = entityManager.createNativeQuery(query, Article.class).getResultList();

//        List<ArticleResponse> articles = new ArrayList<>();
//        try{
//            articles= foundArticles.stream()
//                    .map(articleMapper::toResponseDTO)
//                    .collect(Collectors.toList());
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException(e);
//        }
//        List<Article> articles = articleRepository.searchArticles(keyword);
//        System.out.println(articles);
//        return articles.stream().map(articleMapper::toResponseDTO).collect(Collectors.toList());
        return new ArrayList<>();
    }

    @Override
    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new AppException("Article not found with ID: " + id, ErrorCode.NOT_FOUND));
        //if(isUserOwnerOfArticle(article)) {
            articleRepository.delete(article);
        //}
        //else new AppException("You are not Article with ID: " + id, ErrorCode.NOT_FOUND));
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

//    boolean isUserOwnerOfArticle(Article article) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        Long userId = ((CustomAuthenticationToken) authentication).getUserId();
//        if(article.getWriterId().equals(userId)) {
//            return true;
//        }
//        return false;
//    }
}
