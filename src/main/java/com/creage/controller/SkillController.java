package com.creage.controller;

import com.creage.model.Skill;
import com.creage.serviceImpl.SkillServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillServiceImpl skillService;

    @PostMapping
    public ResponseEntity<Skill> createSkill(@RequestParam String skillName) {
        return ResponseEntity.ok(skillService.createSkill(skillName));
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<Skill>> addMultipleSkills(@RequestBody Set<String> skillNames) {
        return ResponseEntity.ok(skillService.addMultipleSkills(skillNames));
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/{skillId}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long skillId) {
        return ResponseEntity.ok(skillService.getSkillById(skillId));
    }

    @PutMapping("/{skillId}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long skillId, @RequestParam String newSkillName) {
        return ResponseEntity.ok(skillService.updateSkill(skillId, newSkillName));
    }

    @DeleteMapping("/{skillId}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok("Skill deleted successfully.");
    }
}
