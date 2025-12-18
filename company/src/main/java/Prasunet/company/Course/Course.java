package Prasunet.company.Course;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    private String id;              // Unique ID (MongoDB ObjectId)

    private String title;           // Course title

    private String description;     // Course description

    private String mentorId;        // User ID of mentor who created the course

    private CourseStatus status;      // to check whether a course can be deleted / edited or not

    private Instant createdAt;      // Creation timestamp
}
