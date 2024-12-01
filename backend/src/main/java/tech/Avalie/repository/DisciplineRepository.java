package tech.Avalie.repository;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import tech.Avalie.entities.Disciplines;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DisciplineRepository extends MongoRepository<Disciplines, ObjectId> {
    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Optional<List<Disciplines>> findByName(String name);
    Optional<List<Disciplines>> findByCourse(ObjectId course);
    Optional<List<Disciplines>> findByStartTimeBetween(LocalTime start_time, LocalTime end_time);
}