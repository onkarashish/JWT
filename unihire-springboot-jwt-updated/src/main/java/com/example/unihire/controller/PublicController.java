package com.example.unihire.controller;

import com.example.unihire.service.AuthService;
import com.example.unihire.service.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final AuthService authService;
    private final JobService jobService;

    public PublicController(AuthService authService, JobService jobService) {
        this.authService = authService;
        this.jobService = jobService;
    }

    @GetMapping("/job/search")
    public ResponseEntity<List<?>> searchJobs() {
        return ResponseEntity.ok(jobService.listAll());
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String token = authService.login(username, password);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String role = body.get("role"); // STUDENT or COMPANY
        String token = authService.register(username, password, role);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("token", token));
    }
}
