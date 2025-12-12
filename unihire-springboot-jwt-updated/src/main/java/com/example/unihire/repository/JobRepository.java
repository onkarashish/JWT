package com.example.unihire.repository;

import com.example.unihire.model.Job;
import com.example.unihire.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Integer> {
    List<Job> findByPostedBy(UserInfo company);
}
