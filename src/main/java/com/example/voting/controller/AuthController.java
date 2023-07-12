package com.example.voting.controller;

import com.example.voting.Exceptions.BadRequestException;
import com.example.voting.Exceptions.DatabaseException;
import com.example.voting.config.AsyncConfing;
import com.example.voting.entity.Vote;
import com.example.voting.model.JwtRequest;
import com.example.voting.model.JwtResponse;
import com.example.voting.entity.User;
import com.example.voting.security.JwtHelper;
import com.example.voting.services.UserService;
import com.example.voting.services.VoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private AsyncConfing asyncConfing;

    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        logger.info("login api called.");
        logger.info("Request: " + request);
        this.doAuthenticate(request.getUsername(), request.getPassword());
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        logger.info("user Details: " + userDetails);
        String token = this.helper.generateToken(userDetails);
        logger.info("Generated Token: " + token);

        JwtResponse response = JwtResponse.builder()
                .jwtToken(token)
                .username(userDetails.getUsername()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void doAuthenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, password);
        logger.info("authentication ticket: " + authentication);
        try {
            logger.info("Thread name: " + Thread.currentThread().getName());
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid Username or Password!!");
        } finally {
            logger.info("Authentication process done.");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public String exceptionHandler() {
        return "Credentials Invalid!!";
    }

    @PostMapping("/create-user")
    public User createUser(@RequestBody User user) {
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfing.taskExecutor();
        CompletableFuture<User> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Thread name: " + Thread.currentThread().getName());
            return userService.createUser(user);
        }, executor);
        try {
            logger.info("Thread name: " + Thread.currentThread().getName());

            return future.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving current username", e);
        }
    }

    @PostMapping("/vote")
    public Vote voteNominee(@RequestBody Vote vote) {
        if (vote == null) {
            throw new BadRequestException("400", "Invalid username");
        }
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfing.taskExecutor();
        CompletableFuture<Vote> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Thread name: " + Thread.currentThread().getName());
            return voteService.voteNominee(vote);
        }, executor);
        try {
            logger.info("Thread name: " + Thread.currentThread().getName());
            return future.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving current username", e);
        }
    }

    @PostMapping("/count-votes")
    public Integer getVotes(@RequestBody String username) {
        if (username == null || username.isEmpty()) {
            throw new BadRequestException("400", "Invalid username");
        }
        ThreadPoolTaskExecutor executor = (ThreadPoolTaskExecutor) asyncConfing.taskExecutor();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            logger.info("Thread name: " + Thread.currentThread().getName());
            return voteService.getNoVoteOFUser(username);
        }, executor);
        try {
            logger.info("Thread name: " + Thread.currentThread().getName());
            return future.get();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving current username", e);
        }
    }
}
