package com.creage.controller;

import com.creage.dto.StudentProfileDTO;
import com.creage.model.StudentProfile;
import com.creage.serviceImpl.StudentProfileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
public class StudentProfileController {

    private final StudentProfileServiceImpl studentProfileService;

    @PostMapping
    public ResponseEntity<StudentProfile> createProfile(@RequestBody StudentProfileDTO dto) {
        return ResponseEntity.ok(studentProfileService.createStudentProfile(dto));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<StudentProfile> getProfile(@PathVariable Long userId) {
        return ResponseEntity.ok(studentProfileService.getStudentProfile(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<StudentProfile> updateProfile(@PathVariable Long userId, 
                                                        @RequestBody StudentProfileDTO dto) {
        return ResponseEntity.ok(studentProfileService.updateStudentProfile(userId, dto));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long userId) {
        studentProfileService.deleteStudentProfile(userId);
        return ResponseEntity.ok("Profile deleted successfully.");
    }
}
