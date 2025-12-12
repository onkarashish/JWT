package com.example.unihire.service;

import com.example.unihire.exception.ConflictException;
import com.example.unihire.exception.NotFoundException;
import com.example.unihire.exception.UnauthorizedException;
import com.example.unihire.model.Job;
import com.example.unihire.model.JobApplication;
import com.example.unihire.model.UserInfo;
import com.example.unihire.model.enums.ApplicationStatus;
import com.example.unihire.repository.JobApplicationRepository;
import com.example.unihire.repository.JobRepository;
import com.example.unihire.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private final JobApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(JobApplicationRepository applicationRepository, JobRepository jobRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public JobApplication apply(Integer jobId, String studentUsername) {
        UserInfo student = userRepository.findByUsername(studentUsername).orElseThrow(() -> new NotFoundException("Student not found"));
        if (!student.getRoles().name().equals("STUDENT")) throw new UnauthorizedException("Only students can apply");
        Job job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
        var existing = applicationRepository.findByJobIdAndStudent(jobId, student);
        if (existing.isPresent()) throw new ConflictException("Already applied");
        JobApplication app = JobApplication.builder().job(job).student(student).status(ApplicationStatus.PENDING).build();
        return applicationRepository.save(app);
    }

    public List<JobApplication> getByStudent(String studentUsername) {
        UserInfo student = userRepository.findByUsername(studentUsername).orElseThrow(() -> new NotFoundException("Student not found"));
        return applicationRepository.findByStudent(student);
    }

    public List<JobApplication> getByJob(Integer jobId, String companyUsername) {
        var job = jobRepository.findById(jobId).orElseThrow(() -> new NotFoundException("Job not found"));
        if (!job.getPostedBy().getUsername().equals(companyUsername)) throw new UnauthorizedException("Not owner");
        return applicationRepository.findByJobId(jobId);
    }

    public JobApplication updateStatus(Integer applicationId, ApplicationStatus status, String companyUsername) {
        var app = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("Application not found"));
        if (!app.getJob().getPostedBy().getUsername().equals(companyUsername)) throw new UnauthorizedException("Not owner");
        app.setStatus(status);
        return applicationRepository.save(app);
    }

    public void withdraw(Integer applicationId, String studentUsername) {
        var app = applicationRepository.findById(applicationId).orElseThrow(() -> new NotFoundException("Application not found"));
        if (!app.getStudent().getUsername().equals(studentUsername)) throw new UnauthorizedException("Not owner");
        applicationRepository.delete(app);
    }
}
