package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="article_idx", nullable = false)
    @BatchSize(size = 20)
    private Article article;

    private String tag;

    @Builder
    public Tag(String tag) {
        this.tag = tag;
    }
}
