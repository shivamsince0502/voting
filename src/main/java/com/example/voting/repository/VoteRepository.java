package com.example.voting.repository;

import com.example.voting.entity.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Integer> {
    public Optional<Vote> findByFromId(String fromUsername);

}
