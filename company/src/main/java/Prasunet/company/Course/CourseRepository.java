package Prasunet.company.Course;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String> {

    List<Course> findByMentorId(String mentorId);
}

