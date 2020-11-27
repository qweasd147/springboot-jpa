package com.example.api;

import com.example.model.Article;
import com.example.model.ArticleDetail;
import com.example.model.ArticleDto;
import com.example.model.Tag;
import com.example.service.ArticleService;
import com.example.service.ArticleServiceFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/article")
@RequiredArgsConstructor
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleServiceFacade articleServiceFacade;

    @PersistenceContext
    private EntityManager em;

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
    @ResponseStatus(HttpStatus.CREATED)
    public void putInCache(@PathVariable Long articleIdx){

        //캐시 put 유도
        this.articleServiceFacade.findByIdxMaybeInCache(articleIdx);
    }

    @GetMapping("/cache/{articleIdx}")
    public ArticleDto.Response maybeHitCache(@PathVariable Long articleIdx){

        Article article = this.articleServiceFacade.findByIdxMaybeInCache(articleIdx);

        boolean existsInEm = em.contains(article);
        log.info("영속성 관리 상태 : " + existsInEm);

        return ArticleDto.Response.of(article);
    }

    /**
     * lazy loading 확인용 api
     * @param articleIdx
     * @param detailIdx
     */
    @GetMapping("/{articleIdx}/{detailIdx}")
    public void findDetails(@PathVariable Long articleIdx, @PathVariable Long detailIdx){

        Article article = this.articleService.searchOneWithoutTags(articleIdx);
        List<Tag> tags = article.getTags();
        int size = tags.size();
        em.clear(); //혹시 모를 side effect를 막기 위해 clear
        ArticleDetail articleDetail = this.articleService.searchDetailByIdx(detailIdx);

    }

}
