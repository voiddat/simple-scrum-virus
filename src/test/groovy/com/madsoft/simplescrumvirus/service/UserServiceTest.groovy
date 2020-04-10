package com.madsoft.simplescrumvirus.service

import com.madsoft.simplescrumvirus.model.Course
import com.madsoft.simplescrumvirus.model.CourseEnrollment
import com.madsoft.simplescrumvirus.model.User
import com.madsoft.simplescrumvirus.repository.CourseRepository
import spock.lang.Specification

import javax.persistence.EntityNotFoundException
import java.time.LocalDateTime

class UserServiceTest extends Specification {
    CourseRepository courseRepository = Mock()
    Course course = Mock()
    CourseEnrollment courseEnrollmentTimely = Mock()
    CourseEnrollment courseEnrollmentOverdue = Mock()
    User userTimely = Mock()
    User userOverdue = Mock()
    UserService userService = new UserService(courseRepository)

    def 'overdue - should throw EntityNotFoundException - no course'() {
        given:
        courseRepository.findById(_) >> Optional.empty()

        when:
        userService.fetchOverdueUsersForGivenCourse(1L)

        then:
        EntityNotFoundException ex = thrown()
    }

    def 'overdue - should return one overdue user - no finish time'() {
        given:
        course.getDeadline() >> LocalDateTime.now()
        courseRepository.findById(_) >> Optional.of(course)
        courseEnrollmentOverdue.getUser() >> userOverdue
        courseEnrollmentOverdue.getFinishTime() >> Optional.empty()
        courseEnrollmentTimely.getUser() >> userTimely
        courseEnrollmentTimely.getFinishTime() >> Optional.of(LocalDateTime.now().minusDays(1L))
        course.getCourseEnrollments() >> [courseEnrollmentTimely, courseEnrollmentOverdue]

        when:
        List<User> result = userService.fetchOverdueUsersForGivenCourse(1L)

        then:
        result.size() == 1
    }

    def 'overdue - should return one overdue user - with finish time'() {
        given:
        course.getDeadline() >> LocalDateTime.now()
        courseRepository.findById(_) >> Optional.of(course)
        courseEnrollmentOverdue.getUser() >> userOverdue
        courseEnrollmentOverdue.getFinishTime() >> Optional.of(LocalDateTime.now().plusDays(1L))
        courseEnrollmentTimely.getUser() >> userTimely
        courseEnrollmentTimely.getFinishTime() >> Optional.of(LocalDateTime.now().minusDays(1L))
        course.getCourseEnrollments() >> [courseEnrollmentTimely, courseEnrollmentOverdue]

        when:
        List<User> result = userService.fetchOverdueUsersForGivenCourse(1L)

        then:
        result.size() == 1
    }
}
