package com.madsoft.simplescrumvirus.service;

import com.madsoft.simplescrumvirus.model.Course;
import com.madsoft.simplescrumvirus.model.CourseEnrollment;
import com.madsoft.simplescrumvirus.model.User;
import com.madsoft.simplescrumvirus.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final CourseRepository courseRepository;

    public List<User> fetchOverdueUsersForGivenCourse(long courseId) {
        Course course = courseRepository.findById(courseId).orElseThrow(EntityNotFoundException::new);

        return course.getCourseEnrollments().stream()
                .filter(courseEnrollment -> isOverdue(courseEnrollment.getFinishTime(), course.getDeadline()))
                .map(CourseEnrollment::getUser)
                .collect(Collectors.toList());
    }

    private boolean isOverdue(Optional<LocalDateTime> finishTimeOpt, LocalDateTime courseDeadline) {
        return finishTimeOpt
                .map(finishTime -> finishTime.isAfter(courseDeadline))
                .orElseGet(() -> LocalDateTime.now().isAfter(courseDeadline));
    }


}
