package com.example.repository;

import com.example.model.ArticleDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ArticleJDBCRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<ArticleDto.CreateReq> createReqList){

        int[][] batchResult = jdbcTemplate.batchUpdate(
                "insert into article(idx, subject, contents, `count`) values (null, ?, ?, 0);",
                createReqList,
                50,
                (ps, argument) -> {
                    ps.setString(1, argument.getSubject());
                    ps.setString(2, argument.getContents());
                });

    }
}
