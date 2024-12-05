package com.example.talent_api.ModelTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Timestamp;
import com.example.talent_api.models.Application;

public class ApplicationTest {

    private Application application;

    @BeforeEach
    public void setUp() {
        // Initialize the Application object before each test
        application = new Application();
    }

    @Test
    public void testNoArgsConstructor() {
        Application app = new Application();
        assertNotNull(app);
    }

    @Test
    public void testParameterizedConstructor() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Application app = new Application(1, 2, now, "Cover Letter", "Custom Resume", "Pending");

        assertEquals(1, app.getUserId());
        assertEquals(2, app.getJobId());
        assertEquals(now, app.getDateApplied());
        assertEquals("Cover Letter", app.getCoverLetter());
        assertEquals("Custom Resume", app.getCustomResume());
        assertEquals("Pending", app.getApplicationStatus());
    }

    @Test
    public void testGettersAndSetters() {
        // Test setters
        application.setId(1);
        application.setUserId(100);
        application.setJobId(200);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        application.setDateApplied(now);
        application.setCoverLetter("Test Cover Letter");
        application.setCustomResume("Test Resume");
        application.setApplicationStatus("Accepted");

        // Test getters
        assertEquals(1, application.getId());
        assertEquals(100, application.getUserId());
        assertEquals(200, application.getJobId());
        assertEquals(now, application.getDateApplied());
        assertEquals("Test Cover Letter", application.getCoverLetter());
        assertEquals("Test Resume", application.getCustomResume());
        assertEquals("Accepted", application.getApplicationStatus());
    }

    @Test
    public void testApplicationStatus() {
        application.setApplicationStatus("Rejected");
        assertEquals("Rejected", application.getApplicationStatus());
    }

    @Test
    public void testSetDateApplied() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        application.setDateApplied(now);
        assertEquals(now, application.getDateApplied());
    }
}
