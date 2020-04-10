package com.madsoft.simplescrumvirus.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.MERGE)
    private User scrumEvangelist;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "course")
    @NotNull
    @JsonManagedReference
    private List<CourseEnrollment> courseEnrollments;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime deadline;
}
