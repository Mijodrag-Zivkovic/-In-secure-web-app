package com.News.News.controllers;

import com.News.News.dtos.AdminArticleRequest;
import com.News.News.dtos.ArticleRequest;
import com.News.News.dtos.ArticleResponse;
import com.News.News.services.AdminArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/articles")
@PreAuthorize("hasRole('ADMIN')")
public class AdminArticleController {

    private final AdminArticleService adminArticleService;

    public AdminArticleController(AdminArticleService adminArticleService) {
        this.adminArticleService = adminArticleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponse> createArticle(@RequestBody @Valid AdminArticleRequest requestDTO) {
        ArticleResponse responseDTO = adminArticleService.createArticle(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponse> getArticleById(@PathVariable Long id) {
        ArticleResponse responseDTO = adminArticleService.getArticleById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<ArticleResponse> articles = adminArticleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        adminArticleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(
            @PathVariable Long id,
            @RequestBody @Valid AdminArticleRequest requestDTO) {
        ArticleResponse responseDTO = adminArticleService.updateArticle(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

}
