package com.example.repository;

import com.example.model.Article;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long>, ArticleRepositoryCustom {

    @Query("SELECT article FROM Article article JOIN article.tags tags WHERE tags.tag IN :tags")
    List<Article> findBySeveralTags(@Param("tags") List<String> tags);

    @Modifying
    @Query("UPDATE Article article SET article.subject = :subject, article.contents = :contents")
    void updateByQuery(@Param("subject") String subject
            , @Param("contents") String name);

    @Query("SELECT article FROM Article article WHERE article.idx = :articleIdx")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Article findArticleOneWithLock(@Param("articleIdx")Long articleIdx);

    @Query("SELECT article FROM Article article WHERE article.idx = :articleIdx")
    @EntityGraph(attributePaths = "tags")
    Optional<Article> findArticleOneWithTag(@Param("articleIdx")Long articleIdx);
}
