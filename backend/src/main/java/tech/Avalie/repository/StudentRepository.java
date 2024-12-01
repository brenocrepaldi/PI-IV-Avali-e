package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Student;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends MongoRepository<Student,ObjectId> {
    Optional<Student> findByEmail(String email);
    Optional<List<Student>> findByCourse(ObjectId course);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Student>> findByName(String name);

    Optional<Student> findByRa(String ra);

    Optional<List<Student>> findByScheduleIn(List<String> schedule);
}
