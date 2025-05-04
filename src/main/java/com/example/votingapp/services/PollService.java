package com.example.votingapp.services;

import com.example.votingapp.model.OptionVote;
import com.example.votingapp.model.Poll;
import com.example.votingapp.repositories.PollRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PollService {
    private final PollRepository pollRepository;

    public PollService(PollRepository pollRepository) {
        this.pollRepository = pollRepository;
    }

    public Poll createPoll(Poll poll) {
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public ResponseEntity<Poll> getPollById(Long id) {
        return pollRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    public void vote(Long pollId, int optionIndex) {
        // get poll
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(()->new RuntimeException("Poll not found"));

        // get all options
        List<OptionVote> options = poll.getOptions();

        // if index for vote is not valid
        if (optionIndex < 0 || optionIndex >= options.size()) {
            throw new IllegalArgumentException("Invalid option index");
        }

        // get selected option
        OptionVote selectedOption = options.get(optionIndex);

        // increment selected option vote count
        selectedOption.setVoteCount(selectedOption.getVoteCount() + 1);

        // save to DB
        pollRepository.save(poll);
    }
}
