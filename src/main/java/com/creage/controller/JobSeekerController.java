package com.creage.controller;

import com.creage.dto.StudentProfileDTO;
import com.creage.model.Skill;
import com.creage.model.JobApplication;
import com.creage.model.JobSeekerProfile;
import com.creage.serviceImpl.SkillServiceImpl;
import com.creage.serviceImpl.JobApplicationServiceImpl;
import com.creage.serviceImpl.JobSeekerProfileServiceImpl;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jobseeker-profiles")
@RequiredArgsConstructor
public class JobSeekerController {

    private final JobSeekerProfileServiceImpl studentProfileService;
    
    private final SkillServiceImpl skillService;
    

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOrUpdateProfile(@RequestBody StudentProfileDTO dto) {
        return studentProfileService.createOrUpdateStudentProfile(dto);
    }


    @GetMapping("/{userId}")
    public ResponseEntity<JobSeekerProfile> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(studentProfileService.getStudentProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<JobSeekerProfile> updateProfile(@PathVariable Long userId, 
                                                        @RequestBody StudentProfileDTO dto) {
        return ResponseEntity.ok(studentProfileService.updateStudentProfile(userId, dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        studentProfileService.deleteStudentProfile(userId);
        return ResponseEntity.ok("Profile deleted successfully.");
    }
    
    @PostMapping("/skill")
    public ResponseEntity<Skill> createSkill(@RequestParam String skillName) {
        return ResponseEntity.ok(skillService.createSkill(skillName));
    }

    @PostMapping("/skills")
    public ResponseEntity<List<Skill>> addMultipleSkills(@RequestBody Set<String> skillNames) {
        return ResponseEntity.ok(skillService.addMultipleSkills(skillNames));
    }

    @GetMapping("/skill")
    public ResponseEntity<List<Skill>> getAllSkills() {
        return ResponseEntity.ok(skillService.getAllSkills());
    }

    @GetMapping("/skill/{skillId}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long skillId) {
        return ResponseEntity.ok(skillService.getSkillById(skillId));
    }

    @PutMapping("/skill/{skillId}")
    public ResponseEntity<Skill> updateSkill(@PathVariable Long skillId, @RequestParam String newSkillName) {
        return ResponseEntity.ok(skillService.updateSkill(skillId, newSkillName));
    }

    @DeleteMapping("/skill/{skillId}")
    public ResponseEntity<String> deleteSkill(@PathVariable Long skillId) {
        skillService.deleteSkill(skillId);
        return ResponseEntity.ok("Skill deleted successfully.");
    }
    
}
