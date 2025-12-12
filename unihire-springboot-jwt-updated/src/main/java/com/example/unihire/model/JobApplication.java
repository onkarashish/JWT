package com.example.unihire.model;

import com.example.unihire.model.enums.ApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications", uniqueConstraints = @UniqueConstraint(columnNames = {"job_id", "student_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class JobApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private UserInfo student;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status;
}
