package com.example.talent_api.ModelTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.talent_api.models.Candidate;

public class CandidateTest {

    private Candidate candidate;

    @BeforeEach
    public void setUp() {
        // Initialize the Candidate object before each test
        candidate = new Candidate();
    }

    @Test
    public void testNoArgsConstructor() {
        Candidate candidate = new Candidate();
        assertNotNull(candidate);
    }

    @Test
    public void testParameterizedConstructor() {
        Candidate candidate = new Candidate(1, 2, "John Doe", "john.doe@example.com", 
                                            "123 Main St", "123-456-7890", "resume.pdf");

        assertEquals(1, candidate.getId());
        assertEquals(2, candidate.getUserId());
        assertEquals("John Doe", candidate.getFullName());
        assertEquals("john.doe@example.com", candidate.getEmail());
        assertEquals("123 Main St", candidate.getAddress());
        assertEquals("123-456-7890", candidate.getPhone());
        assertEquals("resume.pdf", candidate.getResume());
    }

    @Test
    public void testGettersAndSetters() {
        // Test setters
        candidate.setId(1);
        candidate.setUserId(100);
        candidate.setFullName("Jane Doe");
        candidate.setEmail("jane.doe@example.com");
        candidate.setAddress("456 Oak St");
        candidate.setPhone("987-654-3210");
        candidate.setResume("resume2.pdf");

        // Test getters
        assertEquals(1, candidate.getId());
        assertEquals(100, candidate.getUserId());
        assertEquals("Jane Doe", candidate.getFullName());
        assertEquals("jane.doe@example.com", candidate.getEmail());
        assertEquals("456 Oak St", candidate.getAddress());
        assertEquals("987-654-3210", candidate.getPhone());
        assertEquals("resume2.pdf", candidate.getResume());
    }

    @Test
    public void testEmailField() {
        candidate.setEmail("new.email@example.com");
        assertEquals("new.email@example.com", candidate.getEmail());
    }

    @Test
    public void testFullNameField() {
        candidate.setFullName("New Name");
        assertEquals("New Name", candidate.getFullName());
    }
}
