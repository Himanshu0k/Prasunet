package Prasunet.company.Course;

import Prasunet.company.User.Role;
import Prasunet.company.User.User;
import Prasunet.company.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public Course createCourse(String mentorId, String title, String description) {

        User mentor = validateMentor(mentorId);

        Course course = Course.builder()
                .title(title)
                .description(description)
                .mentorId(mentorId)
                .createdAt(Instant.now())
                .status(CourseStatus.DRAFT)
                .build();

        return courseRepository.save(course);
    }

    public Course updateCourse(String mentorId, String courseId, String title, String description) {

        User mentor = validateMentor(mentorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getMentorId().equals(mentor.getId())) {
            throw new RuntimeException("Mentor does not own this course");
        }

        if (course.getStatus() != CourseStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT courses can be updated");
        }

        course.setTitle(title);
        course.setDescription(description);

        return courseRepository.save(course);
    }

    public void archiveCourse(String mentorId, String courseId) {

        User mentor = validateMentor(mentorId);

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getMentorId().equals(mentor.getId())) {
            throw new RuntimeException("Mentor does not own this course");
        }

        course.setStatus(CourseStatus.ARCHIVED);
        courseRepository.save(course);
    }

    private User validateMentor(String mentorId) {
        User mentor = userRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("Mentor not found"));

        if (mentor.getRole() != Role.MENTOR) {
            System.out.println("error");
            throw new RuntimeException("Only mentors allowed");
        }

        if (!mentor.isApproved()) {
            throw new RuntimeException("Mentor not approved");
        }
        return mentor;
    }
}
