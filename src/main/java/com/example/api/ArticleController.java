package com.example.api;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.example.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping
    public List<ArticleDto.Response> searchAll(){

        List<Article> articles = articleService.searchAll();

        return articles.stream()
                .map(ArticleDto.Response::of)
                .collect(Collectors.toList());
    }

    @GetMapping("/{articleIdx}")
    public ArticleDto.Response searchOne(@PathVariable Long articleIdx){

        Article article = articleService.searchOne(articleIdx);

        return ArticleDto.Response.of(article);
    }

}
