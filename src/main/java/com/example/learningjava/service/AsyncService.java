package com.example.learningjava.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    @Async
    public CompletableFuture<String> performAsyncTask() {
        try {
            // Simulate a long-running task
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture("Task Completed");
    }
}
