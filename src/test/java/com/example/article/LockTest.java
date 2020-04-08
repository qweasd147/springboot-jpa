package com.example.article;

import com.example.ThreadTestUtils;
import com.example.model.Article;
import com.example.service.ArticleService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.StopWatch;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.number.OrderingComparison.comparesEqualTo;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@DisplayName("비관적 LOCK 테스트")
@Slf4j
public class LockTest {

    @Autowired
    private ArticleService articleService;

    @PersistenceContext
    private EntityManager em;

    private final static int LOCK_NUM_THREADS = ThreadTestUtils.NUM_THREADS;
    private final static int LOCK_NUM_ITERATIONS = ThreadTestUtils.NUM_ITERATIONS;


    @Nested
    @DisplayName("annotation으로 비관적 Lock (PESSIMISTIC_WRITE)을 걸었을 때")
    class WriteLockByAnnotation {

        @Test
        @DisplayName("실행 횟수랑 count 증가 횟수가 같다.")
        public void lockByAnnotationTest() throws InterruptedException {

            long targetArticleIdx = 1L;

            Runnable runnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                    .parallel()
                    .forEach((ignore)-> articleService.incrementCountWithLock(targetArticleIdx));
            };

            boolean isSuccessFull = ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore)-> runnable);

            em.clear();

            Article article = articleService.searchOne(targetArticleIdx);

            assertThat(article.getCount(), comparesEqualTo(LOCK_NUM_THREADS * LOCK_NUM_ITERATIONS));
            assertTrue(isSuccessFull);
        }
    }

    @Nested
    @DisplayName("repository에서 비관적 Lock (PESSIMISTIC_WRITE)을 걸었을 때")
    class WriteLockByRepository {


        @Test
        @DisplayName("실행 횟수랑 count 증가 횟수가 같다.")
        public void lockByRepositoryTest() throws InterruptedException {

            long targetArticleIdx = 2L;

            Runnable runnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                        .parallel()
                        .forEach((ignore)-> articleService.IncrementCountFromRepository(targetArticleIdx));
            };

            boolean isSuccessFull = ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore)-> runnable);

            em.clear();

            Article article = articleService.searchOne(targetArticleIdx);

            assertThat(article.getCount(), comparesEqualTo(LOCK_NUM_THREADS * LOCK_NUM_ITERATIONS));
            assertTrue(isSuccessFull);
        }
    }

    @Nested
    @DisplayName("Lock을 따로 걸지 않았을 때")
    class WithoutLock {

        @Test
        @DisplayName("실행 횟수랑 count 증가 횟수가 같지 않다.")
        public void withoutLockTest() throws InterruptedException {

            long targetArticleIdx = 3L;

            Runnable runnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                        .parallel()
                        .forEach((ignore)-> articleService.incrementCountWithoutLock(targetArticleIdx));
            };

            boolean isSuccessFull = ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore) -> runnable);

            em.clear();

            Article article = articleService.searchOne(targetArticleIdx);

            assertThat(article.getCount(), not(comparesEqualTo(LOCK_NUM_THREADS * LOCK_NUM_ITERATIONS)));
            assertTrue(isSuccessFull);
            log.info("count : {}", article.getCount());
        }
    }

    @Nested
    @DisplayName("성능 테스트")
    class CheckTime {

        @Test
        @DisplayName("성능 지표 보기")
        public void checkTime() throws InterruptedException {

            long repositoryLockIdx = 4L;
            long serviceLockIdx = 5L;
            long nonLockIdx = 6L;

            Runnable repositoryLockRunnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                        .parallel()
                        .forEach((ignore)-> articleService.IncrementCountFromRepository(repositoryLockIdx));
            };

            Runnable serviceLockRunnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                        .parallel()
                        .forEach((ignore)-> articleService.incrementCountWithLock(serviceLockIdx));
            };

            Runnable nonLockRunnable = ()->{
                IntStream.range(0, LOCK_NUM_ITERATIONS)
                        .parallel()
                        .forEach((ignore)-> articleService.incrementCountWithoutLock(nonLockIdx));
            };

            StopWatch stopWatch = new StopWatch();

            //TODO : 먼저 실행하는게 더 오래걸린다... 왜그런지 모르겠다.

            stopWatch.start("nonLockRunnable");
            ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore) -> nonLockRunnable);
            stopWatch.stop();

            stopWatch.start("serviceLockRunnable");
            ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore) -> serviceLockRunnable);
            stopWatch.stop();

            stopWatch.start("repositoryLock");
            ThreadTestUtils.run(LOCK_NUM_THREADS, (ignore) -> repositoryLockRunnable);
            stopWatch.stop();

            log.info(stopWatch.prettyPrint());
        }

    }
}
