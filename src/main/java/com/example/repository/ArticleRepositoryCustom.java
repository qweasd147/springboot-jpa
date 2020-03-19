package com.example.repository;

import com.example.model.Article;
import com.example.model.ArticleDto;
import org.springframework.data.domain.Page;

public interface ArticleRepositoryCustom {

    Page<Article> findAllByDto(ArticleDto.ListRequest listRequest);
}
