package com.madsoft.simplescrumvirus.repository;

import com.madsoft.simplescrumvirus.model.CourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, Long> {
    Optional<CourseEnrollment> findCourseEnrollmentByUser_IdAndCourse_Id(long userId, long courseId);
}
