package com.example.talent_api.ControllerTests;

import static org.assertj.core.api.Assertions.assertThat;
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

import com.example.talent_api.controllers.JobController;
import com.example.talent_api.models.Application;
import com.example.talent_api.models.Job;
import com.example.talent_api.repositories.ApplicationRepository;
import com.example.talent_api.repositories.JobRepository;
import com.example.talent_api.repositories.ManagerRepository;
import java.util.List;

public class JobControllerTest {

    @InjectMocks
    private JobController jobController;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    private Job job;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        job = new Job();
        job.setId(1);
        job.setManagerId(1);
    }

    @Test
    public void testIndex() {
        String response = jobController.index();
        assertThat(response).isEqualTo("Welcome to Jobs");
    }

    @Test
    public void testGetJobs() {
        when(jobRepository.findAll()).thenReturn(Collections.singletonList(job));

        ResponseEntity<List<Job>> response = jobController.getJobs();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void testGetJob_Found() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.of(job));

        ResponseEntity<Job> response = jobController.getJob(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(job);
    }

    @Test
    public void testGetJob_NotFound() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.getJob(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateJob_Success() {
        when(managerRepository.existsById(anyInt())).thenReturn(true);
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        ResponseEntity<Job> response = jobController.createJob(job);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(job);
    }

    @Test
    public void testUpdateJob_Found() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.of(job));
        when(jobRepository.save(any(Job.class))).thenReturn(job);

        ResponseEntity<Job> response = jobController.updateJob(1, job);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(job);
    }

    @Test
    public void testUpdateJob_NotFound() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Job> response = jobController.updateJob(1, job);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteJob_Found() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.of(job));
        when(applicationRepository.findByJobId(anyInt())).thenReturn(Collections.singletonList(new Application()));

        ResponseEntity<Void> response = jobController.deleteJob(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(jobRepository, times(1)).deleteById(anyInt());
    }

    @Test
    public void testDeleteJob_NotFound() {
        when(jobRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Void> response = jobController.deleteJob(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetJobsByManagerId() {
        when(jobRepository.findByManagerId(anyInt())).thenReturn(Collections.singletonList(job));

        ResponseEntity<List<Job>> response = jobController.getJobsByManagerId(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }
}
