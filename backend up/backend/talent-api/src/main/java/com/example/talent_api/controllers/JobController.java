package com.example.talent_api.controllers;

import java.util.List;
import java.util.Optional;

import com.example.talent_api.models.Application;
import com.example.talent_api.repositories.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.talent_api.models.Job;
import com.example.talent_api.repositories.JobRepository;
import com.example.talent_api.repositories.ManagerRepository;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @GetMapping("/welcome")
    public String index() {
        return "Welcome to Jobs";
    }

    @GetMapping
    public ResponseEntity<List<Job>> getJobs() {
        List<Job> jobs = jobRepository.findAllJobsSortedByDateListed();
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> getJob(@PathVariable int id) {
        Optional<Job> job = jobRepository.findById(id);
        return job.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Job> createJob(@RequestBody Job job) {
        if(!managerRepository.existsById(job.getManagerId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager does not exist");
        }
        jobRepository.save(job);
        return ResponseEntity.ok(job);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Job> updateJob(@PathVariable int id, @RequestBody Job job) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            job.setId(id);
            jobRepository.save(job);
            return ResponseEntity.ok(job);
        }
        return ResponseEntity.notFound().build();
    }

//    @PatchMapping("/{id}")
//    public ResponseEntity<Job> patchJob(@PathVariable int id, @RequestBody Job job) {
//        Optional<Job> jobOptional = jobRepository.findById(id);
//        if (jobOptional.isPresent()) {
//            job.setId(id);
//            jobRepository.save(job);
//            return ResponseEntity.ok(job);
//        }
//
//        return ResponseEntity.notFound().build();
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable int id) {
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            List<Application> applications = applicationRepository.findByJobId(id);
            for (Application application : applications) {
                applicationRepository.deleteById(application.getId());
            }
            jobRepository.deleteById(jobOptional.get().getId());
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<Job>> getJobsByManagerId(@PathVariable int managerId) {
        List<Job> jobs = jobRepository.findByManagerIdOrderByDateListedDesc(managerId);

        return ResponseEntity.ok(jobs);
    }
}
