package com.example.talent_api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.sql.Timestamp;

@Entity
@Table(name = "JOB")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "manager_id")
    private Integer managerId;

    @Column(name = "department")
    private String department;

    @Column(name = "listing_title")
    private String listingTitle;

    @Column(name = "date_listed")
    private Timestamp dateListed;

    @Column(name = "date_closed")
    private Timestamp dateClosed;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Column(name = "listing_status")
    private String listingStatus;

    // No-argument constructor
    public Job() {
    }

    // Parameterized constructor
    public Job(int id, Integer managerId, String department, String listingTitle, Timestamp dateListed,
            Timestamp dateClosed, String jobTitle, String jobDescription, String additionalInformation,
            String listingStatus) {
        this.id = id;
        this.managerId = managerId;
        this.department = department;
        this.listingTitle = listingTitle;
        this.dateListed = dateListed;
        this.dateClosed = dateClosed;
        this.jobTitle = jobTitle;
        this.jobDescription = jobDescription;
        this.additionalInformation = additionalInformation;
        this.listingStatus = listingStatus;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getListingTitle() {
        return listingTitle;
    }

    public void setListingTitle(String listingTitle) {
        this.listingTitle = listingTitle;
    }

    public Timestamp getDateListed() {
        return dateListed;
    }

    public void setDateListed(Timestamp dateListed) {
        this.dateListed = dateListed;
    }

    public Timestamp getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Timestamp dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public String getListingStatus() {
        return listingStatus;
    }

    public void setListingStatus(String listingStatus) {
        this.listingStatus = listingStatus;
    }

}
