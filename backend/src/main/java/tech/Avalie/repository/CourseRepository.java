package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Course;
import tech.Avalie.entities.Student;

import java.util.List;
import java.util.Optional;
@Repository
public interface CourseRepository extends MongoRepository<Course, ObjectId> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Course>> findByName(String name);
}
