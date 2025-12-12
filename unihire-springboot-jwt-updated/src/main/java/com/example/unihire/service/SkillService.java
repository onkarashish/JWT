package com.example.unihire.service;

import com.example.unihire.model.Skill;
import com.example.unihire.repository.SkillRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public Skill createIfNotExists(String name) {
        return skillRepository.findBySkillName(name).orElseGet(() -> skillRepository.save(Skill.builder().skillName(name).build()));
    }

    public List<Skill> listAll() {
        return skillRepository.findAll();
    }
}
