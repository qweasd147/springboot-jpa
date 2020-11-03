package com.example.service;

import com.example.model.Article;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ArticleServiceFacade {

    private final ArticleService articleService;


    public List<Article> searchAllWithInfo(){

        List<Article> articles = this.articleService.searchAll();

        articles.forEach((article)-> Hibernate.initialize(article.getTags()));

        return articles;
    }

}
