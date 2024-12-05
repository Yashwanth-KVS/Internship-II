package com.example.talent_api.ControllerTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.talent_api.controllers.ManagerController;
import com.example.talent_api.models.Manager;
import com.example.talent_api.repositories.ManagerRepository;
import com.example.talent_api.repositories.UserRepository;
import java.util.List;

public class ManagerControllerTest {

    @InjectMocks
    private ManagerController managerController;

    @Mock
    private ManagerRepository managerRepository;

    @Mock
    private UserRepository userRepository;

    private Manager manager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        manager = new Manager();
        manager.setId(1);
        manager.setUserId(1);
    }

    @Test
    public void testGetAllManagers() {
        when(managerRepository.findAll()).thenReturn(Collections.singletonList(manager));

        ResponseEntity<List<Manager>> response = managerController.getAllManagers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(1);
    }

    @Test
    public void testGetManagerById_Found() {
        when(managerRepository.findById(anyInt())).thenReturn(Optional.of(manager));

        ResponseEntity<Manager> response = managerController.getManagerById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(manager);
    }

    @Test
    public void testGetManagerById_NotFound() {
        when(managerRepository.findById(anyInt())).thenReturn(Optional.empty());

        ResponseEntity<Manager> response = managerController.getManagerById(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateManager_Success() {
        when(userRepository.existsById(anyInt())).thenReturn(true);
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        ResponseEntity<Manager> response = managerController.createManager(manager);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(manager);
    }

    @Test
    public void testUpdateManager_NotFound() {
        when(managerRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<Manager> response = managerController.updateManager(1, manager);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testUpdateManager_Success() {
        when(managerRepository.existsById(anyInt())).thenReturn(true);
        when(managerRepository.save(any(Manager.class))).thenReturn(manager);

        ResponseEntity<Manager> response = managerController.updateManager(1, manager);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(manager);
    }

    @Test
    public void testDeleteManager_NotFound() {
        when(managerRepository.existsById(anyInt())).thenReturn(false);

        ResponseEntity<Void> response = managerController.deleteManager(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteManager_Success() {
        when(managerRepository.existsById(anyInt())).thenReturn(true);

        ResponseEntity<Void> response = managerController.deleteManager(1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(managerRepository, times(1)).deleteById(1);
    }
}
