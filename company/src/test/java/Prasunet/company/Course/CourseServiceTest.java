package Prasunet.company.Course;

import Prasunet.company.User.Role;
import Prasunet.company.User.User;
import Prasunet.company.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private User approvedMentor;
    private User unapprovedMentor;
    private User student;
    private Course draftCourse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        approvedMentor = User.builder()
                .id("mentor123")
                .role(Role.MENTOR)
                .approved(true)
                .build();

        unapprovedMentor = User.builder()
                .id("mentor999")
                .role(Role.MENTOR)
                .approved(false)
                .build();

        student = User.builder()
                .id("student123")
                .role(Role.STUDENT)
                .approved(true)
                .build();

        draftCourse = Course.builder()
                .id("course1")
                .title("Spring Boot")
                .description("Learn Spring Boot")
                .mentorId("mentor123")
                .createdAt(Instant.now())
                .status(CourseStatus.DRAFT)
                .build();
    }

    // ======================
    // CREATE COURSE TESTS
    // ======================

    @Test
    void shouldCreateCourseSuccessfully() {
        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Course course = courseService.createCourse(
                "mentor123",
                "Java",
                "Java Backend Course"
        );

        assertNotNull(course);
        assertEquals("Java", course.getTitle());
        assertEquals("mentor123", course.getMentorId());
        assertEquals(CourseStatus.DRAFT, course.getStatus());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldFailToCreateCourseIfUserIsNotMentor() {
        when(userRepository.findById("student123"))
                .thenReturn(Optional.of(student));

        assertThrows(RuntimeException.class, () ->
                courseService.createCourse(
                        "student123",
                        "Test",
                        "Desc"
                )
        );
    }

    @Test
    void shouldFailToCreateCourseIfMentorNotApproved() {
        when(userRepository.findById("mentor999"))
                .thenReturn(Optional.of(unapprovedMentor));

        assertThrows(RuntimeException.class, () ->
                courseService.createCourse(
                        "mentor999",
                        "Test",
                        "Desc"
                )
        );
    }

    // ======================
    // UPDATE COURSE TESTS
    // ======================

    @Test
    void shouldUpdateDraftCourseSuccessfully() {
        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.findById("course1"))
                .thenReturn(Optional.of(draftCourse));

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Course updatedCourse = courseService.updateCourse(
                "mentor123",
                "course1",
                "Updated Title",
                "Updated Description"
        );

        assertEquals("Updated Title", updatedCourse.getTitle());
        assertEquals("Updated Description", updatedCourse.getDescription());
    }

    @Test
    void shouldFailToUpdateCourseIfNotOwner() {
        draftCourse.setMentorId("otherMentor");

        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.findById("course1"))
                .thenReturn(Optional.of(draftCourse));

        assertThrows(RuntimeException.class, () ->
                courseService.updateCourse(
                        "mentor123",
                        "course1",
                        "Title",
                        "Desc"
                )
        );
    }

    @Test
    void shouldFailToUpdateIfCourseNotDraft() {
        draftCourse.setStatus(CourseStatus.PUBLISHED);

        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.findById("course1"))
                .thenReturn(Optional.of(draftCourse));

        assertThrows(RuntimeException.class, () ->
                courseService.updateCourse(
                        "mentor123",
                        "course1",
                        "Title",
                        "Desc"
                )
        );
    }

    // ======================
    // ARCHIVE COURSE TESTS
    // ======================

    @Test
    void shouldArchiveCourseSuccessfully() {
        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.findById("course1"))
                .thenReturn(Optional.of(draftCourse));

        courseService.archiveCourse("mentor123", "course1");

        assertEquals(CourseStatus.ARCHIVED, draftCourse.getStatus());
        verify(courseRepository, times(1)).save(draftCourse);
    }

    @Test
    void shouldFailToArchiveCourseIfNotOwner() {
        draftCourse.setMentorId("otherMentor");

        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.findById("course1"))
                .thenReturn(Optional.of(draftCourse));

        assertThrows(RuntimeException.class, () ->
                courseService.archiveCourse("mentor123", "course1")
        );
    }
}
