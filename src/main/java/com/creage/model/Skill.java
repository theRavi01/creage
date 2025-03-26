package com.creage.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;  // e.g., "Java", "Spring Boot", "React"

    @ManyToMany(mappedBy = "skills")
    @JsonBackReference  // Prevents infinite recursion
    private List<StudentProfile> studentProfiles;
}
