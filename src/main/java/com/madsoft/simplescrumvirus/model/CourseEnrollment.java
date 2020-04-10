package com.madsoft.simplescrumvirus.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseEnrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NotNull
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonBackReference
    private Course course;

    private LocalDateTime finishTime;

    public Optional<LocalDateTime> getFinishTime() {
        return Optional.ofNullable(finishTime);
    }
}
