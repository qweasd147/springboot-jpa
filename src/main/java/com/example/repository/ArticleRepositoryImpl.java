package com.example.repository;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

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
}
