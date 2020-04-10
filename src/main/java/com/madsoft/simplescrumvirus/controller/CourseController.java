package com.madsoft.simplescrumvirus.controller;

import com.madsoft.simplescrumvirus.exception.InvalidDatesException;
import com.madsoft.simplescrumvirus.model.Course;
import com.madsoft.simplescrumvirus.model.CourseEnrollment;
import com.madsoft.simplescrumvirus.exception.model.Error;
import com.madsoft.simplescrumvirus.model.User;
import com.madsoft.simplescrumvirus.service.CourseEnrollmentService;
import com.madsoft.simplescrumvirus.service.CourseService;
import com.madsoft.simplescrumvirus.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(value = "/course")
@RequiredArgsConstructor
public class CourseController {
    private final CourseService courseService;
    private final UserService userService;
    private final CourseEnrollmentService courseEnrollmentService;

    @GetMapping(value = "/")
    public ResponseEntity<List<Course>> fetchAllCourses() {
        return ResponseEntity.ok(courseService.fetchAllCourses());
    }

    @GetMapping(value = "/{courseId}")
    public ResponseEntity<Course> fetchCourseById(@PathVariable String courseId) {
        return ResponseEntity.ok(courseService.fetchCourseById(Long.parseLong(courseId)));
    }

    @GetMapping(value = "/{courseId}/overdue")
    public ResponseEntity<List<User>> fetchOverdueUsersForGivenCourse(@PathVariable String courseId) {
        return ResponseEntity.ok(userService.fetchOverdueUsersForGivenCourse(Long.parseLong(courseId)));
    }

    @PostMapping(value = "/")
    public ResponseEntity<Course> addOrUpdateCourse(@RequestBody Course course) {
        return ResponseEntity.ok(courseService.addOrUpdateCourse(course));
    }

    @PostMapping(value = "/{courseId}/enroll/{userId}")
    public ResponseEntity<CourseEnrollment> enrollUserToCourse(@PathVariable String courseId, @PathVariable String userId) {
        return ResponseEntity.ok(courseEnrollmentService.enrollUserToCourse(Long.parseLong(userId), Long.parseLong(courseId)));
    }

    @PostMapping(value = "/{courseId}/finish/{userId}")
    public ResponseEntity<CourseEnrollment> finishCourseForUser(@PathVariable String courseId, @PathVariable String userId) {
        return ResponseEntity.ok(courseEnrollmentService.finishCourseForUser(Long.parseLong(userId), Long.parseLong(courseId)));
    }

    @ExceptionHandler({ConstraintViolationException.class, IllegalArgumentException.class})
    public ResponseEntity<Error> handleValidationExceptions(RuntimeException ex) {
        return ResponseEntity.status(500).body(new Error(ex.getMessage()));
    }

    @ExceptionHandler({InvalidDatesException.class})
    public ResponseEntity<Error> handleInvalidDatesException(InvalidDatesException ex) {
        return ResponseEntity.status(400).body(new Error(ex.getMessage()));
    }
}
