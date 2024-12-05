package com.example.talent_api.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.talent_api.models.Job;
import org.springframework.data.jpa.repository.Query;

public interface JobRepository extends JpaRepository<Job, Integer> {
    public List<Job> findByManagerId(int managerId);

    @Query("SELECT j FROM Job j ORDER BY j.dateListed DESC")
    List<Job> findAllJobsSortedByDateListed();
    List<Job> findByManagerIdOrderByDateListedDesc(int managerId);
}
