
package com.example.talent_api.ModelTests;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;

import org.junit.jupiter.api.Test;

import com.example.talent_api.models.Job;

public class JobTest {

    @Test
    public void testNoArgConstructor() {
        Job job = new Job();

        assertThat(job.getId()).isEqualTo(0);
        assertThat(job.getManagerId()).isNull();
        assertThat(job.getDepartment()).isNull();
        assertThat(job.getListingTitle()).isNull();
        assertThat(job.getDateListed()).isNull();
        assertThat(job.getDateClosed()).isNull();
        assertThat(job.getJobTitle()).isNull();
        assertThat(job.getJobDescription()).isNull();
        assertThat(job.getAdditionalInformation()).isNull();
        assertThat(job.getListingStatus()).isNull();
    }

    @Test
    public void testParameterizedConstructor() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Job job = new Job(1, 1, "IT", "Senior Developer", now, now, "Developer", "Job description", "Additional info",
                "Open");

        assertThat(job.getId()).isEqualTo(1);
        assertThat(job.getManagerId()).isEqualTo(1);
        assertThat(job.getDepartment()).isEqualTo("IT");
        assertThat(job.getListingTitle()).isEqualTo("Senior Developer");
        assertThat(job.getDateListed()).isEqualTo(now);
        assertThat(job.getDateClosed()).isEqualTo(now);
        assertThat(job.getJobTitle()).isEqualTo("Developer");
        assertThat(job.getJobDescription()).isEqualTo("Job description");
        assertThat(job.getAdditionalInformation()).isEqualTo("Additional info");
        assertThat(job.getListingStatus()).isEqualTo("Open");
    }

    @Test
    public void testSettersAndGetters() {
        Job job = new Job();
        Timestamp now = new Timestamp(System.currentTimeMillis());

        job.setId(1);
        job.setManagerId(2);
        job.setDepartment("HR");
        job.setListingTitle("Recruiter");
        job.setDateListed(now);
        job.setDateClosed(now);
        job.setJobTitle("Recruiter");
        job.setJobDescription("Job description");
        job.setAdditionalInformation("Additional info");
        job.setListingStatus("Closed");

        assertThat(job.getId()).isEqualTo(1);
        assertThat(job.getManagerId()).isEqualTo(2);
        assertThat(job.getDepartment()).isEqualTo("HR");
        assertThat(job.getListingTitle()).isEqualTo("Recruiter");
        assertThat(job.getDateListed()).isEqualTo(now);
        assertThat(job.getDateClosed()).isEqualTo(now);
        assertThat(job.getJobTitle()).isEqualTo("Recruiter");
        assertThat(job.getJobDescription()).isEqualTo("Job description");
        assertThat(job.getAdditionalInformation()).isEqualTo("Additional info");
        assertThat(job.getListingStatus()).isEqualTo("Closed");
    }
}
