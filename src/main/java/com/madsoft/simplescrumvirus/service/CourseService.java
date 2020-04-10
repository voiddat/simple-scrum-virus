package com.madsoft.simplescrumvirus.service;

import com.madsoft.simplescrumvirus.exception.InvalidDatesException;
import com.madsoft.simplescrumvirus.model.Course;
import com.madsoft.simplescrumvirus.model.CourseEnrollment;
import com.madsoft.simplescrumvirus.model.User;
import com.madsoft.simplescrumvirus.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;

    public Course addOrUpdateCourse(Course course) {
        if (course.getDeadline().isBefore(course.getStartDate())) {
            throw new InvalidDatesException("Invalid dates");
        }

        if (!isValidUsername(course)) {
            throw new IllegalArgumentException("Incorrect usernames");
        }

        return courseRepository.save(course);
    }

    public List<Course> fetchAllCourses() {
        return courseRepository.findAll();
    }

    public Course fetchCourseById(long id) {
        return courseRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private boolean isValidUsername(Course course) {
        return course.getCourseEnrollments().stream()
                .map(CourseEnrollment::getUser)
                .map(User::getUsername)
                .allMatch(username -> User.userList.contains(username));
    }
}
