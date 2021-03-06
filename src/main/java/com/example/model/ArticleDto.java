package com.example.model;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ArticleDto {

    @Getter
    @NoArgsConstructor
    public static class CreateReq {

        @NotBlank(message = "제목 필수 입력")
        private String subject;

        @NotBlank(message = "내용 필수 입력")
        private String contents;
        private int count = 0;
        private Set<String> tags = new LinkedHashSet<>();

        @Builder
        public CreateReq(String subject, String contents, int count, Set<String> tags) {
            this.subject = subject;
            this.contents = contents;
            this.count = count;
            this.tags = tags;
        }

        public Article toEntity(){

            Article article = Article.builder()
                    .subject(this.subject)
                    .contents(this.contents)
                    .count(this.count)
                    .build();

            if(tags != null)
                article.initTags(tags);

            return article;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ModifyRequest {

        private String subject;
        private String contents;
        private int count = 0;
        private Set<String> tags = new LinkedHashSet<>();

        @Builder
        public ModifyRequest(String subject, String contents, int count, Set<String> tags) {
            this.subject = subject;
            this.contents = contents;
            this.count = count;
            this.tags = tags;
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Response {

        private Long idx;
        private String subject;
        private String contents;
        private int count = 0;
        private List<String> tags;

        @Builder
        public Response(Long idx, String subject, String contents, int count, List<String> tags) {
            this.idx = idx;
            this.subject = subject;
            this.contents = contents;
            this.count = count;
            this.tags = tags;
        }

        public static Response of(Article article){

            List<String> tagList = article.getTags().stream()
                    .map(Tag::getTag)
                    .collect(Collectors.toList());

            return Response.builder()
                    .idx(article.getIdx())
                    .subject(article.getSubject())
                    .contents(article.getContents())
                    .count(article.getCount())
                    .tags(tagList)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class ListRequest {

        private static final int MAX_SIZE = 40;

        private int page = 0;
        private int size = 16;
        private String sort;
        private Set<String> tags;

        public ListRequest(int page, int size, String sort, Set<String> tags) {
            this.page = page;
            this.size = size;
            this.sort = sort;
            this.tags = tags;
        }

        public Pageable toPageRequest(){

            if(size > MAX_SIZE)
                size = MAX_SIZE;

            return PageRequest.of(this.page, this.size, Sort.Direction.DESC, "idx");
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class WithArticleInfo {

        private final Article article;
        private final List<ArticleInfo> articleInfos;
    }

    @Getter
    @RequiredArgsConstructor
    public static class WithArticleDetails {

        private final Article article;
        private final ArticleDetail articleInfos;
    }
}
