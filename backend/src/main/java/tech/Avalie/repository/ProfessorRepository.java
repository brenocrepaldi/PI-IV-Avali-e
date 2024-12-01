package tech.Avalie.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Professor;

import java.util.List;
import java.util.Optional;
@Repository
public interface ProfessorRepository extends MongoRepository<Professor, ObjectId> {
    Optional<Professor> findByEmail(String email);
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Professor>> findByName(String name);
    Optional<List<Professor>> findByDisciplines(String disciplines);
}
