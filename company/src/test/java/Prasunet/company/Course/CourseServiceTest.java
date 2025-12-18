package Prasunet.company.Course;

import Prasunet.company.User.Role;
import Prasunet.company.User.User;
import Prasunet.company.User.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        approvedMentor = User.builder()
                .id("mentor123")
                .role(Role.MENTOR)
                .approved(true)
                .build();
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        // GIVEN
        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        when(courseRepository.save(any(Course.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        Course course = courseService.createCourse(
                "mentor123",
                "Spring Boot",
                "Learn Spring Boot"
        );

        // THEN
        assertNotNull(course);
        assertEquals("Spring Boot", course.getTitle());
        assertEquals("mentor123", course.getMentorId());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldFailIfUserIsNotMentor() {
        // GIVEN
        User student = User.builder()
                .id("student1")
                .role(Role.STUDENT)
                .approved(true)
                .build();

        when(userRepository.findById("student1"))
                .thenReturn(Optional.of(student));

        // THEN
        assertThrows(RuntimeException.class, () ->
                courseService.createCourse("student1", "Test", "Desc")
        );
    }

    @Test
    void shouldFailIfMentorNotApproved() {
        // GIVEN
        User unapprovedMentor = User.builder()
                .id("mentor2")
                .role(Role.MENTOR)
                .approved(false)
                .build();

        when(userRepository.findById("mentor2"))
                .thenReturn(Optional.of(unapprovedMentor));

        // THEN
        assertThrows(RuntimeException.class, () ->
                courseService.createCourse("mentor2", "Test", "Desc")
        );
    }

    @Test
    void shouldFailIfTitleIsEmpty() {
        when(userRepository.findById("mentor123"))
                .thenReturn(Optional.of(approvedMentor));

        assertThrows(RuntimeException.class, () ->
                courseService.createCourse("mentor123", "", "Desc")
        );
    }
}

