package com.example.voting.services;

import com.example.voting.entity.Vote;
import com.example.voting.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class VoteService {
    @Autowired
    private VoteRepository voteRepository;

    public List<Vote> getAllVoteOfUser(String username) {
        return voteRepository.findAll().stream().filter(vote -> vote.getFromId().equals(username)).toList();

    }

    public int getNoVoteOFUser(String username) {
        return getAllVoteOfUser(username).size();
    }

    public Vote voteNominee(Vote vote) {
        return voteRepository.save(vote);
    }

}
