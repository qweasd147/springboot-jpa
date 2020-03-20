package com.example.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@NoArgsConstructor
@Getter
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column(nullable = false)
    @NotNull
    private String subject;

    @Column(nullable = false)
    @NotNull
    @Lob
    private String contents;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "article_idx")
    @OrderBy("idx ASC ")
    @BatchSize(size = 20)
    private List<Tag> tags = new ArrayList<>();

    @Builder
    public Article(String subject, String contents, List<Tag> tags) {
        this.subject = subject;
        this.contents = contents;
        this.tags = tags;
    }

    public void addTag(String tag){

        if(this.tags == null)   this.tags = new ArrayList<>();

        if(isPassValidTag(tag)){

            Tag tagObj = Tag.builder()
                    .article(this)
                    .tag(tag.trim())
                    .build();

            this.tags.add(tagObj);
        }
    }

    public boolean isPassValidTag(String tag){
        if(tag == null) return false;
        String trimTag = tag.trim();

        if(StringUtils.isEmpty(trimTag))    return false;
        if(this.tags.contains(trimTag)) return false;

        return true;
    }

    public void initTags(List<String> tags){

        if(this.tags == null)   this.tags = new ArrayList<>();

        if(tags != null){
            tags.stream()
                    .filter(tag-> isPassValidTag(tag))
                    .map(String::trim)
                    .map(tagStr -> Tag.builder()
                            .article(this)
                            .tag(tagStr)
                            .build())
                    .forEach((tagObj)-> this.tags.add(tagObj));
        }
    }

    public void initTags(Set<String> tags){

        if(this.tags == null)   this.tags = new ArrayList<>();

        if(tags != null){
            List<Tag> newTagList = tags.stream()
                    .filter(tag -> isPassValidTag(tag))
                    .map(String::trim)
                    .map(tagStr -> Tag.builder()
                            .article(this)
                            .tag(tagStr)
                            .build())
                    .collect(Collectors.toList());

            this.tags = newTagList;
        }
    }

    public Article updateContents(String subject, String contents) {
        this.subject = subject;
        this.contents = contents;

        return this;
    }
}
