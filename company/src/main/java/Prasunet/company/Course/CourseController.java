package Prasunet.company.Course;

import Prasunet.company.Dtos.CourseRequest;
import Prasunet.company.User.Role;
import Prasunet.company.User.User;
import Prasunet.company.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final UserRepository userRepository;

    // ======================================
    // CREATE COURSE
    // ======================================
    @PostMapping
    public ResponseEntity<Course> createCourse(
            @AuthenticationPrincipal String currentUserId,
            @RequestBody CourseRequest request
    ) {
        Course course = courseService.createCourse(
                currentUserId,
                request.getTitle(),
                request.getDescription()
        );
        return ResponseEntity.ok(course);
    }

    // ======================================
    // UPDATE COURSE
    // ======================================
    @PutMapping("/{courseId}")
    public ResponseEntity<Course> updateCourse(
            @AuthenticationPrincipal String currentUserId,
            @PathVariable String courseId,
            @RequestBody CourseRequest request
    ) {
        Course updated = courseService.updateCourse(
                currentUserId,
                courseId,
                request.getTitle(),
                request.getDescription()
        );
        return ResponseEntity.ok(updated);
    }

    // ======================================
    // ARCHIVE COURSE
    // ======================================
    @PutMapping("/{courseId}/archive")
    public ResponseEntity<String> archiveCourse(
            @AuthenticationPrincipal String currentUserId,
            @PathVariable String courseId
    ) {
        courseService.archiveCourse(currentUserId, courseId);
        return ResponseEntity.ok("Course archived successfully");
    }
}
