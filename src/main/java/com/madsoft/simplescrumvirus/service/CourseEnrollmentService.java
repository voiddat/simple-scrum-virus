package com.madsoft.simplescrumvirus.service;

import com.madsoft.simplescrumvirus.model.Course;
import com.madsoft.simplescrumvirus.model.CourseEnrollment;
import com.madsoft.simplescrumvirus.model.User;
import com.madsoft.simplescrumvirus.repository.CourseEnrollmentRepository;
import com.madsoft.simplescrumvirus.repository.CourseRepository;
import com.madsoft.simplescrumvirus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseEnrollmentService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseEnrollmentRepository courseEnrollmentRepository;

    @Transactional
    public CourseEnrollment enrollUserToCourse(long userId, long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        if (!isValidUsername(user)) {
            throw new IllegalArgumentException("Invalid username");
        }

        if (isAlreadyEnrolled(userId, courseId)) {
            throw new IllegalArgumentException("User id=" + userId + " is already enrolled to course id=" + courseId);
        }

        CourseEnrollment courseEnrollment = CourseEnrollment.builder()
                .user(user)
                .course(course)
                .build();
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    @Transactional
    public CourseEnrollment finishCourseForUser(long userId, long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course id=" + courseId + " does not exist");
        }
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("User id=" + userId + " does not exist");
        }
        CourseEnrollment courseEnrollment = courseEnrollmentRepository.findCourseEnrollmentByUser_IdAndCourse_Id(userId, courseId).orElseThrow(EntityNotFoundException::new);

        courseEnrollment.setFinishTime(LocalDateTime.now());
        return courseEnrollmentRepository.save(courseEnrollment);
    }

    private boolean isValidUsername(User user) {
        return User.userList.contains(user.getUsername());
    }

    private boolean isAlreadyEnrolled(long userId, long courseId) {
        return courseEnrollmentRepository.existsCourseEnrollmentByUser_IdAndCourse_Id(userId, courseId);
    }

}
