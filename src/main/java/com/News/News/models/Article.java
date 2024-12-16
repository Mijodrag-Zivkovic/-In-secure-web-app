package com.News.News.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @NotNull
    @Column(nullable = false)
    private String content;

    @NotNull
    @Column(nullable = false) // Ensures this field is not null
    private Long writerId;

    public Article(String title, String content, Long writerId) {
        this.title = title;
        this.content = content;
        this.writerId = writerId;
    }
}
