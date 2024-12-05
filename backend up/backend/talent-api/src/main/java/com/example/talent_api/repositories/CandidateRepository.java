package com.example.talent_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.talent_api.models.Candidate;

public interface CandidateRepository extends JpaRepository<Candidate, Integer> {
    Optional<Candidate> findByUserId(Integer userId);
}
