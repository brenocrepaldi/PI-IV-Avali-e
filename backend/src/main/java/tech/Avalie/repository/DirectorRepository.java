package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Director;
import tech.Avalie.entities.Professor;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectorRepository extends MongoRepository<Director, ObjectId> {
    Optional<Director> findByEmail(String email);
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Director>> findByName(String name);
    Optional<Director> findByRa (String ra);
    Optional<Director> findByCourse(String course);
}
