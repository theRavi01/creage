package com.creage.serviceImpl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.creage.dto.StudentProfileDTO;
import com.creage.exception.ResourceNotFoundException;
import com.creage.exception.ValidationException;
import com.creage.model.Skill;
import com.creage.model.JobSeekerProfile;
import com.creage.model.Users;
import com.creage.repository.SkillRepository;
import com.creage.repository.JobSeekerProfileRepository;
import com.creage.repository.UsersRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobSeekerProfileServiceImpl {

	
    private final JobSeekerProfileRepository jobSeekerProfileRepository;
    private final UsersRepository usersRepository;
    private final SkillRepository skillRepository;
    
    @Transactional
    public JobSeekerProfile createStudentProfile(StudentProfileDTO dto) {
        try {
            log.info("Creating new student profile for User ID: {}", dto.getUserId());

            Users user = usersRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            List<Skill> skills = skillRepository.findAllById(dto.getSkillIds());
            if (skills.isEmpty()) {
                throw new ValidationException("At least one valid skill is required");
            }

            JobSeekerProfile profile = JobSeekerProfile.builder()
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

            return jobSeekerProfileRepository.save(profile);
        } catch (Exception e) {
            log.error("Error creating student profile: {}", e.getMessage());
            throw new ValidationException("Failed to create profile: " + e.getMessage());
        }
    }

    public JobSeekerProfile getStudentProfile(Long userId) {
        log.info("Fetching student profile for User ID: {}", userId);
        return jobSeekerProfileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found for User ID: " + userId));
    }

    @Transactional
    public JobSeekerProfile updateStudentProfile(Long userId, StudentProfileDTO dto) {
        log.info("Updating student profile for User ID: {}", userId);
        JobSeekerProfile profile = getStudentProfile(userId);

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

        return jobSeekerProfileRepository.save(profile);
    }

    @Transactional
    public void deleteStudentProfile(Long userId) {
        log.info("Deleting student profile for User ID: {}", userId);
        JobSeekerProfile profile = getStudentProfile(userId);
        jobSeekerProfileRepository.delete(profile);
    }
}
