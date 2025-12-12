package com.example.unihire.controller;

import com.example.unihire.model.Job;
import com.example.unihire.model.JobApplication;
import com.example.unihire.model.enums.ApplicationStatus;
import com.example.unihire.service.ApplicationService;
import com.example.unihire.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth/company")
@PreAuthorize("hasRole('COMPANY')")
public class CompanyController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public CompanyController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @PostMapping("/job")
    public ResponseEntity<Job> createJob(@RequestBody Job job, @AuthenticationPrincipal String username) {
        Job created = jobService.createJob(job, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> myJobs(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(jobService.listByCompany(username));
    }

    @PutMapping("/job/{jobId}")
    public ResponseEntity<Job> updateJob(@PathVariable Integer jobId, @RequestBody Job update, @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(jobService.updateJob(jobId, update, username));
    }

    @DeleteMapping("/job/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Integer jobId, @AuthenticationPrincipal String username) {
        jobService.deleteJob(jobId, username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/job/{jobId}/applications")
    public ResponseEntity<List<JobApplication>> jobApplications(@PathVariable Integer jobId, @AuthenticationPrincipal String username) {
        return ResponseEntity.ok(applicationService.getByJob(jobId, username));
    }

    @PutMapping("/application/{applicationId}")
    public ResponseEntity<JobApplication> updateApplicationStatus(@PathVariable Integer applicationId, @RequestBody Map<String,String> body, @AuthenticationPrincipal String username) {
        ApplicationStatus status = ApplicationStatus.valueOf(body.get("status").toUpperCase());
        return ResponseEntity.ok(applicationService.updateStatus(applicationId, status, username));
    }
}
