package com.example.talent_api.controllers;

import java.util.List;
import java.util.Optional;

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

import com.example.talent_api.models.Application;
import com.example.talent_api.models.Candidate;
import com.example.talent_api.repositories.ApplicationRepository;
import com.example.talent_api.repositories.CandidateRepository;
import com.example.talent_api.repositories.JobRepository;
import com.example.talent_api.repositories.UserRepository;

@RestController
@RequestMapping("/applications")
public class ApplicationController {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public ResponseEntity<List<Application>> getAllApplications() {
        List<Application> applications = applicationRepository.findAll();
        return ResponseEntity.ok(applications);
    }

    // Get application by id
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplicationById(@PathVariable("id") Integer id) {
        Optional<Application> application = applicationRepository.findById(id);
        return application.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a application
    // @PostMapping
    // public ResponseEntity<Application> createApplication(@RequestBody Application
    // application) {
    // if(!userRepository.existsById(application.getUserId()) ||
    // !jobRepository.existsById(application.getJobId())) {
    // throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Job does not
    // exist");
    // }
    // Application savedApplication = applicationRepository.save(application);
    // return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    // }

    @PostMapping
    public ResponseEntity<Application> createApplication(@RequestBody Application application) {
        // Check if the user and job exist
        if (!userRepository.existsById(application.getUserId()) || !jobRepository.existsById(application.getJobId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or Job does not exist");
        }

        // Check if an application already exists for the same user and job
        List<Application> existingApplications = applicationRepository.findByUserIdAndJobId(application.getUserId(),
                application.getJobId());
        if (!existingApplications.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Application already exists for this job");
        }

        // Save the new application
        Application savedApplication = applicationRepository.save(application);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedApplication);
    }

    // Update an application by id
    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(
            @PathVariable("id") Integer id,
            @RequestBody Application updatedApplication) {
        Optional<Application> existingApplicationOptional = applicationRepository.findById(id);

        if (existingApplicationOptional.isPresent()) {
            Application existingApplication = existingApplicationOptional.get();

            // Update the fields of the existing application with the new data
            existingApplication.setUserId(updatedApplication.getUserId());
            existingApplication.setJobId(updatedApplication.getJobId());
            existingApplication.setDateApplied(updatedApplication.getDateApplied());
            existingApplication.setCoverLetter(updatedApplication.getCoverLetter());
            existingApplication.setCustomResume(updatedApplication.getCustomResume());
            existingApplication.setApplicationStatus(updatedApplication.getApplicationStatus());

            // Save the updated application
            Application savedApplication = applicationRepository.save(existingApplication);
            return ResponseEntity.ok(savedApplication);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete application by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("id") Integer id) {
        if (applicationRepository.existsById(id)) {
            applicationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<Application>> getApplicationsByManagerId(
            @PathVariable int candidateId) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(candidateId);
        if (optionalCandidate.isPresent()) {
            Candidate candidate = optionalCandidate.get();
            List<Application> applications = applicationRepository.findByUserId(candidate.getUserId());

            return ResponseEntity.ok(applications);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsByJobId(
            @PathVariable int jobId) {
        List<Application> applications = applicationRepository.findByJobId(jobId);

        return ResponseEntity.ok(applications);
    }
    @PutMapping("/{id}/approve")
public Application updateapproveStatus(@PathVariable Integer id) {
    try {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setApplicationStatus("Approved");
            return applicationRepository.save(application);
        } else {
            throw new RuntimeException("Application not found");
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to update status", e);
    }
}

@PutMapping("/{id}/deny")
public Application updatedenyStatus(@PathVariable Integer id) {
    try {
        Optional<Application> applicationOptional = applicationRepository.findById(id);
        if (applicationOptional.isPresent()) {
            Application application = applicationOptional.get();
            application.setApplicationStatus("Denied");
            return applicationRepository.save(application);
        } else {
            throw new RuntimeException("Application not found");
        }
    } catch (Exception e) {
        throw new RuntimeException("Failed to update status", e);
    }
}


}
