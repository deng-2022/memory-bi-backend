package com.yupi.springbootinit.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 */
@Configuration
public class ThreadPoolExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            // 记录线程数
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                count++;
                return thread;
            }
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor
                (2, 4, 100,
                        TimeUnit.MINUTES, new ArrayBlockingQueue<>(4), threadFactory);
        return threadPoolExecutor;
    }
}
