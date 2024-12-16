package com.News.News.controllers;

import com.News.News.dtos.ArticleRequestDTO;
import com.News.News.dtos.ArticleResponseDTO;
import com.News.News.services.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleResponseDTO> createArticle(@RequestBody @Valid ArticleRequestDTO requestDTO) {
        ArticleResponseDTO responseDTO = articleService.createArticle(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> getArticleById(@PathVariable Long id) {
        ArticleResponseDTO responseDTO = articleService.getArticleById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDTO>> getAllArticles() {
        List<ArticleResponseDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArticleResponseDTO> updateArticle(
            @PathVariable Long id,
            @RequestBody @Valid ArticleRequestDTO requestDTO) {
        ArticleResponseDTO responseDTO = articleService.updateArticle(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
