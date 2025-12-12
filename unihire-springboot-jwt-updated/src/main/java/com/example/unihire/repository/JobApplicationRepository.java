package com.example.unihire.repository;

import com.example.unihire.model.JobApplication;
import com.example.unihire.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Integer> {
    List<JobApplication> findByStudent(UserInfo student);
    List<JobApplication> findByJobId(Integer jobId);
    Optional<JobApplication> findByJobIdAndStudent(Integer jobId, UserInfo student);
}
