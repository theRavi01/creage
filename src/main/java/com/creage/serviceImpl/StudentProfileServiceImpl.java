package com.creage.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.creage.dto.StudentProfileDTO;
import com.creage.exception.ResourceNotFoundException;
import com.creage.exception.ValidationException;
import com.creage.model.Skill;
import com.creage.model.StudentProfile;
import com.creage.model.Users;
import com.creage.repository.SkillRepository;
import com.creage.repository.StudentProfileRepository;
import com.creage.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentProfileServiceImpl {

	
    private final StudentProfileRepository studentProfileRepository;
    private final UsersRepository usersRepository;
    private final SkillRepository skillRepository;
    
    @Transactional
    public StudentProfile createStudentProfile(StudentProfileDTO dto) {
        try {
            log.info("Creating new student profile for User ID: {}", dto.getUserId());

            Users user = usersRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());
            if (skills.isEmpty()) {
                throw new ValidationException("At least one valid skill is required");
            }

            StudentProfile profile = StudentProfile.builder()
                    .userId(user)
                    .firstName(dto.getFirstName())
                    .firstName(dto.getLastName())
                    .headline(dto.getHeadline())
                    .currentPosition(dto.getCurrentPosition())
                    .education(dto.getEducation())
                    .location(dto.getLocation())
                    .industry(dto.getIndustry())
                    .aboutMe(dto.getAboutMe())
                    .openToWork(dto.isOpenToWork())
                    .skills(skills)
                    .build();

            return studentProfileRepository.save(profile);
        } catch (Exception e) {
            log.error("Error creating student profile: {}", e.getMessage());
            throw new ValidationException("Failed to create profile: " + e.getMessage());
        }
    }

    public StudentProfile getStudentProfile(Long userId) {
        log.info("Fetching student profile for User ID: {}", userId);
        return studentProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for User ID: " + userId));
    }

    @Transactional
    public StudentProfile updateStudentProfile(Long userId, StudentProfileDTO dto) {
        log.info("Updating student profile for User ID: {}", userId);
        StudentProfile profile = getStudentProfile(userId);

        profile.setHeadline(dto.getHeadline());
        profile.setCurrentPosition(dto.getCurrentPosition());
        profile.setEducation(dto.getEducation());
        profile.setLocation(dto.getLocation());
        profile.setIndustry(dto.getIndustry());
        profile.setAboutMe(dto.getAboutMe());
        profile.setOpenToWork(dto.isOpenToWork());

        List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());
        if (skills.isEmpty()) {
            throw new ValidationException("At least one valid skill is required");
        }
        profile.setSkills(skills);

        return studentProfileRepository.save(profile);
    }

    @Transactional
    public void deleteStudentProfile(Long userId) {
        log.info("Deleting student profile for User ID: {}", userId);
        StudentProfile profile = getStudentProfile(userId);
        studentProfileRepository.delete(profile);
    }
}
