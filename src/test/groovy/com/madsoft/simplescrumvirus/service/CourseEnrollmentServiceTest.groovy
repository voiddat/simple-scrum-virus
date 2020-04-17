package com.madsoft.simplescrumvirus.service


import com.madsoft.simplescrumvirus.model.Course
import com.madsoft.simplescrumvirus.model.CourseEnrollment
import com.madsoft.simplescrumvirus.model.User
import com.madsoft.simplescrumvirus.repository.CourseEnrollmentRepository
import com.madsoft.simplescrumvirus.repository.CourseRepository
import com.madsoft.simplescrumvirus.repository.UserRepository
import spock.lang.Specification

import javax.persistence.EntityNotFoundException

class CourseEnrollmentServiceTest extends Specification {
    CourseRepository courseRepository = Mock()
    UserRepository userRepository = Mock()
    CourseEnrollmentRepository courseEnrollmentRepository = Mock()
    Course course = Mock()
    User user = Mock()
    CourseEnrollment courseEnrollment = Mock()
    CourseEnrollmentService courseEnrollmentService = new CourseEnrollmentService(
            courseRepository,
            userRepository,
            courseEnrollmentRepository
    )

    def 'enroll - should throw EntityNotFoundException - no course'() {
        given:
        courseRepository.findById(_) >> Optional.empty()
        courseEnrollmentRepository = Mock()

        when:
        courseEnrollmentService.enrollUserToCourse(1L, 1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'enroll - should throw EntityNotFoundException - no user'() {
        given:
        courseRepository.findById(_) >> Optional.of(course)
        userRepository.findById(_) >> Optional.empty()

        when:
        courseEnrollmentService.enrollUserToCourse(1L, 1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'enroll - should throw IllegalArgumentException - user already enrolled'() {
        given:
        user.getUsername() >> 'Kowalski'
        courseRepository.findById(_) >> Optional.of(course)
        userRepository.findById(_) >> Optional.of(user)
        courseEnrollmentRepository.existsCourseEnrollmentByUser_IdAndCourse_Id(_, _) >> true

        when:
        courseEnrollmentService.enrollUserToCourse(1L, 1L)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'User id=1 is already enrolled to course id=1'
    }

    def 'enroll - should throw InvalidUsernameException - invalid username'() {
        given:
        user.getUsername() >> 'NotFromList'
        courseRepository.findById(_) >> Optional.of(course)
        userRepository.findById(_) >> Optional.of(user)

        when:
        courseEnrollmentService.enrollUserToCourse(1L, 1L)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'Invalid username'
    }

    def 'enroll - should enroll user to course'() {
        given:
        user.getUsername() >> 'Smith'
        courseRepository.findById(_) >> Optional.of(course)
        userRepository.findById(_) >> Optional.of(user)
        courseEnrollmentRepository.findCourseEnrollmentByUser_IdAndCourse_Id(_, _) >> Optional.empty()

        when:
        courseEnrollmentService.enrollUserToCourse(1L, 1L)

        then:
        1 * courseEnrollmentRepository.save(_)
    }

    def 'finish - should throw EntityNotFoundException - no course'() {
        given:
        courseRepository.existsById(_) >> false

        when:
        courseEnrollmentService.finishCourseForUser(1L, 1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'finish - should throw EntityNotFoundException - no user'() {
        given:
        courseRepository.existsById(_) >> true
        userRepository.existsById(_) >> false

        when:
        courseEnrollmentService.finishCourseForUser(1L, 1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'finish - should throw EntityNotFoundException - no courseEnrollment'() {
        given:
        courseRepository.existsById(_) >> true
        userRepository.existsById(_) >> true
        courseEnrollmentRepository.findCourseEnrollmentByUser_IdAndCourse_Id(_, _) >> Optional.empty()

        when:
        courseEnrollmentService.finishCourseForUser(1L, 1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'finish - should finish course correctly'() {
        given:
        courseRepository.existsById(_) >> true
        userRepository.existsById(_) >> true
        courseEnrollmentRepository.findCourseEnrollmentByUser_IdAndCourse_Id(_, _) >> Optional.of(courseEnrollment)

        when:
        courseEnrollmentService.finishCourseForUser(1L, 1L)

        then:
        noExceptionThrown()
    }
}
