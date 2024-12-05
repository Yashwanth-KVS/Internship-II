package com.example.talent_api.ControllerTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.example.talent_api.controllers.UserController;
import com.example.talent_api.models.User;

import com.example.talent_api.repositories.UserRepository;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User(1, "Tuan Nguyen", "password", "GPT");
        // testUser.setId(1);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User(1, "user1", "password1", "type1");
        user1.setId(1);
        User user2 = new User(2, "user2", "password2", "type2");
        user2.setId(2);
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andExpect(jsonPath("$[1].username").value("user2"));

        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testGetUserById() throws Exception {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("Tuan Nguyen"));
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateUser() throws Exception {
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Tuan Nguyen\",\"password\":\"password\",\"type\":\"GPT\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("Tuan Nguyen"));
    }

    @Test
    public void testUpdateUser() throws Exception {
        User updatedUser = new User(1, "Teezzy Nguyen", "password", "GPT");
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Teezzy Nguyen\",\"password\":\"password\",\"type\":\"GPT\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("Teezzy Nguyen"))
                .andExpect(jsonPath("$.password").value("password"))
                .andExpect(jsonPath("$.type").value("GPT"));

        verify(userRepository).findById(1);
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testUpdateUserNotFound() throws Exception {
        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"Tuan Nguyen\",\"password\":\"password\",\"type\":\"GPT\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        when(userRepository.existsById(1)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {
        when(userRepository.existsById(1)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}
