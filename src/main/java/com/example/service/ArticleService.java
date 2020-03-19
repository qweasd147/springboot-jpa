package com.example.service;

import com.example.model.Article;
import com.example.model.ArticleDto;
import com.example.repository.ArticleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Page<Article> searchAll(Pageable pageable){
        return articleRepository.findAll(pageable);
    }

    public Article searchOne(Long articleIdx){
        return articleRepository.findById(articleIdx).orElse(null);
    }

    public Article register(ArticleDto.CreateReq createReq){
        return articleRepository.save(createReq.toEntity());
    }

    public Article modify(Long articleIdx, ArticleDto.ModifyRequest modifyRequest){

        Article article = articleRepository.findById(articleIdx)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 번호" + articleIdx));

        article
            .updateContents(modifyRequest.getSubject(), modifyRequest.getContents())
            .initTags(modifyRequest.getTags());

        return article;
    }

    public void remove(Long articleIdx){
        articleRepository.deleteById(articleIdx);
    }

    public List<Article> searchAllByTags(List<String> tags){
        return articleRepository.findBySeveralTags(tags);
    }
}
