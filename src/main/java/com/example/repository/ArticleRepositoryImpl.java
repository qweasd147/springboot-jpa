package com.example.repository;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.example.model.ArticleInfo;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.model.QArticle.article;
import static com.example.model.QArticleInfo.articleInfo;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

public class ArticleRepositoryImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    public ArticleRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Article.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<Article> findAllByDto(ArticleDto.ListRequest listRequest) {

        Pageable pageable = listRequest.toPageRequest();
        JPQLQuery<Article> query = from(article);
                //.where();

        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();
        long totalCount = query.fetchCount();

        return new PageImpl<>(articles, pageable, totalCount);
    }

    @Override
    public Article incrementCountByRepository(Long articleIdx){

        Map<String, Object> properties = new HashMap<>();

        properties.put("javax.persistence.lock.timeout", 10000);

        Article articleOne = getEntityManager()
                .find(Article.class, articleIdx, LockModeType.PESSIMISTIC_WRITE, properties);

        getEntityManager().lock(articleOne, LockModeType.PESSIMISTIC_WRITE);

        articleOne.incrementCount();

        return articleOne;
    }

    @Override
    public ArticleDto.WithArticleInfo findByIdxWithArticleInfo(Long articleIdx) {

        Map<Article, List<ArticleInfo>> articleListMap = getQuerydsl().createQuery()
                .from(article)
                .innerJoin(articleInfo)
                    .on(article.idx.eq(articleInfo.article.idx))
                .where(article.idx.eq(articleIdx))
                //.transform(groupBy(article).as(articleInfo))
                .transform(groupBy(article).as(list(articleInfo)));


        return articleListMap.entrySet().stream()
                .map(entry-> new ArticleDto.WithArticleInfo(entry.getKey(), entry.getValue()))
                .findFirst()
                .orElse(null);
    }
}
