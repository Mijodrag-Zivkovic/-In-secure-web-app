package com.News.News.controllers;

import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PreAuthorize("hasRole('WRITER')")
    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody @Valid ArticleRequest requestDTO) {
        ArticleResponse responseDTO = articleService.createArticle(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        ArticleResponse responseDTO = articleService.getArticleById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<ArticleResponse> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @PreAuthorize("hasRole('WRITER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('WRITER')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestBody @Valid ArticleRequest requestDTO) {
        ArticleResponse responseDTO = articleService.updateArticle(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
