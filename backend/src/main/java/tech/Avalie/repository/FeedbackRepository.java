package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import tech.Avalie.entities.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends MongoRepository<Feedback, ObjectId> {

    Optional<List<Feedback>> findByStudent(String student);
    @Query("{ 'discipline': { $regex: ?0, $options: 'i' } }")

    Optional<List<Feedback>> findByDiscipline(String discipline);
    @Query("{ 'date': { $regex: ?0, $options: 'i' } }")

    Optional<List<Feedback>> findByDate(String date);

    @Query("{ 'note': { $regex: ?0, $options: 'i' } }")
    Optional<List<Feedback>> findByNote(int note);
}
