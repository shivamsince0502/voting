package com.example.voting.controller;

import com.example.voting.config.AsyncConfing;
import com.example.voting.entity.User;
import com.example.voting.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private AsyncConfing asyncConfing;

    @GetMapping("/user")
    public List<User> getUser() {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) asyncConfing.taskExecutor();
        try {
            CompletableFuture<List<User>> future = CompletableFuture.supplyAsync(() -> {
                logger.info("Thread name: " + Thread.currentThread().getName());
                return userService.getUserList();
            }, taskExecutor);

            logger.info("Thread name: " + Thread.currentThread().getName());
            return future.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving user list", e);
        }
    }

    @GetMapping("/curr-user")
    public String getCurrUsername(Principal principal) {
        ThreadPoolTaskExecutor taskExecutor = (ThreadPoolTaskExecutor) asyncConfing.taskExecutor();
        try {
            CompletableFuture<String> future = CompletableFuture.supplyAsync(principal::getName, taskExecutor);

            logger.info("Thread name: " + Thread.currentThread().getName());
            return future.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving current username", e);
        }
    }
}
