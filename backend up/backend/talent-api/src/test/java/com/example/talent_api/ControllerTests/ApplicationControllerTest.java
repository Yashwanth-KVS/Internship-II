package com.example.talent_api.ControllerTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.example.talent_api.controllers.ApplicationController;
import com.example.talent_api.models.Application;
import com.example.talent_api.models.Candidate;
import com.example.talent_api.repositories.ApplicationRepository;
import com.example.talent_api.repositories.CandidateRepository;
import com.example.talent_api.repositories.JobRepository;
import com.example.talent_api.repositories.UserRepository;
import java.util.List;

public class ApplicationControllerTest {

    @InjectMocks
    private ApplicationController applicationController;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JobRepository jobRepository;

    private Application application;
    private Candidate candidate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        application = new Application();
        application.setId(1);
        application.setUserId(1);
        application.setJobId(1);

        candidate = new Candidate();
        candidate.setUserId(1);
    }

    @Test
    public void testGetAllApplications() {
        when(applicationRepository.findAll()).thenReturn(Collections.singletonList(application));

        ResponseEntity<List<Application>> response = applicationController.getAllApplications();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void testGetApplicationById_Found() {
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.of(application));

        ResponseEntity<Application> response = applicationController.getApplicationById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(application);
    }

    @Test
    public void testGetApplicationById_NotFound() {
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Application> response = applicationController.getApplicationById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateApplication_Success() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(jobRepository.existsById(anyInt())).thenReturn(true);
        when(applicationRepository.findByUserIdAndJobId(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ResponseEntity<Application> response = applicationController.createApplication(application);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(application);
    }

    @Test
    public void testUpdateApplication_Found() {
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.of(application));
        when(applicationRepository.save(any(Application.class))).thenReturn(application);

        ResponseEntity<Application> response = applicationController.updateApplication(1, application);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(application);
    }

    @Test
    public void testUpdateApplication_NotFound() {
        when(applicationRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Application> response = applicationController.updateApplication(1, application);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteApplication_Found() {
        when(applicationRepository.existsById(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = applicationController.deleteApplication(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(applicationRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testDeleteApplication_NotFound() {
        when(applicationRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = applicationController.deleteApplication(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetApplicationsByCandidateId_Found() {
        when(candidateRepository.findById(anyInt())).thenReturn(Optional.of(candidate));
        when(applicationRepository.findByUserId(anyInt())).thenReturn(Collections.singletonList(application));

        ResponseEntity<List<Application>> response = applicationController.getApplicationsByManagerId(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void testGetApplicationsByCandidateId_NotFound() {
        when(candidateRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<List<Application>> response = applicationController.getApplicationsByManagerId(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetApplicationsByJobId() {
        when(applicationRepository.findByJobId(anyInt())).thenReturn(Collections.singletonList(application));

        ResponseEntity<List<Application>> response = applicationController.getApplicationsByJobId(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
}
