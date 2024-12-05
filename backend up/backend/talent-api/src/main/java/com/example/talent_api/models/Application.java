package com.example.talent_api.models;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "APPLICATION")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "user_Name")
    private String userName;


    @Column(name = "job_id")
    private Integer jobId;

    @Column(name = "date_applied")
    @CreationTimestamp
    private Timestamp dateApplied;

    @Column(name = "cover_letter")
    private String coverLetter;

    @Column(name = "custom_resume")
    private String customResume;

    @Column(name = "application_status")
    private String applicationStatus;

    // No-argument constructor
    public Application() {
    }

    // Parameterized constructor
    public Application(Integer userId, Integer jobId,String userName, Timestamp dateApplied,
                       String coverLetter, String customResume, String applicationStatus) {
        this.userId = userId;
        this.userName = userName;
        this.jobId = jobId;
        this.dateApplied = dateApplied;
        this.coverLetter = coverLetter;
        this.customResume = customResume;
        this.applicationStatus = applicationStatus;
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

     public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public Integer getJobId() {
        return jobId;
    }

    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }

    public Timestamp getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Timestamp dateApplied) {
        this.dateApplied = dateApplied;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getCustomResume() {
        return customResume;
    }

    public void setCustomResume(String customResume) {
        this.customResume = customResume;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
}
