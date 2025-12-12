package com.example.unihire.controller;

import com.example.unihire.model.JobApplication;
import com.example.unihire.service.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth/student")
@PreAuthorize("hasRole('STUDENT')")
public class StudentController {

    private final ApplicationService applicationService;

    public StudentController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping("/application")
    public ResponseEntity<List<JobApplication>> myApplications(@AuthenticationPrincipal String username) {
        return ResponseEntity.ok(applicationService.getByStudent(username));
    }

    @PostMapping("/apply/{jobId}")
    public ResponseEntity<JobApplication> apply(@PathVariable Integer jobId, @AuthenticationPrincipal String username) {
        return ResponseEntity.status(HttpStatus.CREATED).body(applicationService.apply(jobId, username));
    }

    @DeleteMapping("/application/{applicationId}")
    public ResponseEntity<Void> withdraw(@PathVariable Integer applicationId, @AuthenticationPrincipal String username) {
        applicationService.withdraw(applicationId, username);
        return ResponseEntity.noContent().build(); //204 NoContent
    }
}
