package com.madsoft.simplescrumvirus.service

import com.madsoft.simplescrumvirus.exception.InvalidDatesException
import com.madsoft.simplescrumvirus.model.Course
import com.madsoft.simplescrumvirus.model.CourseEnrollment
import com.madsoft.simplescrumvirus.model.User
import com.madsoft.simplescrumvirus.repository.CourseRepository
import spock.lang.Specification

import java.time.LocalDateTime

class CourseServiceTest extends Specification {
    CourseRepository courseRepository = Mock()
    Course course = Mock()
    User user = Mock()
    User scrumEvangelist = Mock()
    CourseEnrollment courseEnrollment = Mock()
    CourseService courseService = new CourseService(courseRepository)

    def 'should throw InvalidDatesException'() {
        given:
        course.getDeadline() >> LocalDateTime.now().minusDays(1L)
        course.getStartDate() >> LocalDateTime.now()

        when:
        courseService.addOrUpdateCourse(course)

        then:
        InvalidDatesException ex = thrown()
        ex.message == 'Invalid dates'
    }

    def 'should throw CourseInvalidException (invalid names)'() {
        given:
        user.getUsername() >> 'NotFromList'
        courseEnrollment.getUser() >> user
        course.getStartDate() >> LocalDateTime.now().minusDays(1L)
        course.getDeadline() >> LocalDateTime.now()
        course.getCourseEnrollments() >> [courseEnrollment]
        course.getScrumEvangelist() >> scrumEvangelist

        when:
        courseService.addOrUpdateCourse(course)

        then:
        IllegalArgumentException ex = thrown()
        ex.message == 'Incorrect usernames'
    }

    def 'should create course'() {
        given:
        user.getUsername() >> 'Kowalski'
        course.getStartDate() >> LocalDateTime.now().minusDays(1L)
        course.getDeadline() >> LocalDateTime.now()
        course.getCourseEnrollments() >> []
        course.getScrumEvangelist() >> user

        when:
        courseService.addOrUpdateCourse(course)

        then:
        1 * courseRepository.save(_) >> course
    }
}
