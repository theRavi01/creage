package com.creage.skill;

import com.creage.exception.ResourceNotFoundException;
import com.creage.exception.ValidationException;
import com.creage.model.Skill;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SkillServiceImpl {

    private final SkillRepository skillRepository;

    @Transactional
    public Skill createSkill(String skillName) {
        try {
            log.info("Creating new skill: {}", skillName);

            if (skillName == null || skillName.trim().isEmpty()) {
                throw new ValidationException("Skill name cannot be empty");
            }

            skillRepository.findByName(skillName).ifPresent(skill -> {
                throw new ValidationException("Skill already exists: " + skillName);
            });

            Skill skill = Skill.builder().name(skillName).build();
            return skillRepository.save(skill);
        } catch (Exception e) {
            log.error("Error creating skill: {}", e.getMessage());
            throw new ValidationException("Failed to create skill: " + e.getMessage());
        }
    }

    @Transactional
    public List<Skill> addMultipleSkills(Set<String> skillNames) {
        try {
            log.info("Adding multiple skills: {}", skillNames);

            if (skillNames == null || skillNames.isEmpty()) {
                throw new ValidationException("Skill list cannot be empty");
            }

            List<Skill> existingSkills = skillRepository.findAll();
            Set<String> existingSkillNames = existingSkills.stream()
                    .map(Skill::getName)
                    .collect(Collectors.toSet());

            List<Skill> newSkills = skillNames.stream()
                    .filter(skill -> !existingSkillNames.contains(skill))
                    .map(skill -> Skill.builder().name(skill).build())
                    .collect(Collectors.toList());

            if (newSkills.isEmpty()) {
                throw new ValidationException("All skills already exist.");
            }

            return skillRepository.saveAll(newSkills);
        } catch (Exception e) {
            log.error("Error adding multiple skills: {}", e.getMessage());
            throw new ValidationException("Failed to add multiple skills: " + e.getMessage());
        }
    }

    public List<Skill> getAllSkills() {
        log.info("Fetching all skills");
        return skillRepository.findAll();
    }

    public Skill getSkillById(Long skillId) {
        log.info("Fetching skill by ID: {}", skillId);
        return skillRepository.findById(skillId)
                .orElseThrow(() -> new ResourceNotFoundException("Skill not found with ID: " + skillId));
    }

    @Transactional
    public Skill updateSkill(Long skillId, String newSkillName) {
        try {
            log.info("Updating skill ID: {} to new name: {}", skillId, newSkillName);

            if (newSkillName == null || newSkillName.trim().isEmpty()) {
                throw new ValidationException("Skill name cannot be empty");
            }

            Skill skill = getSkillById(skillId);
            skill.setName(newSkillName);
            return skillRepository.save(skill);
        } catch (Exception e) {
            log.error("Error updating skill: {}", e.getMessage());
            throw new ValidationException("Failed to update skill: " + e.getMessage());
        }
    }

    @Transactional
    public void deleteSkill(Long skillId) {
        log.info("Deleting skill with ID: {}", skillId);
        Skill skill = getSkillById(skillId);
        skillRepository.delete(skill);
    }
}
