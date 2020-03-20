package com.example.article;

import com.example.model.Article;
import com.example.repository.ArticleRepository;
import com.example.service.ArticleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnitUtil;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class DomainTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleService articleService;

    @PersistenceContext
    private EntityManager em;

    @Test
    @DisplayName("lazy loading 타입 제대로 로딩 되었나 테스트")
    public void lazyLoadTest(){

        Article article = articleService.searchOneWithTags(1L);

        boolean isLoaded = em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(article, "tags");
    }
}
