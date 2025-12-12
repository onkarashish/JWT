package com.example.unihire.repository;

import com.example.unihire.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    Optional<Skill> findBySkillName(String skillName);
}
