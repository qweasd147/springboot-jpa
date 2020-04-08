package com.example.repository;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.model.QArticle.article;

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
    @Transactional
    public Article incrementBoardCount(Long boardIdx){

        Map<String, Object> properties = new HashMap<>();

        properties.put("javax.persistence.lock.timeout", 10000);

        Article articleOne = getEntityManager()
                .find(Article.class, boardIdx, LockModeType.PESSIMISTIC_WRITE, properties);

        getEntityManager().lock(articleOne, LockModeType.PESSIMISTIC_WRITE);

        articleOne.incrementCount();

        return articleOne;
    }
}
