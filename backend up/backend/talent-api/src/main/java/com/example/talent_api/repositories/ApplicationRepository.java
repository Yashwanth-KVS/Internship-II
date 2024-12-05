package com.example.talent_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.talent_api.models.Application;

public interface ApplicationRepository extends JpaRepository<Application, Integer>{
    List<Application> findByJobId(int jobId);

    List<Application> findByUserId(int userId);

    List<Application> findByUserIdAndJobId(Integer userId, Integer jobId);



}
