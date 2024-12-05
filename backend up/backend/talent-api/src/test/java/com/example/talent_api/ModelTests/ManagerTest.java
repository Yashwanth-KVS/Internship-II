package com.example.talent_api.ModelTests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.example.talent_api.models.Manager;

public class ManagerTest {

    private Manager manager;

    @BeforeEach
    public void setUp() {
        // Initialize the Manager object before each test
        manager = new Manager();
    }

    @Test
    public void testNoArgsConstructor() {
        Manager manager = new Manager();
        assertNotNull(manager);
    }

    @Test
    public void testParameterizedConstructor() {
        Manager manager = new Manager(100, "Jane Doe", "jane.doe@example.com", 
                                      "Engineering", "987-654-3210");

        assertEquals(100, manager.getUserId());
        assertEquals("Jane Doe", manager.getFullName());
        assertEquals("jane.doe@example.com", manager.getEmail());
        assertEquals("Engineering", manager.getDepartment());
        assertEquals("987-654-3210", manager.getPhone());
    }

    @Test
    public void testGettersAndSetters() {
        // Test setters
        manager.setId(1);
        manager.setUserId(200);
        manager.setFullName("John Smith");
        manager.setEmail("john.smith@example.com");
        manager.setDepartment("Finance");
        manager.setPhone("123-456-7890");

        // Test getters
        assertEquals(1, manager.getId());
        assertEquals(200, manager.getUserId());
        assertEquals("John Smith", manager.getFullName());
        assertEquals("john.smith@example.com", manager.getEmail());
        assertEquals("Finance", manager.getDepartment());
        assertEquals("123-456-7890", manager.getPhone());
    }

    @Test
    public void testEmailField() {
        manager.setEmail("new.manager@example.com");
        assertEquals("new.manager@example.com", manager.getEmail());
    }

    @Test
    public void testFullNameField() {
        manager.setFullName("New Manager");
        assertEquals("New Manager", manager.getFullName());
    }
}
