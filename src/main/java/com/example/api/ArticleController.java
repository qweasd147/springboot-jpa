package com.example.api;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.example.service.ArticleService;
import com.example.service.ArticleServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/article")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleServiceFacade articleServiceFacade;

    @GetMapping
    public List<ArticleDto.Response> searchAll(){

        List<Article> articles = articleServiceFacade.searchAllWithInfo();

        return articles.stream()
                .map(ArticleDto.Response::of)
                .collect(Collectors.toList());
    }

    @GetMapping("/{articleIdx}")
    public ArticleDto.Response searchOne(@PathVariable Long articleIdx){

        Article article = articleService.searchOne(articleIdx);

        return ArticleDto.Response.of(article);
    }

    @PutMapping("/increment/{articleIdx}")
    public ArticleDto.Response increment(@PathVariable Long articleIdx){

        Article article = articleService.incrementCountWithLock(articleIdx);

        return ArticleDto.Response.of(article);
    }

    @GetMapping("/info/{articleIdx}")
    public ArticleDto.WithArticleInfo findWithArticleInfo(@PathVariable Long articleIdx){

        return this.articleService.findByIdxWithArticleInfo(articleIdx);
    }

    @PostMapping("/cache/{articleIdx}")
    public ArticleDto.Response putInCache(@PathVariable Long articleIdx){

        //캐시 put 유도
        Article article = this.articleServiceFacade.findByIdxMaybeInCache(articleIdx);

        return ArticleDto.Response.of(article);
    }

    @GetMapping("/cache/{articleIdx}")
    public ArticleDto.Response maybeHitCache(@PathVariable Long articleIdx){

        Article article = this.articleServiceFacade.findByIdxMaybeInCache(articleIdx);

        return ArticleDto.Response.of(article);
    }

}
