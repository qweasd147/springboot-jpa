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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    @DisplayName("lazy 타입 initialize 사용 하여 초기화 테스트")
    public void lazyLoadInitTest(){

        Article articleWithTags = articleService.searchOneWithTags(1L);

        boolean isLoaded = em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(articleWithTags, "tags");

        assertTrue(isLoaded);
    }

    @Test
    @DisplayName("lazy 타입 initialize 사용x 기본 테스트")
    public void lazyLoadingDefault(){

        Article articleWithoutTags = articleService.searchOne(1L);

        boolean isLoaded = em.getEntityManagerFactory()
                .getPersistenceUnitUtil()
                .isLoaded(articleWithoutTags, "tags");


        assertFalse(isLoaded);
    }
}
