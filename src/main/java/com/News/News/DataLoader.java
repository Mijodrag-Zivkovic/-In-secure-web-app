package com.News.News;

import com.News.News.models.Article;
import com.News.News.models.Role;
import com.News.News.models.UserAccount;
import com.News.News.repositories.ArticleRepository;
import com.News.News.repositories.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Create and save users
        UserAccount admin = new UserAccount();
        admin.setUsername("admin");
        admin.setPassword("adminpass");
        admin.setEmail("admin@articles.com");// "{noop}" for unencoded passwords
        admin.setRole(Role.ADMIN);
        userAccountRepository.save(admin);

        UserAccount writer = new UserAccount();
        writer.setUsername("writer");
        writer.setPassword("writerpass");
        writer.setEmail("writer@articles.com");
        writer.setRole(Role.WRITER);
        userAccountRepository.save(writer);

        UserAccount reader = new UserAccount();
        reader.setUsername("reader");
        reader.setPassword("readerpass");
        reader.setEmail("reader@articles.com");
        reader.setRole(Role.READER);
        userAccountRepository.save(reader);

        // Create and save articles
        Article article1 = new Article();
        article1.setTitle("Spring Boot Basics");
        article1.setContent("Learn the basics of Spring Boot.");
        article1.setWriterId(writer.getId());
        articleRepository.save(article1);

        Article article2 = new Article();
        article2.setTitle("Understanding JWT");
        article2.setContent("An introduction to JWT and how it works.");
        article2.setWriterId(writer.getId());
        articleRepository.save(article2);

        System.out.println("Sample users and articles loaded.");
    }
}