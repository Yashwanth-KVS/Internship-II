package com.example.talent_api.ControllerTests;

import com.example.talent_api.models.Candidate;
import com.example.talent_api.repositories.CandidateRepository;
import com.example.talent_api.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import com.example.talent_api.controllers.*;

class CandidateControllerTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CandidateController candidateController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCandidates_ReturnsCandidatesList() {
        List<Candidate> candidates = Arrays.asList(new Candidate(1, 1, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf"));
        when(candidateRepository.findAll()).thenReturn(candidates);

        ResponseEntity<List<Candidate>> response = candidateController.getAllCandidates();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllCandidates_NoContent() {
        when(candidateRepository.findAll()).thenReturn(List.of());

        ResponseEntity<List<Candidate>> response = candidateController.getAllCandidates();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testGetCandidateById_ReturnsCandidate() {
        Candidate candidate = new Candidate(1, 1, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf");
        when(candidateRepository.findById(1)).thenReturn(Optional.of(candidate));

        ResponseEntity<Candidate> response = candidateController.getCandidateById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
    }

    @Test
    void testGetCandidateById_NotFound() {
        when(candidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Candidate> response = candidateController.getCandidateById(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testCreateCandidate_UserExists() {
        Candidate candidate = new Candidate(1, 1, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf");
        when(userRepository.existsById(candidate.getUserId())).thenReturn(true);
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidateController.createCandidate(candidate);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(candidate, response.getBody());
    }

    @Test
    void testCreateCandidate_UserDoesNotExist() {
        Candidate candidate = new Candidate(1, 999, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf");
        when(userRepository.existsById(candidate.getUserId())).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> candidateController.createCandidate(candidate));
        //assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User does not exist", exception.getReason());
    }

    @Test
    void testUpdateCandidate_CandidateExists() {
        Candidate candidate = new Candidate(1, 1, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf");
        when(candidateRepository.existsById(candidate.getId())).thenReturn(true);
        when(candidateRepository.save(candidate)).thenReturn(candidate);

        ResponseEntity<Candidate> response = candidateController.updateCandidate(1, candidate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(candidate, response.getBody());
    }

    @Test
    void testUpdateCandidate_CandidateNotFound() {
        Candidate candidate = new Candidate(1, 1, "John Doe", "john@example.com", "Address", "1234567890", "resume.pdf");
        when(candidateRepository.existsById(candidate.getId())).thenReturn(false);

        ResponseEntity<Candidate> response = candidateController.updateCandidate(1, candidate);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteCandidate_CandidateExists() {
        when(candidateRepository.existsById(1)).thenReturn(true);

        ResponseEntity<Void> response = candidateController.deleteCandidate(1);

        verify(candidateRepository, times(1)).deleteById(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteCandidate_CandidateNotFound() {
        when(candidateRepository.existsById(1)).thenReturn(false);

        ResponseEntity<Void> response = candidateController.deleteCandidate(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
