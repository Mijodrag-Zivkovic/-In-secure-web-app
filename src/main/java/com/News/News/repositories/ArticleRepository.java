package com.News.News.repositories;

import com.News.News.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

//    @Query("SELECT a FROM Article a WHERE a.title LIKE CONCAT('%', :title, '%')",nativeQuery = true)
//    List<Article> searchArticles(@Param("title") String title);
//    @Query(value = "SELECT * FROM articles WHERE title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
//    List<Article> searchArticles(@Param("title") String title);
    @Query(value = "SELECT * FROM newsapp.articles WHERE title LIKE CONCAT('%', :title, '%')", nativeQuery = true)
    List<Article> searchArticles(@Param("title") String title);
}
