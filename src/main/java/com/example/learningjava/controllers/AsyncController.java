package com.example.learningjava.controllers;

import com.example.learningjava.service.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class AsyncController {

    @Autowired
    private AsyncService asyncService;

    @GetMapping("/async-task")
    public CompletableFuture<String> triggerAsyncTask() {
        return asyncService.performAsyncTask();
    }
}
