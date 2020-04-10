package com.madsoft.simplescrumvirus.repository;

import com.madsoft.simplescrumvirus.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
