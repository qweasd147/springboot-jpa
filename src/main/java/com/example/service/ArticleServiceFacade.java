package com.example.service;

import com.example.config.CacheConfig;
import com.example.model.Article;
import com.example.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class ArticleServiceFacade {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;


    public List<Article> searchAllWithInfo(){

        List<Article> articles = this.articleService.searchAll();

        articles.forEach((article)-> Hibernate.initialize(article.getTags()));

        return articles;
    }

    @Cacheable(cacheNames = CacheConfig.CACHE_FOR_3_SECONDS)
    public Article findByIdxMaybeInCache(Long articleIdx){

        return this.articleRepository.findById(articleIdx)
                .orElseThrow(() -> new IllegalArgumentException("article을 찾을 수 없음" + articleIdx));
    }

}
