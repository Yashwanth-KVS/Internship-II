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

import com.example.talent_api.models.Manager;
import com.example.talent_api.repositories.ManagerRepository;
import com.example.talent_api.repositories.UserRepository;

@RestController
@RequestMapping("/managers")
public class ManagerController {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private UserRepository userRepository;

    // Get list of all managers
    @GetMapping
    public ResponseEntity<List<Manager>> getAllManagers() {
        List<Manager> managers = managerRepository.findAll();
        return ResponseEntity.ok(managers);
    }

    // Get a single manager by ID
    @GetMapping("/{id}")
    public ResponseEntity<Manager> getManagerById(@PathVariable int id) {
        Optional<Manager> manager = managerRepository.findById(id);
        return manager.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new manager
    @PostMapping
    public ResponseEntity<Manager> createManager(@RequestBody Manager manager) {
        if(!userRepository.existsById(manager.getUserId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
        Manager savedManager = managerRepository.save(manager);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedManager);
    }

    // Update an existing manager
    @PutMapping("/{id}")
    public ResponseEntity<Manager> updateManager(@PathVariable int id, @RequestBody Manager manager) {
        if (!managerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        manager.setId(id);
        Manager updatedManager = managerRepository.save(manager);
        return ResponseEntity.ok(updatedManager);
    }

    // Delete a manager by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteManager(@PathVariable int id) {
        if (!managerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        managerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
