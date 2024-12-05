package com.example.talent_api.ModelTests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.example.talent_api.models.User;

public class UserTest {

    @Test
    public void testNoArgConstructor() {
        User user = new User();

        assertThat(user.getId()).isNull();
        assertThat(user.getUsername()).isNull();
        assertThat(user.getPassword()).isNull();
        assertThat(user.getType()).isNull();
    }

    @Test
    public void testParameterizedConstructor() {
        User user = new User(1, "testUser", "password123", "admin");

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("testUser");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getType()).isEqualTo("admin");
    }

    @Test
    public void testSettersAndGetters() {
        User user = new User();
        
        user.setId(2);
        user.setUsername("newUser");
        user.setPassword("newPassword");
        user.setType("user");

        assertThat(user.getId()).isEqualTo(2);
        assertThat(user.getUsername()).isEqualTo("newUser");
        assertThat(user.getPassword()).isEqualTo("newPassword");
        assertThat(user.getType()).isEqualTo("user");
    }
}

