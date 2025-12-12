package com.example.unihire.service;

import com.example.unihire.exception.NotFoundException;
import com.example.unihire.exception.UnauthorizedException;
import com.example.unihire.model.Job;
import com.example.unihire.model.UserInfo;
import com.example.unihire.repository.JobRepository;
import com.example.unihire.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public Job createJob(Job job, String companyUsername) {
        UserInfo company = userRepository.findByUsername(companyUsername).orElseThrow(() -> new NotFoundException("Company not found"));
        if (!company.getRoles().name().equals("COMPANY")) throw new UnauthorizedException("Unauthorized: not a company");
        job.setPostedBy(company);
        return jobRepository.save(job);
    }

    public List<Job> listAll() {
        return jobRepository.findAll();
    }

    public List<Job> listByCompany(String companyUsername) {
        UserInfo company = userRepository.findByUsername(companyUsername).orElseThrow(() -> new NotFoundException("Company not found"));
        return jobRepository.findByPostedBy(company);
    }

    public Job updateJob(Integer id, Job update, String companyUsername) {
        var job = jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job not found"));
        if (!job.getPostedBy().getUsername().equals(companyUsername)) throw new UnauthorizedException("Not owner");
        job.setTitle(update.getTitle());
        job.setDescription(update.getDescription());
        job.setSalary(update.getSalary());
        return jobRepository.save(job);
    }

    public void deleteJob(Integer id, String companyUsername) {
        var job = jobRepository.findById(id).orElseThrow(() -> new NotFoundException("Job not found"));
        if (!job.getPostedBy().getUsername().equals(companyUsername)) throw new UnauthorizedException("Not owner");
        jobRepository.delete(job);
    }
}
